package br.com.nexalty.template_rest_profissional.rest;

import org.springframework.http.HttpMethod;
import java.util.Base64;

public class AuthenticationServiceBasic implements IAuthenticationService {

    @Override
    public String authenticate(String username, String password, String targetURL, HttpMethod method) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Override
    public EAuthenticationType getAuthenticationType() {
        return EAuthenticationType.BASIC;
    }
}
