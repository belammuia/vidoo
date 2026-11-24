
package com.vidoo.folders.controller;

import com.vidoo.folders.dto.CreateFolderRequest;
import com.vidoo.folders.dto.FolderResponse;
import com.vidoo.folders.dto.RenameFolderRequest;
import com.vidoo.folders.entity.Folder;
import com.vidoo.folders.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(
            @Valid @RequestBody CreateFolderRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        Folder folder = folderService.createFolder(
                ownerId,
                request.parentFolderId(),
                request.name()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(FolderResponse.from(folder));
    }

    // LIST
    @GetMapping
    public ResponseEntity<List<FolderResponse>> listFolders(
            @RequestParam(required = false) UUID parentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        List<FolderResponse> folders = folderService
                .listFolders(ownerId, parentId)
                .stream()
                .map(FolderResponse::from)
                .toList();

        return ResponseEntity.ok(folders);
    }

    // RENAME
    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponse> renameFolder(
            @PathVariable UUID folderId,
            @Valid @RequestBody RenameFolderRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        Folder folder = folderService.renameFolder(
                ownerId,
                folderId,
                request.name()
        );

        return ResponseEntity.ok(FolderResponse.from(folder));
    }

    // DELETE
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable UUID folderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        folderService.deleteFolder(ownerId, folderId);

        return ResponseEntity.noContent().build();
    }
}
