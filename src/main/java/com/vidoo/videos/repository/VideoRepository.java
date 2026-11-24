package com.vidoo.videos.repository;

import com.vidoo.videos.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {

    List<Video> findAllByOwnerId(UUID ownerId);

    List<Video> findAllByOwnerIdAndFolderId(
            UUID ownerId,
            UUID folderId
    );

    Optional<Video> findByIdAndOwnerId(
            UUID videoId,
            UUID ownerId
    );
}