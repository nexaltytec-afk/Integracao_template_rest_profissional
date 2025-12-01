package br.com.nexalty.template_rest_profissional.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity 
@Order(1)
@Slf4j
public class SecurityConfig {

	@Value("${auth.security-default-white-list:}")
    private String whiteList;

    @Value("${auth.security-commons-white-list:}")
    private String commonsWhiteList;
    
    private final JwtTokenUtil jwtTokenUtil;
    
    public SecurityConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        log.info("=== SECURITY CONFIG INICIALIZADO ===");
        log.info("JwtTokenUtil injetado: {}", jwtTokenUtil != null);
    }

     
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
             throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    protected static final String[] AUTH_SWAGGER_WHITELIST = {
            // -- swagger ui
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs.yaml",
            "/webjars/**",
            "/swagger-resources/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,  JwtTokenUtil jwtTokenUtil) throws Exception {
    	
    	 log.info("=== CONFIGURANDO SECURITY FILTER CHAIN ===");
         log.info("WhiteList: {}", whiteList);
         log.info("CommonsWhiteList: {}", commonsWhiteList);
    	    
         	http
        		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {

                    if(StringUtils.isNotBlank(commonsWhiteList))
                        auth.requestMatchers(Arrays
                                .stream(commonsWhiteList.split(","))
                                .map(String::trim)
                                .toArray(String[]::new)).permitAll();

                    if(StringUtils.isNotBlank(whiteList))
                            auth.requestMatchers(Arrays
                                    .stream(whiteList.split(","))
                                    .map(String::trim)
                                    .toArray(String[]::new)).permitAll();

                    auth.requestMatchers(AUTH_SWAGGER_WHITELIST).permitAll();

                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form.disable()) 
                .httpBasic(basic -> basic.disable()) 
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                        .authenticationEntryPoint(new JwtEntryPointHandler())
                );

        return http.build();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        log.info("=== CRIANDO JwtAuthenticationFilter ===");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenUtil);
        log.info("Filter criado: {}", filter != null);
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Content-Disposition"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
