CREATE TABLE users (
                       id UUID PRIMARY KEY,

                       google_subject VARCHAR(255) NOT NULL,
                       email VARCHAR(320) NOT NULL,
                       full_name VARCHAR(150),
                       profile_picture_url VARCHAR(2048),

                       created_at TIMESTAMPTZ NOT NULL,
                       updated_at TIMESTAMPTZ NOT NULL,

                       CONSTRAINT uk_users_google_subject UNIQUE (google_subject),
                       CONSTRAINT uk_users_email UNIQUE (email)
);