package com.vidoo.auth.dto;

import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {

    public record UserResponse(
            UUID id,
            String email,
            String fullName,
            String profilePictureUrl
    ) {
    }
}