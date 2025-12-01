package br.com.nexalty.template_rest_profissional.oap;

import lombok.Getter;

@Getter
public class ValidationExpectedException extends RuntimeException{
    private int erro;
    private Object dados;

    public ValidationExpectedException(int erro, String message){
        super(message);
        this.erro = erro;
    }

    public ValidationExpectedException(int erro,  String message, Object dados){
        super(message);
        this.erro = erro;
        this.dados = dados;
    }
}
