package com.celia.auth_service.dtos;

public record CommunityUserDTO(
        String username,
        String email,
        String first_name,
        String last_name
) {
}
