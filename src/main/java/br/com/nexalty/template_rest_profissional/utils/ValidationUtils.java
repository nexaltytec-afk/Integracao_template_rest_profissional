package br.com.nexalty.template_rest_profissional.utils;
 
import java.util.function.Supplier;

import br.com.nexalty.template_rest_profissional.oap.ValidationExpectedException;
import br.com.nexalty.template_rest_profissional.types.Registro;
import br.com.nexalty.template_rest_profissional.utils.constants.CommonConst;

public class ValidationUtils {

    public static void isValidOrThrow(Registro r) {
        isValidOrThrow(r, () -> new ValidationExpectedException(r.getAsInt(CommonConst.RETORNO) , r.getAsString(CommonConst.MENSAGEM)));
    }

    public static void isValidOrThrow(Registro r, Supplier<RuntimeException> exp) {
        if(r.getAsInt(CommonConst.RETORNO) < 0)
            throw exp.get();
    }
}
