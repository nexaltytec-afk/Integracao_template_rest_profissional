package br.com.nexalty.template_rest_profissional.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private String path;
    private Integer status;
    private Integer errorCode; // Novo campo para código de erro específico

    public DefaultResponse() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public DefaultResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public DefaultResponse(boolean success, String message, T data, Integer status) {
        this(success, message, data);
        this.status = status;
    }

    public DefaultResponse(boolean success, String message, T data, Integer status, Integer errorCode) {
        this(success, message, data, status);
        this.errorCode = errorCode;
    }

    // Getters e Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getErrorCode() { return errorCode; }
    public void setErrorCode(Integer errorCode) { this.errorCode = errorCode; }

    // Métodos estáticos para sucesso
    public static <T> DefaultResponse<T> success(T data) {
        return new DefaultResponse<>(true, "Operation completed successfully", data);
    }

    public static <T> DefaultResponse<T> success(String message, T data) {
        return new DefaultResponse<>(true, message, data);
    }

    public static <T> DefaultResponse<T> success(String message, T data, Integer status) {
        return new DefaultResponse<>(true, message, data, status);
    }
    
    // Métodos estáticos para erro
    public static <T> DefaultResponse<T> error(String message) {
        return new DefaultResponse<>(false, message, null);
    }

    public static <T> DefaultResponse<T> error(String message, T data) {
        return new DefaultResponse<>(false, message, data);
    }

    public static <T> DefaultResponse<T> error(String message, T data, Integer status) {
        return new DefaultResponse<>(false, message, data, status);
    }

    public static <T> DefaultResponse<T> error(String message, T data, Integer status, Integer errorCode) {
        return new DefaultResponse<>(false, message, data, status, errorCode);
    }

    // Método específico para erros de validação
    public static <T> DefaultResponse<T> validationError(String message, T validationErrors) {
        return new DefaultResponse<>(false, message, validationErrors, 400, -1001400);
    }

    @Override
    public String toString() {
        return "DefaultResponse [success=" + success + ", message=" + message + ", data=" + data + ", timestamp="
                + timestamp + ", path=" + path + ", status=" + status + ", errorCode=" + errorCode + "]";
    }
}