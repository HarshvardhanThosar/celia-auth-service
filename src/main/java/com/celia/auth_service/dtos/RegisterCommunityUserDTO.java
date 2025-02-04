package com.celia.auth_service.dtos;

public record RegisterCommunityUserDTO(
        String username,
        String password,
        String email,
        String first_name,
        String last_name) {
}