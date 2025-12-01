package br.com.nexalty.template_rest_profissional.dto;
  
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import br.com.nexalty.template_rest_profissional.enums.EAuthProvider;
import br.com.nexalty.template_rest_profissional.enums.EUserInstancia;
import br.com.nexalty.template_rest_profissional.enums.EUserTipo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class User {
      
    private Long id; 
    private String email; 
    private String password; 
    private String firstName; 
    private String lastName; 
    private boolean verify; 
    private EUserTipo tipo; 
    private EUserInstancia instancia; 
    private EAuthProvider authProvider; 
    private String providerId;  
    private String pictureUrl;  
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

    
}
