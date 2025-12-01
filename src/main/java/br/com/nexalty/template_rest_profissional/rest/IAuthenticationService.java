package br.com.nexalty.template_rest_profissional.rest;


import org.springframework.http.HttpMethod;

public interface IAuthenticationService {
    String authenticate(String username, String password, String targetURL, HttpMethod method);
    EAuthenticationType getAuthenticationType();
}
