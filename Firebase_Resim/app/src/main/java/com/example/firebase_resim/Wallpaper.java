package com.example.firebase_resim;

public class Wallpaper {
    private String name;
    private String imageUrl;
    private String id;

    // Default constructor required for Firebase
    public Wallpaper() {
    }

    public Wallpaper(String name, String imageUrl, String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}