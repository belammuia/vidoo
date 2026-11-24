package com.vidoo.folders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameFolderRequest(

        @NotBlank(message = "Folder name is required")
        @Size(max = 100, message = "Folder name cannot exceed 100 characters")
        String name
) {}
