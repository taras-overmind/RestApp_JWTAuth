package com.taras_overmind.model.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
