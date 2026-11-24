package com.vidoo.videos.entity;

import com.vidoo.common.entity.BaseEntity;
import com.vidoo.folders.entity.Folder;
import com.vidoo.users.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video extends BaseEntity {

    public enum Status {
        UPLOADING,
        UPLOADED,
        FAILED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(nullable = false, length = 255)
    private String originalFilename;

    @Column(nullable = false, length = 255)
    private String storageFilename;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private Long sizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    protected Video() {
    }

    public Video(
            User owner,
            Folder folder,
            String originalFilename,
            String storageFilename,
            String contentType,
            Long sizeBytes
    ) {
        this.owner = owner;
        this.folder = folder;
        this.originalFilename = originalFilename;
        this.storageFilename = storageFilename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.status = Status.UPLOADING;
    }

    public User getOwner() {
        return owner;
    }

    public Folder getFolder() {
        return folder;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStorageFilename() {
        return storageFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public Status getStatus() {
        return status;
    }

    public void markUploaded() {
        this.status = Status.UPLOADED;
    }

    public void markFailed() {
        this.status = Status.FAILED;
    }
}