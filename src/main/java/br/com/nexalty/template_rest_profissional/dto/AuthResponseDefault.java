package br.com.nexalty.template_rest_profissional.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDefault {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private User user;
    private List<String> roles;
    private Long userId;
    
    public AuthResponseDefault(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.user = user;
    }
}
