package br.com.nexalty.template_rest_profissional.rest;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class ResponseService {
     
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ResponseService(ObjectMapper objectMapper) { 
        this.objectMapper = objectMapper;
    }
    
    public <T> DefaultResponse handleSuccess(ResponseEntity<?> response, Class<T> responseType) {
        try {
            if (response.getBody() != null) {
                // CORRETO: usa o método estático que retorna nova instância
                return DefaultResponse.success(response.getBody());
            } else {
                // CORRETO: usa o método estático
                return DefaultResponse.success("Request successful but no content returned", null);
            }
            
        } catch (Exception e) {
            log.error("Error processing successful response", e);
            // CORRETO: usa o método estático
            return DefaultResponse.error("Error processing response: " + e.getMessage(), null, 500);
        }
    }
    
    public <T> DefaultResponse handleFailure(Exception exception, Class<T> responseType) {
        try {
            if (exception instanceof HttpClientErrorException) {
                HttpClientErrorException httpException = (HttpClientErrorException) exception;
                int statusCode = httpException.getStatusCode().value();
                String responseBody = httpException.getResponseBodyAsString();
                
                log.info("Response: " + responseBody);
                
                // CORRETO: usa o método estático
                return DefaultResponse.error(
                    parseErrorMessage(responseBody, statusCode), 
                    responseBody, 
                    statusCode
                );
                
            } else {
                // CORRETO: usa o método estático
                return DefaultResponse.error(
                    "Request failed: " + exception.getMessage(), 
                    null, 
                    500
                );
            }
            
        } catch (Exception e) {
            log.error("Error processing failure response", e);
            // CORRETO: usa o método estático
            return DefaultResponse.error(
                "Error processing failure: " + e.getMessage(), 
                null, 
                500
            );
        }
    }
    
    private String parseErrorMessage(String responseBody, int statusCode) {
        try {
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                // Tenta parsear como JSON para extrair mensagem de erro
                try {
                    ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                    if (errorResponse.getMessage() != null) {
                        return errorResponse.getMessage();
                    }
                    if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
                        return errorResponse.getErrors().get(0).getMessage();
                    }
                } catch (Exception e) {
                    // Se não for JSON válido, retorna o corpo cru
                }
                
                return responseBody;
            }
        } catch (Exception e) {
            log.error("Error parsing error message", e);
        }
        
        return "HTTP Error: " + statusCode;
    }
    
    // Classe para mapeamento de erros
    public static class ErrorResponse {
        private String message;
        private List<ErrorDetail> errors;
        
        // Getters e Setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<ErrorDetail> getErrors() { return errors; }
        public void setErrors(List<ErrorDetail> errors) { this.errors = errors; }
    }
    
    public static class ErrorDetail {
        private String code;
        private String message;
        
        // Getters e Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}