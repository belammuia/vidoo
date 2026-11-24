package com.vidoo.videos.dto;

import org.springframework.core.io.Resource;

public record VideoStreamResponse(
        Resource resource,
        String contentType,
        long size
) {
}