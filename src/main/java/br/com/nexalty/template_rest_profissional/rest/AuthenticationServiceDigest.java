package br.com.nexalty.template_rest_profissional.rest;


import org.springframework.http.HttpMethod;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class AuthenticationServiceDigest implements IAuthenticationService {

    @Override
    public String authenticate(String username, String password, String targetURL, HttpMethod method) {

        // O cabeçalho WWW-Authenticate é recebido do servidor
        String authHeader = getWwwAuthenticateHeader(targetURL);

        String realm = extractValue(authHeader, "realm");
        String nonce = extractValue(authHeader, "nonce");
        String opaque = extractValue(authHeader, "opaque");
        String nc = "00000001";
        String qop = "auth";

        // Hash MD5 helper
        String ha1 = md5(username + ":" + realm + ":" + password);
        String ha2 = md5(method + ":" + targetURL);
        String response = md5(ha1 + ":" + nonce + ":" + nc + ":" + nonce + ":" + qop + ":" + ha2);

        return "Digest username=\"" + username + "\", realm=\"" + realm + "\", nonce=\"" + nonce + "\", uri=\"" + targetURL +
                "\", qop=" + qop + ", nc=" + nc + ", cnonce=\"" + nonce + "\", response=\"" + response + "\", opaque=\"\"";

    }

    @Override
    public EAuthenticationType getAuthenticationType() {
        return EAuthenticationType.DIGEST;
    }

    private static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractValue(String header, String key) {
        int startIndex = header.indexOf(key + "=\"");
        if (startIndex == -1) {
            return null;
        }
        startIndex += key.length() + 2;
        int endIndex = header.indexOf("\"", startIndex);
        return header.substring(startIndex, endIndex);
    }

    /**
     * Obtém o cabeçalho WWW-Authenticate do servidor.
     *
     * @param targetURL A URL do recurso que requer autenticação.
     * @return O cabeçalho WWW-Authenticate.
     */
    private String getWwwAuthenticateHeader(String targetURL) {
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Language", "en-US");

            // Faz uma requisição inicial para obter o cabeçalho WWW-Authenticate
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                String authHeader = connection.getHeaderField("WWW-Authenticate");
                if (authHeader != null && authHeader.startsWith("Digest")) {
                    return authHeader;
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter o cabeçalho WWW-Authenticate: " + e.getMessage(), e);
        }
        throw new RuntimeException("Servidor não retornou um cabeçalho WWW-Authenticate válido.");
    }

}
