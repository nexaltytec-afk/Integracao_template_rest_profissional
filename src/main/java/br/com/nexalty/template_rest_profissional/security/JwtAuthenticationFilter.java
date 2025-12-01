package br.com.nexalty.template_rest_profissional.security;
  
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.nexalty.template_rest_profissional.config.ContextProperties;
import br.com.nexalty.template_rest_profissional.oap.InvalidAudienceException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.nexalty.template_rest_profissional.security.SecurityConfig.AUTH_SWAGGER_WHITELIST; 

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
 
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        log.debug("JwtAuthenticationFilter inicializado com jwtTokenUtil: {}", jwtTokenUtil != null); 
    }

    @PostConstruct
    public void init() {
        log.debug("=== JWT AUTHENTICATION FILTER INICIALIZADO ===");
        log.debug("jwtTokenUtil injetado: {}", jwtTokenUtil != null);
    }

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
            if (isWhiteList(request.getRequestURI())) {
                log.debug("URL na whitelist: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");
	   	     log.debug("🧩 [Filtro] Header Authorization recebido: {}", authorizationHeader != null ? "PRESENTE" : "AUSENTE");
	   	
	   	     String token = null;
	   	     String username = null;
	   	
	   	     // Validação do header
	   	     if (StringUtils.isBlank(authorizationHeader)) {
	   	         log.error("❌ [Filtro] Token ausente no cabeçalho Authorization");
	   	         throw new InvalidAudienceException("Token não existe");
	   	     }
	   	
	   	     // Extração do token (Bearer ...)
	   	     if (authorizationHeader.startsWith("Bearer ")) {
	   	         token = authorizationHeader.substring(7);
	   	         log.debug("🧩 [Filtro] Token extraído com sucesso");
	   	     } else {
	   	         log.error("❌ [Filtro] Formato inválido de token (esperado 'Bearer <token>')");
	   	         throw new InvalidAudienceException("Formato inválido de token");
	   	     }

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                
                log.debug("=== INICIANDO VALIDAÇÃO JWT ===");
                log.debug("Token recebido: {}", token);
                
                // DEBUG: Verifica se jwtTokenUtil está injetado
                log.debug("jwtTokenUtil é nulo? {}", jwtTokenUtil == null);
                
                if (jwtTokenUtil != null) {
                    // VALIDAÇÃO PASSO A PASSO
                    boolean isSignatureValid = jwtTokenUtil.validateToken(token);
                    log.debug("Assinatura válida: {}", isSignatureValid);
                    
                    if (isSignatureValid) {
                        boolean isExpired = jwtTokenUtil.isTokenExpired(token);
                        log.debug("Token expirado: {}", isExpired);
                        
                        if (!isExpired) {
                            String tokenType = jwtTokenUtil.extractTokenType(token);
                            log.debug("Tipo do token: {}", tokenType);
                            
                            if ("access".equals(tokenType)) {
                                username = jwtTokenUtil.extractUsername(token);
                                List<String> roles = jwtTokenUtil.extractRoles(token);
                                Long userId = jwtTokenUtil.extractUserId(token);
                                
                                log.debug("Autenticando usuário: {}, Roles: {}, UserId: {}", username, roles, userId);
                                
                                // Cria autenticação
                                UserDetails userDetails = new User(username, "", 
                                        roles.stream()
                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                            .collect(Collectors.toList()));

                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                log.debug("Autenticação configurada com sucesso");
                            } else {
                                log.debug("Token não é do tipo 'access'");
                            }
                        } else {
                            log.debug("Token expirado - autenticação não realizada");
                        }
                    } else {
                        log.debug("Assinatura inválida - autenticação não realizada");
                    }
                } else {
                    log.error("ERRO: jwtTokenUtil não foi injetado!");
                }
            } else {
                log.debug("Header Authorization não encontrado ou inválido");
            }
            
         
        
        filterChain.doFilter(request, response);
    }

    private boolean isWhiteList(final String requestURI) {
        log.warn("🧩 [Whitelist] Iniciando verificação para URI: {}", requestURI);

        String path = Optional.ofNullable((String) ContextProperties.get("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .map(ctx -> {
                    String result = StringUtils.substringAfter(requestURI, ctx);
                    log.warn("🧩 [Whitelist] Context-path detectado: '{}', path resultante: '{}'", ctx, result);
                    return result;
                })
                .orElseGet(() -> {
                    log.warn("🧩 [Whitelist] Nenhum context-path configurado. Usando requestURI original: '{}'", requestURI);
                    return requestURI;
                });

        String[] whiteList = Optional.ofNullable((String) ContextProperties.get("auth.security-default-white-list"))
                .map(w -> {
                    String[] result = w.split(",");
                    log.warn("🧩 [Whitelist] WhiteList padrão carregada: {}", Arrays.toString(result));
                    return result;
                })
                .orElseGet(() -> {
                    log.warn("🧩 [Whitelist] Nenhuma whiteList padrão configurada.");
                    return new String[]{};
                });

        String[] commonsWhiteList = Optional.ofNullable((String) ContextProperties.get("auth.security-commons-white-list"))
                .map(w -> {
                    String[] result = w.split(",");
                    log.warn("🧩 [Whitelist] Commons WhiteList carregada: {}", Arrays.toString(result));
                    return result;
                })
                .orElseGet(() -> {
                    log.warn("🧩 [Whitelist] Nenhuma commonsWhiteList configurada.");
                    return new String[]{};
                });

        // Também imprime o AUTH_SWAGGER_WHITELIST completo
        log.warn("🧩 [Whitelist] AUTH_SWAGGER_WHITELIST estático: {}", Arrays.toString(AUTH_SWAGGER_WHITELIST));

        boolean matched = Stream.of(
                        Arrays.stream(AUTH_SWAGGER_WHITELIST),
                        Arrays.stream(commonsWhiteList),
                        Arrays.stream(whiteList)
                )
                .flatMap(s -> s)
                .map(String::trim)
                .peek(pattern -> log.warn("🧩 [Whitelist] Comparando padrão '{}' com path '{}'", pattern, path))
                .anyMatch(pattern -> {
                    boolean result = MATCHER.match(pattern, path);
                    if (result) log.warn("✅ [Whitelist] Padrão correspondente encontrado: '{}'", pattern);
                    return result;
                });

        log.warn("🧩 [Whitelist] Resultado final para '{}': {}", path, matched);
        return matched;
    }


}
