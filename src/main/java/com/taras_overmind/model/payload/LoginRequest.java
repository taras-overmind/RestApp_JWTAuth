package com.taras_overmind.model.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
