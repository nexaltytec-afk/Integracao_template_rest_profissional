package br.com.nexalty.template_rest_profissional.oap;
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}