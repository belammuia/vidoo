package com.vidoo.folders.entity;

import com.vidoo.common.entity.BaseEntity;
import com.vidoo.users.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "folders")

public class Folder extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)

    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @Column(nullable = false, length = 100)
    private String name;

    protected Folder(){

    }

    public Folder(User owner, Folder parentFolder, String name){
        this.owner = owner;
        this.parentFolder = parentFolder;
        this.name = name;
    }

    public User getOwner(){
        return owner;
    }

    public  Folder getParentFolder(){
        return parentFolder;
    }

    public String getName(){
        return name;
    }

    public void rename(String name){
        this.name = name;
    }
}


