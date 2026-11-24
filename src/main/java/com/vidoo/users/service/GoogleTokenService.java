package com.vidoo.users.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenService(
            @Value("${app.auth.google.client-id}") String clientId
    ) {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken verify(String idToken)
            throws GeneralSecurityException, IOException {

        try {
            GoogleIdToken token = verifier.verify(idToken);

            if (token == null) {
                throw new IllegalArgumentException("Invalid Google ID token");
            }

            return token;

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid Google ID token");
        }
    }
}