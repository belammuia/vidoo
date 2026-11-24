
package com.vidoo.folders.service;

import com.vidoo.folders.entity.Folder;
import com.vidoo.folders.repository.FolderRepository;
import com.vidoo.users.entity.User;
import com.vidoo.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.vidoo.common.exception.ResourceNotFoundException;
import com.vidoo.common.exception.DuplicateResourceException;
import com.vidoo.common.exception.ResourceConflictException;

@Service
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderService(
            FolderRepository folderRepository,
            UserRepository userRepository
    ) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public Folder createFolder(
            UUID ownerId,
            UUID parentFolderId,
            String name
    ) {
        String validName = validateName(name);

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Folder parentFolder = null;

        if (parentFolderId != null) {
            parentFolder = findOwnedFolder(parentFolderId, ownerId);
        }

        if (folderNameExists(ownerId, parentFolderId, validName)) {
            throw new DuplicateResourceException(
                    "A folder with this name already exists"
            );
        }

        Folder folder = new Folder(owner, parentFolder, validName);

        return folderRepository.save(folder);
    }

    // LIST
    @Transactional(readOnly = true)
    public List<Folder> listFolders(
            UUID ownerId,
            UUID parentFolderId
    ) {
        if (parentFolderId == null) {
            return folderRepository
                    .findAllByOwnerIdAndParentFolderIsNull(ownerId);
        }

        findOwnedFolder(parentFolderId, ownerId);

        return folderRepository
                .findAllByOwnerIdAndParentFolderId(
                        ownerId,
                        parentFolderId
                );
    }

    // RENAME
    public Folder renameFolder(
            UUID ownerId,
            UUID folderId,
            String newName
    ) {
        String validName = validateName(newName);

        Folder folder = findOwnedFolder(folderId, ownerId);

        if (!folder.getName().equalsIgnoreCase(validName)
                && folderNameExists(
                ownerId,
                folder.getParentFolder() == null
                        ? null
                        : folder.getParentFolder().getId(),
                validName
        )) {
            throw new DuplicateResourceException(
                    "A folder with this name already exists"
            );
        }

        folder.rename(validName);

        return folderRepository.save(folder);
    }

    // DELETE
    public void deleteFolder(UUID ownerId, UUID folderId) {

        Folder folder = findOwnedFolder(folderId, ownerId);

        if (folderRepository.existsByOwnerIdAndParentFolderId(
                ownerId,
                folderId
        )) {
            throw new ResourceConflictException(
                    "Cannot delete a folder containing subfolders"
            );
        }

        folderRepository.delete(folder);
    }

    // HELPERS

    private Folder findOwnedFolder(UUID folderId, UUID ownerId) {
        return folderRepository
                .findByIdAndOwnerId(folderId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Folder not found"));
    }

    private boolean folderNameExists(
            UUID ownerId,
            UUID parentFolderId,
            String name
    ) {
        if (parentFolderId == null) {
            return folderRepository
                    .existsByOwnerIdAndParentFolderIsNullAndNameIgnoreCase(
                            ownerId, name
                    );
        }

        return folderRepository
                .existsByOwnerIdAndParentFolderIdAndNameIgnoreCase(
                        ownerId, parentFolderId, name
                );
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Folder name cannot be blank"
            );
        }

        String trimmedName = name.trim();

        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException(
                    "Folder name cannot exceed 100 characters"
            );
        }

        return trimmedName;
    }
}
