package com.celia.auth_service.dtos.responses;

public record CommunityUserResponseDTO(
        String username,
        String email,
        String first_name,
        String last_name
) {
}
