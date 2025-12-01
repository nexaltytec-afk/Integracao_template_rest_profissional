package br.com.nexalty.template_rest_profissional.rest;

/**
 * Tipos de autenticação suportados para requisições HTTP.
 */
public enum EAuthenticationType {
    BASIC,
    DIGEST,
    BEARER,
    API_KEY,
    NTLM,
    OAUTH2,
    NONE
}
