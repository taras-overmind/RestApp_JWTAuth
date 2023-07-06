package com.taras_overmind.model.payload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String login;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String login, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.login = login;
        this.roles = roles;
    }
}
