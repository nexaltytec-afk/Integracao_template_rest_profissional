package br.com.nexalty.template_rest_profissional.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Slf4j 
@Component
public class JwtTokenUtil  {

	@Value("${jwt.secret:FDrbzTLGWtj4kQIkiCI01gaHD+R7aU2PmHezknGDYqjDHn20eqZKnYxKm+oCiOdKJK+DGE6fghlRpdhuV602/g==}")
    private String secret;

    @Value("${jwt.access-token.expiration:900000}") // 15 minutos
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7 dias
    private Long refreshTokenExpiration;

    // Gera Access Token
    public String generateAccessToken(String username, Long userId, List<String> roles) {
        return generateToken(username, userId, roles, "access", accessTokenExpiration);
    }

    // Gera Refresh Token
    public String generateRefreshToken(String username, Long userId, List<String> roles) {
        return generateToken(username, userId, roles, "refresh", refreshTokenExpiration);
    }

    private String generateToken(String username, Long userId, List<String> roles, String type, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("userId", userId);
        claims.put("type", type);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Validação completa do token
    public boolean validateToken(String token) {
        System.err.println("=== DENTRO DO VALIDATE TOKEN ===");
        //System.err.println("Token recebido para validação: " + token);
        
        try {
            System.err.println("Secret length: " + secret.length());
            
            // Tenta fazer o parse
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            
            System.err.println("✓ Token VÁLIDO - Assinatura OK");
            System.err.println("Subject: " + claimsJws.getBody().getSubject());
            return true;
            
        } catch (ExpiredJwtException e) {
            System.err.println("✗ Token EXPIRADO: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("✗ ERRO na validação: " + e.getClass().getSimpleName());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Extrai username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. Extrai Roles
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    // Extrai userId
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    // Verifica tipo do token
    public String extractTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("type", String.class);
    }

    // Verifica expiração
    public boolean isTokenExpired(String token) {
    	 System.err.println("=== DENTRO DO VALIDATE TOKEN ===");
         //System.err.println("Token recebido para validação: " + token);
         
         try {
        boolean isExpired = extractExpiration(token).before(new Date());
        if(isExpired) {
            System.err.println("Token EXPIRADO");
        } else {
            System.err.println("Token VÁLIDO (não expirado)");
        }
        return isExpired;
         } catch (Exception e) {
             System.err.println("✗ Token EXPIRADO: " + e.getMessage());
             return false;
         } 
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
