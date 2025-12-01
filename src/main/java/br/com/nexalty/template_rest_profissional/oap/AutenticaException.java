package br.com.nexalty.template_rest_profissional.oap;

import lombok.Getter;

@Getter
public class AutenticaException extends RuntimeException{
    private int erro;
    private Object dados;

    public AutenticaException(int erro, String message){
        super(message);
        this.erro = erro;
    }

    public AutenticaException(int erro, Object dados, String message){
        super(message);
        this.erro = erro;
        this.dados = dados;
    }
}
