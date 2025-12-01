package br.com.nexalty.template_rest_profissional.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioToken {
    private Long id; // Mude para Long se necessário
    private String login;
    private List<String> roles;
    
    // Se você precisa manter compatibilidade, pode usar Object ou criar métodos de conversão
    public void setId(Object id) {
        if (id instanceof Long) {
            this.id = (Long) id;
        } else if (id instanceof Integer) {
            this.id = ((Integer) id).longValue();
        } else if (id != null) {
            this.id = Long.valueOf(id.toString());
        }
    }
    
    public Long getIdAsLong() {
        return id;
    }
    
    public Integer getIdAsInteger() {
        return id != null ? id.intValue() : null;
    }
}