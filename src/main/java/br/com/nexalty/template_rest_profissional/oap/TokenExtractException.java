package br.com.nexalty.template_rest_profissional.oap;

import lombok.Getter;

@Getter
public class TokenExtractException extends RuntimeException{
    private int erro;
    private Object dados;

    public TokenExtractException(int erro, String message){
        super(message);
        this.erro = erro;
    }

    public TokenExtractException(int erro, Object dados, String message){
        super(message);
        this.erro = erro;
        this.dados = dados;
    }
}
