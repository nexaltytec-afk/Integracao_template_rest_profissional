package br.com.nexalty.template_rest_profissional.enums;

/**
 * ALTER TABLE jc_scuritydb.user 
MODIFY COLUMN tipo ENUM('CLIENTE','ENTREGADOR','VENDEDOR','CONSUMIDOR','LOJISTA','DISTRIBUIDOR','ADMIN') NOT NULL;
 */
public enum EUserTipo {
	VENDEDOR,
	ENTREGADOR,
	CLIENTE, 
	CONSUMIDOR,
    LOJISTA,
    DISTRIBUIDOR,
    REPRESENTANTE,
    ADMIN;
	
	public static EUserTipo fromString(String value) {
        if (value == null) {
            return CONSUMIDOR;
        }
        
        // Mapeia CLIENTE (antigo) para CONSUMIDOR (novo)
        if ("CLIENTE".equalsIgnoreCase(value)) {
            return CONSUMIDOR;
        }
        
        try {
            return EUserTipo.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CONSUMIDOR; // valor padrão
        }
    }
}


