
package com.vidoo.videos.controller;

import com.vidoo.videos.dto.VideoResponse;
import com.vidoo.videos.dto.VideoStreamResponse;
import com.vidoo.videos.service.VideoService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public VideoResponse upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) UUID folderId,
            Authentication authentication
    ) throws IOException {

        return videoService.upload(
                getUserId(authentication),
                folderId,
                file
        );
    }

    @GetMapping
    public List<VideoResponse> list(Authentication authentication) {
        return videoService.list(getUserId(authentication));
    }

    @GetMapping("/{videoId}/stream")
    public ResponseEntity<Resource> stream(
            @PathVariable UUID videoId,
            Authentication authentication
    ) {

        VideoStreamResponse response = videoService.stream(
                getUserId(authentication),
                videoId
        );

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.contentType()))
                .contentLength(response.size())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline"
                )
                .body(response.resource());
    }

    private UUID getUserId(Authentication authentication) {
        try {
            return UUID.fromString(authentication.getName());
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid authenticated user"
            );
        }
    }
}
