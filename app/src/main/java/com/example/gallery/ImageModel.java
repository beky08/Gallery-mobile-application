package com.example.gallery;

public class ImageModel {
    private String path;
    private long id; // Added ID for deletion
    private String albumName; // Added for Album grouping

    public ImageModel(String path, long id, String albumName) {
        this.path = path;
        this.id = id;
        this.albumName = albumName;
    }

    public String getPath() {
        return path;
    }

    public long getId() {
        return id;
    }

    public String getAlbumName() {
        return albumName;
    }
}