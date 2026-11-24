package com.vidoo.videos.dto;

import com.vidoo.videos.entity.Video;

import java.time.Instant;
import java.util.UUID;

public record VideoResponse(
        UUID id,
        String filename,
        String contentType,
        Long sizeBytes,
        String status,
        UUID folderId,
        Instant createdAt
) {
    public static VideoResponse from(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getOriginalFilename(),
                video.getContentType(),
                video.getSizeBytes(),
                video.getStatus().name(),
                video.getFolder() != null ? video.getFolder().getId() : null,
                video.getCreatedAt()
        );
    }
}