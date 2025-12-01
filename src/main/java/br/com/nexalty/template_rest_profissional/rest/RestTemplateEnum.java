package br.com.nexalty.template_rest_profissional.rest;

public enum RestTemplateEnum {
    SHORT(999999999, 999999999),
    LONG(999999999, 999999999);

    private final int connectTimeoutSeconds;
    private final int readTimeoutMinutes;

    RestTemplateEnum(int connectTimeoutSeconds, int readTimeoutMinutes) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
        this.readTimeoutMinutes = readTimeoutMinutes;
    }

    public int getConnectTimeoutSeconds() {
        return connectTimeoutSeconds * 10000;
    }

    public int getReadTimeoutMinutes() {
        return readTimeoutMinutes * 10000;
    }
}
