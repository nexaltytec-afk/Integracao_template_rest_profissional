package br.com.nexalty.template_rest_profissional.dto;
 
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmacaoRequest {
    private String destinatario;
    private String nome;
    private String link;
    @Column(nullable = true)
    private String token;
    
   
}