package br.com.nexalty.template_rest_profissional.oap;
// Exceções personalizadas
public class EmailNaoEncontradoException extends RuntimeException {
    public EmailNaoEncontradoException(String message) {
        super(message);
    }
}

