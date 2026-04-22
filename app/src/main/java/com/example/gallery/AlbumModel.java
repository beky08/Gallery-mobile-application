package com.example.gallery;

public class AlbumModel {
    private String albumName;
    private String firstImagePath;
    private int totalImages;

    public AlbumModel(String albumName, String firstImagePath, int totalImages) {
        this.albumName = albumName;
        this.firstImagePath = firstImagePath;
        this.totalImages = totalImages;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public int getTotalImages() {
        return totalImages;
    }
}