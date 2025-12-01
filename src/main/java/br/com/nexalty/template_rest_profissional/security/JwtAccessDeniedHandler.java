package br.com.nexalty.template_rest_profissional.security;
 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.utils.JSONUtils;

import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
 
        response.getOutputStream().print(JSONUtils.toJSON(DefaultResponse.error("Falha na autenticação! Token ausente, inválido ou expirado.")));
    }
}
