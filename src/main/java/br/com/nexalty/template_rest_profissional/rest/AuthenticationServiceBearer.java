package br.com.nexalty.template_rest_profissional.rest;

import org.springframework.http.HttpMethod;

public class AuthenticationServiceBearer implements IAuthenticationService {

    @Override
    public String authenticate(String username, String password, String targetURL, HttpMethod method) { 
        String token = password;  
        return "Bearer " + token;
    }

    @Override
    public EAuthenticationType getAuthenticationType() {
        return EAuthenticationType.BEARER;
    }
}