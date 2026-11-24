package com.vidoo;

import com.vidoo.users.entity.User;
import com.vidoo.users.repository.UserRepository;
import com.vidoo.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private String googleSubject;
    private String email;
    private String fullName;
    private String profilePictureUrl;

    @BeforeEach
    void setUp() {
        googleSubject = "google-123";
        email = "belam@example.com";
        fullName = "Belam Muia";
        profilePictureUrl = "https://example.com/profile.jpg";
    }

    @Test
    void shouldCreateUserWhenUserDoesNotExist() {

        when(userRepository.findByGoogleSubject(googleSubject))
                .thenReturn(Optional.empty());

        User savedUser = new User(
                googleSubject,
                email,
                fullName,
                profilePictureUrl
        );

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.findOrCreateUser(
                googleSubject,
                email,
                fullName,
                profilePictureUrl
        );

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateExistingUserInsteadOfCreatingDuplicate() {

        User existingUser = new User(
                googleSubject,
                email,
                "Old Name",
                "https://example.com/old.jpg"
        );

        when(userRepository.findByGoogleSubject(googleSubject))
                .thenReturn(Optional.of(existingUser));

        User result = userService.findOrCreateUser(
                googleSubject,
                email,
                fullName,
                profilePictureUrl
        );

        assertEquals(fullName, result.getFullName());
        assertEquals(profilePictureUrl, result.getProfilePictureUrl());

        verify(userRepository, never()).save(any(User.class));
    }
}