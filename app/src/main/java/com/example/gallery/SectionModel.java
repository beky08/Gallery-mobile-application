package com.example.gallery;

public class SectionModel {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    public int type;
    public String albumName; // to Header only
    public ImageModel image; // to Item only

    // Constructor for Header
    public SectionModel(int type, String albumName) {
        this.type = type;
        this.albumName = albumName;
    }

    // Constructor for Item
    public SectionModel(int type, ImageModel image) {
        this.type = type;
        this.image = image;
    }
}