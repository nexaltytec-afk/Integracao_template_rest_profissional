package br.com.nexalty.template_rest_profissional.oap;
public class TokenIssuedInFutureException extends TokenValidationException {
    public TokenIssuedInFutureException(String message) {
        super(message, 1005);
    }
}

