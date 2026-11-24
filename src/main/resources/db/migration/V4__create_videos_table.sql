CREATE TABLE videos (
                        id UUID PRIMARY KEY,
                        owner_id UUID NOT NULL,
                        folder_id UUID,
                        original_filename VARCHAR(255) NOT NULL,
                        storage_filename VARCHAR(255) NOT NULL UNIQUE,
                        content_type VARCHAR(100) NOT NULL,
                        size_bytes BIGINT NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMPTZ NOT NULL,
                        updated_at TIMESTAMPTZ NOT NULL,

                        CONSTRAINT fk_videos_owner
                            FOREIGN KEY (owner_id)
                                REFERENCES users(id)
                                ON DELETE CASCADE,

                        CONSTRAINT fk_videos_folder
                            FOREIGN KEY (folder_id)
                                REFERENCES folders(id)
                                ON DELETE SET NULL,

                        CONSTRAINT chk_video_size
                            CHECK (size_bytes > 0),

                        CONSTRAINT chk_video_status
                            CHECK (status IN ('UPLOADING', 'UPLOADED', 'FAILED'))
);

CREATE INDEX idx_videos_owner
    ON videos(owner_id);

CREATE INDEX idx_videos_folder
    ON videos(folder_id);