package br.com.nexalty.template_rest_profissional.oap;
public class InvalidAudienceException extends TokenValidationException {
    public InvalidAudienceException(String message) {
        super(message, 1002);
    }
}

