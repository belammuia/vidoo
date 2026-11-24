package com.vidoo.folders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateFolderRequest(

        @NotBlank(message = "Folder name is required")
        @Size(max = 100, message = "Folder name cannot exceed 100 characters")
        String name,

        UUID parentFolderId
) {}
