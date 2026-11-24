package com.vidoo.folders.dto;

import com.vidoo.folders.entity.Folder;

import java.time.Instant;
import java.util.UUID;

public record FolderResponse(
        UUID id,
        String name,
        UUID parentFolderId,
        Instant createdAt,
        Instant updatedAt
) {
    public static FolderResponse from(Folder folder) {
        return new FolderResponse(
                folder.getId(),
                folder.getName(),
                folder.getParentFolder() == null
                        ? null
                        : folder.getParentFolder().getId(),
                folder.getCreatedAt(),
                folder.getUpdatedAt()
        );
    }
}
