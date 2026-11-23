package com.vidoo.users.service;

import com.vidoo.users.entity.User;
import com.vidoo.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(
            String googleSubject,
            String email,
            String fullName,
            String profilePictureUrl
    ) {
        return userRepository.findByGoogleSubject(googleSubject)
                .map(user -> {
                    user.updateProfile(fullName, profilePictureUrl);
                    return user;
                })
                .orElseGet(() -> userRepository.save(
                        new User(
                                googleSubject,
                                email,
                                fullName,
                                profilePictureUrl
                        )
                ));
    }
}