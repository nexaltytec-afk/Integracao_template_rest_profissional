package br.com.nexalty.template_rest_profissional.rest;

import org.springframework.http.HttpMethod;

public class AuthenticationServiceNoOp implements IAuthenticationService {

    @Override
    public String authenticate(String username, String password, String targetURL, HttpMethod method) {
        return null; // ou vazio, pois não há autenticação
    }

    @Override
    public EAuthenticationType getAuthenticationType() {
        return EAuthenticationType.NONE;
    }
}
