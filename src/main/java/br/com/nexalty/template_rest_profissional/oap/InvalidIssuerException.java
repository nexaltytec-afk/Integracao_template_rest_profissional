package br.com.nexalty.template_rest_profissional.oap;
public class InvalidIssuerException extends TokenValidationException {
    public InvalidIssuerException(String message) {
        super(message, 1003);
    }
}

