package br.com.nexalty.template_rest_profissional.oap;
public class EmailNotVerifiedException extends TokenValidationException {
    public EmailNotVerifiedException(String message) {
        super(message, 1006);
    }
}