package br.com.nexalty.template_rest_profissional.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.nexalty.template_rest_profissional.types.Registro;

public class JSONUtils {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static String toJSON(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertTo(Object o, Class<T> clazz) {
        try {
            if (Registro.class.isAssignableFrom(clazz))
                return (T) convertTo(o, new TypeReference<Registro>() {});
            return OBJECT_MAPPER.convertValue(o, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertTo(Object o, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.convertValue(o, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
