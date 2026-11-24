package com.vidoo.videos.service;

import com.vidoo.folders.entity.Folder;
import com.vidoo.folders.repository.FolderRepository;
import com.vidoo.users.entity.User;
import com.vidoo.users.repository.UserRepository;
import com.vidoo.videos.dto.VideoResponse;
import com.vidoo.videos.dto.VideoStreamResponse;
import com.vidoo.videos.entity.Video;
import com.vidoo.videos.repository.VideoRepository;
import com.vidoo.videos.storage.VideoStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final VideoStorageService storageService;

    public VideoService(
            VideoRepository videoRepository,
            UserRepository userRepository,
            FolderRepository folderRepository,
            VideoStorageService storageService
    ) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.storageService = storageService;
    }

    @Transactional
    public VideoResponse upload(
            UUID ownerId,
            UUID folderId,
            MultipartFile file
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Video file is empty");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("video/")) {
            throw new IllegalArgumentException("Only video files are allowed");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Folder folder = null;

        if (folderId != null) {
            folder = folderRepository.findByIdAndOwnerId(folderId, ownerId)
                    .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
        }

        String storageFilename = storageService.store(file);

        try {
            Video video = new Video(
                    owner,
                    folder,
                    file.getOriginalFilename() != null
                            ? file.getOriginalFilename()
                            : "video",
                    storageFilename,
                    contentType,
                    file.getSize()
            );

            video.markUploaded();

            return VideoResponse.from(videoRepository.save(video));

        } catch (RuntimeException exception) {
            storageService.delete(storageFilename);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<VideoResponse> list(UUID ownerId) {
        return videoRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(VideoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VideoStreamResponse stream(UUID ownerId, UUID videoId) {

        Video video = videoRepository.findByIdAndOwnerId(videoId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        if (video.getStatus() != Video.Status.UPLOADED) {
            throw new IllegalArgumentException("Video is not available");
        }

        java.nio.file.Path path = storageService.load(video.getStorageFilename());

        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.FileSystemResource(path);

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException("Video file is unavailable");
        }

        return new VideoStreamResponse(
                resource,
                video.getContentType(),
                video.getSizeBytes()
        );
    }
}