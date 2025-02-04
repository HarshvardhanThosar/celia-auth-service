package com.celia.auth_service.dtos.requests;

public record RegisterCommunityUserRequestDTO(
        String username,
        String password,
        String email,
        String first_name,
        String last_name) {
}