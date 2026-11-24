
package com.vidoo.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.vidoo.auth.dto.AuthResponse;
import com.vidoo.users.entity.User;
import com.vidoo.users.service.GoogleTokenService;
import com.vidoo.users.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class AuthService {

    private final GoogleTokenService googleTokenService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(
            GoogleTokenService googleTokenService,
            UserService userService,
            JwtService jwtService
    ) {
        this.googleTokenService = googleTokenService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse authenticateWithGoogle(String idToken)
            throws GeneralSecurityException, IOException {

        // 1. Verify Google's ID token
        GoogleIdToken googleIdToken =
                googleTokenService.verify(idToken);

        GoogleIdToken.Payload payload =
                googleIdToken.getPayload();

        // 2. Ensure Google's email is verified
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Google account email is not verified"
            );
        }

        // 3. Extract verified Google account details
        String googleSubject = payload.getSubject();
        String email = payload.getEmail();
        String fullName = (String) payload.get("name");
        String profilePictureUrl = (String) payload.get("picture");

        // 4. Find or create the Vidoo user
        User user = userService.findOrCreateUser(
                googleSubject,
                email,
                fullName,
                profilePictureUrl
        );

        // 5. Generate Vidoo's own access token
        String accessToken =
                jwtService.generateAccessToken(user.getId());

        // 6. Return authentication response
        return new AuthResponse(
                accessToken,
                "Bearer",
                new AuthResponse.UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getProfilePictureUrl()
                )
        );
    }
}
