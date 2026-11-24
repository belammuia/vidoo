
package com.vidoo.users.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class MeController {

    @GetMapping("/me")
    public Map<String, UUID> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return Map.of(
                "userId",
                UUID.fromString(jwt.getSubject())
        );
    }
}
