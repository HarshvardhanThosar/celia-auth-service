package com.celia.auth_service.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
