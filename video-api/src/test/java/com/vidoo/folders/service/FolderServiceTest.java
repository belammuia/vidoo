
package com.vidoo.folders.service;

import com.vidoo.common.exception.DuplicateResourceException;
import com.vidoo.common.exception.ResourceConflictException;
import com.vidoo.common.exception.ResourceNotFoundException;
import com.vidoo.folders.entity.Folder;
import com.vidoo.folders.repository.FolderRepository;
import com.vidoo.users.entity.User;
import com.vidoo.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FolderService folderService;

    private UUID ownerId;
    private UUID folderId;
    private UUID parentFolderId;

    private User owner;
    private Folder folder;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        folderId = UUID.randomUUID();
        parentFolderId = UUID.randomUUID();

        owner = new User(
                "google-subject",
                "test@vidoo.com",
                "Test User",
                null
        );

        folder = new Folder(owner, null, "Projects");

        ReflectionTestUtils.setField(folder, "id", folderId);
    }

    @Test
    void shouldCreateRootFolder() {
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderRepository
                .existsByOwnerIdAndParentFolderIsNullAndNameIgnoreCase(
                        ownerId, "Projects"))
                .thenReturn(false);

        when(folderRepository.save(any(Folder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Folder result = folderService.createFolder(
                ownerId, null, "Projects"
        );

        assertNotNull(result);
        assertEquals("Projects", result.getName());
        assertEquals(owner, result.getOwner());
        assertNull(result.getParentFolder());

        verify(folderRepository).save(any(Folder.class));
    }

    @Test
    void shouldCreateNestedFolder() {
        Folder parent = new Folder(owner, null, "Work");

        ReflectionTestUtils.setField(
                parent, "id", parentFolderId
        );

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderRepository.findByIdAndOwnerId(
                parentFolderId, ownerId))
                .thenReturn(Optional.of(parent));

        when(folderRepository
                .existsByOwnerIdAndParentFolderIdAndNameIgnoreCase(
                        ownerId, parentFolderId, "Projects"))
                .thenReturn(false);

        when(folderRepository.save(any(Folder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Folder result = folderService.createFolder(
                ownerId, parentFolderId, "Projects"
        );

        assertEquals("Projects", result.getName());
        assertEquals(parent, result.getParentFolder());
        assertEquals(owner, result.getOwner());

        verify(folderRepository).save(any(Folder.class));
    }

    @Test
    void shouldRejectDuplicateRootFolderName() {
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderRepository
                .existsByOwnerIdAndParentFolderIsNullAndNameIgnoreCase(
                        ownerId, "Projects"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> folderService.createFolder(
                        ownerId, null, "Projects"
                )
        );

        verify(folderRepository, never()).save(any(Folder.class));
    }

    @Test
    void shouldPreventAccessToAnotherUsersFolder() {
        when(folderRepository.findByIdAndOwnerId(
                folderId, ownerId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> folderService.renameFolder(
                        ownerId, folderId, "New Name"
                )
        );

        verify(folderRepository, never()).save(any(Folder.class));
    }

    @Test
    void shouldRenameFolder() {
        when(folderRepository.findByIdAndOwnerId(
                folderId, ownerId))
                .thenReturn(Optional.of(folder));

        when(folderRepository
                .existsByOwnerIdAndParentFolderIsNullAndNameIgnoreCase(
                        ownerId, "Updated Projects"))
                .thenReturn(false);

        when(folderRepository.save(any(Folder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Folder result = folderService.renameFolder(
                ownerId, folderId, "Updated Projects"
        );

        assertEquals("Updated Projects", result.getName());
    }

    @Test
    void shouldPreventDeletingFolderWithSubfolders() {
        when(folderRepository.findByIdAndOwnerId(
                folderId, ownerId))
                .thenReturn(Optional.of(folder));

        when(folderRepository.existsByOwnerIdAndParentFolderId(
                ownerId, folderId))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> folderService.deleteFolder(ownerId, folderId)
        );

        verify(folderRepository, never()).delete(any(Folder.class));
    }

    @Test
    void shouldDeleteEmptyFolder() {
        when(folderRepository.findByIdAndOwnerId(
                folderId, ownerId))
                .thenReturn(Optional.of(folder));

        when(folderRepository.existsByOwnerIdAndParentFolderId(
                ownerId, folderId))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> folderService.deleteFolder(ownerId, folderId)
        );

        verify(folderRepository).delete(folder);
    }
}
