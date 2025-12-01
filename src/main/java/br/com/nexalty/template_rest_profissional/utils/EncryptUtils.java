package br.com.nexalty.template_rest_profissional.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
public class EncryptUtils {

    public static Optional<String> encriptografar (String texto)  {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(texto.getBytes());
            return Optional.ofNullable(Base64Helper.encode(digest.digest()));
        } catch (NoSuchAlgorithmException ns) {
            log.error("N\u00E3o foi possivel encriptografar o texto: {}", texto, ns);
            return Optional.empty();
        }
    }
}
