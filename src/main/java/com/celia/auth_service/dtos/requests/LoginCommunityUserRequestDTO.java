package com.celia.auth_service.dtos.requests;

public record LoginCommunityUserRequestDTO(
        // String email,
        String username,
        String password
) {
}