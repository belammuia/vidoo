package com.vidoo.users.entity;

import com.vidoo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_google_subject",
                        columnNames = "google_subject"
                ),
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        }
)
public class User extends BaseEntity {

    @Column(name = "google_subject", nullable = false, length = 255)
    private String googleSubject;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "profile_picture_url", length = 2048)
    private String profilePictureUrl;

    protected User() {
        // Required by JPA
    }

    public User(
            String googleSubject,
            String email,
            String fullName,
            String profilePictureUrl
    ) {
        this.googleSubject = googleSubject;
        this.email = email;
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getGoogleSubject() {
        return googleSubject;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void updateProfile(String fullName, String profilePictureUrl) {
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }
}