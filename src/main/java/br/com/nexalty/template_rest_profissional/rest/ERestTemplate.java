package br.com.nexalty.template_rest_profissional.rest;

public enum ERestTemplate {

    SHORT(2, 3),  // 2 segundos connect, 3 segundos read
    LONG(30, 1800); // 30 segundos connect, 30 minutos read (1800 segundos)

    private final int connectTimeoutSeconds;
    private final int readTimeoutSeconds;

    ERestTemplate(int connectTimeoutSeconds, int readTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
        this.readTimeoutSeconds = readTimeoutSeconds;
    }

    public int getConnectTimeout() {
        return connectTimeoutSeconds * 1000;
    }

    public int getReadTimeout() {
        return readTimeoutSeconds * 1000;
    }
}
