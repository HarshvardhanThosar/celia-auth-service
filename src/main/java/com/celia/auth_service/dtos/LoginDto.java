package com.celia.auth_service.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}
