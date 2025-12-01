package br.com.nexalty.template_rest_profissional.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionUtils {
    
    public static Map<String, String> extractErrorDetails(String errorMessage) {
        Map<String, String> errorDetails = new HashMap<>();
        
        // Padrão para extrair o valor duplicado da mensagem de erro
        Pattern duplicateEntryPattern = Pattern.compile("Duplicate entry '([^']*)' for key");
        Matcher matcher = duplicateEntryPattern.matcher(errorMessage);
        
        // Verifica por violação de unicidade - analisando a estrutura da mensagem
        if (errorMessage.contains("Duplicate entry")) {
            String duplicateValue = null;
            if (matcher.find()) {
                duplicateValue = matcher.group(1);
            }
            
            // Analisa qual campo está causando a violação baseado no contexto
            if (errorMessage.contains("company_profile") && duplicateValue != null) {
                // Verifica o formato do valor duplicado para identificar o campo
                if (isCnpjFormat(duplicateValue)) {
                    errorDetails.put("cnpj", "CNPJ " + duplicateValue + " já está cadastrado");
                } else if (isEmailFormat(duplicateValue)) {
                    errorDetails.put("contactEmail", "Email " + duplicateValue + " já está cadastrado");
                } else if (isUserIdFormat(duplicateValue)) {
                    errorDetails.put("userId", "Já existe um perfil de empresa para este usuário");
                } else {
                    // Se não consegue identificar pelo formato, usa lógica contextual
                    if (errorMessage.toLowerCase().contains("cnpj")) {
                        errorDetails.put("cnpj", "CNPJ já cadastrado no sistema");
                    } else if (errorMessage.toLowerCase().contains("email")) {
                        errorDetails.put("contactEmail", "Email de contato já cadastrado");
                    } else if (errorMessage.toLowerCase().contains("user_id")) {
                        errorDetails.put("userId", "Já existe um perfil de empresa para este usuário");
                    } else {
                        errorDetails.put("general", "Dado único já cadastrado: " + duplicateValue);
                    }
                }
            }
        }
        
        // Verifica por violações de constraints NOT NULL
        else if (errorMessage.contains("cannot be null") || errorMessage.contains("Column") && errorMessage.contains("cannot be null")) {
            extractNotNullViolations(errorMessage, errorDetails);
        }
        
        // Verifica por violações de formato
        else if (errorMessage.contains("email") || errorMessage.contains("Email")) {
            errorDetails.put("contactEmail", "Email de contato deve ser válido");
        }
        
        // Verifica tamanho de campos
        else if (errorMessage.contains("data truncation") || errorMessage.contains("too long") || errorMessage.contains("Data too long")) {
            extractSizeViolations(errorMessage, errorDetails);
        }
        
        // Mensagem genérica se não conseguir identificar o campo específico
        else {
            errorDetails.put("general", "Erro de integridade de dados. Verifique os dados fornecidos.");
        }
        
        return errorDetails;
    }
    
    private static void extractNotNullViolations(String errorMessage, Map<String, String> errorDetails) {
        if (errorMessage.contains("user_id")) {
            errorDetails.put("userId", "ID do usuário é obrigatório");
        }
        if (errorMessage.contains("company_name")) {
            errorDetails.put("companyName", "Razão social é obrigatória");
        }
        if (errorMessage.contains("cnpj")) {
            errorDetails.put("cnpj", "CNPJ é obrigatório");
        }
        if (errorMessage.contains("contact_email")) {
            errorDetails.put("contactEmail", "Email de contato é obrigatório");
        }
        if (errorMessage.contains("address")) {
            errorDetails.put("address", "Endereço é obrigatório");
        }
        if (errorMessage.contains("city")) {
            errorDetails.put("city", "Cidade é obrigatória");
        }
        if (errorMessage.contains("state")) {
            errorDetails.put("state", "Estado é obrigatório");
        }
        if (errorMessage.contains("cep")) {
            errorDetails.put("cep", "CEP é obrigatório");
        }
        if (errorMessage.contains("bairro")) {
            errorDetails.put("bairro", "Bairro é obrigatório");
        }
        if (errorMessage.contains("country")) {
            errorDetails.put("country", "País é obrigatório");
        }
    }
    
    private static void extractSizeViolations(String errorMessage, Map<String, String> errorDetails) {
        if (errorMessage.contains("cnpj")) {
            errorDetails.put("cnpj", "CNPJ excede o tamanho máximo permitido");
        }
        if (errorMessage.contains("cpf")) {
            errorDetails.put("cpf", "CPF excede o tamanho máximo permitido");
        }
        if (errorMessage.contains("contact_email")) {
            errorDetails.put("contactEmail", "Email de contato excede o tamanho máximo permitido");
        }
        if (errorMessage.contains("company_name")) {
            errorDetails.put("companyName", "Razão social excede o tamanho máximo permitido");
        }
        if (errorMessage.contains("trade_name")) {
            errorDetails.put("tradeName", "Nome fantasia excede o tamanho máximo permitido");
        }
    }
    
    // Métodos auxiliares para identificar formatos
    private static boolean isCnpjFormat(String value) {
        return value != null && value.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }
    
    private static boolean isEmailFormat(String value) {
        return value != null && value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private static boolean isUserIdFormat(String value) {
        return value != null && value.matches("\\d+");
    }
    
    // Método principal para tratamento de exceções
    public static Map<String, String> handleDataIntegrityException(String errorMessage) {
        return extractErrorDetails(errorMessage);
    }
    
    // Método sobrecarregado para receber a exceção diretamente
    public static Map<String, String> handleDataIntegrityException(Exception ex) {
        return extractErrorDetails(ex.getMessage());
    }
}