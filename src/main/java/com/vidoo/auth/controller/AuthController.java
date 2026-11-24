
package com.vidoo.auth.controller;

import com.vidoo.auth.dto.AuthResponse;
import com.vidoo.auth.dto.GoogleLoginRequest;
import com.vidoo.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request
    ) throws Exception {

        return ResponseEntity.ok(
                authService.authenticateWithGoogle(request.idToken())
        );
    }
}
