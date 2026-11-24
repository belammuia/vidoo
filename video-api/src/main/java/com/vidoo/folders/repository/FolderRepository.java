package com.vidoo.folders.repository;

import com.vidoo.folders.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {

    // List root folders belonging to a user
    List<Folder> findAllByOwnerIdAndParentFolderIsNull(UUID ownerId);

    // List subfolders belonging to a user and parent folder
    List<Folder> findAllByOwnerIdAndParentFolderId(
            UUID ownerId,
            UUID parentFolderId
    );

    // Find a specific folder owned by a user
    Optional<Folder> findByIdAndOwnerId(UUID folderId, UUID ownerId);

    // Cheek whether a folder created by a user exists
    boolean existsByOwnerIdAndParentFolderId(
            UUID ownerId,
            UUID parentFolderId
    );

    // Check whether a folder with the same name exists in a parent
    boolean existsByOwnerIdAndParentFolderIdAndNameIgnoreCase(
            UUID ownerId,
            UUID parentFolderId,
            String name
    );

    // Check whether a root folder with the same name exists
    boolean existsByOwnerIdAndParentFolderIsNullAndNameIgnoreCase(
            UUID ownerId,
            String name
    );
}