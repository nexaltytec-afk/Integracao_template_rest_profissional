package br.com.nexalty.template_rest_profissional.oap;


public class TokenValidationException extends RuntimeException {
    private final Integer erro;
    
    public TokenValidationException(String message) {
        super(message);
        this.erro = 1001; // Código genérico de validação de token
    }
    
    public TokenValidationException(String message, Integer erro) {
        super(message);
        this.erro = erro;
    }
    
    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
        this.erro = 1001;
    }
    
    public Integer getErro() {
        return erro;
    }
}

