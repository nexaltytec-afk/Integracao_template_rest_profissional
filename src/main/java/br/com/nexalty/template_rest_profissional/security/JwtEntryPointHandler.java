package br.com.nexalty.template_rest_profissional.security;
 
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.utils.JSONUtils;

import java.io.IOException;

@Slf4j
public class JwtEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json"); 
        response.getOutputStream().print(JSONUtils.toJSON( DefaultResponse.error("Falha na autenticação! Token ausente, inválido ou expirado.")));
    }
}
