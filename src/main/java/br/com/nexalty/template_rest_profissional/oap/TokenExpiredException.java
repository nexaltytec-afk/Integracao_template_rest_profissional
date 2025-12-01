package br.com.nexalty.template_rest_profissional.oap;
public class TokenExpiredException extends TokenValidationException {
    public TokenExpiredException(String message) {
        super(message, 1004);
    }
}

