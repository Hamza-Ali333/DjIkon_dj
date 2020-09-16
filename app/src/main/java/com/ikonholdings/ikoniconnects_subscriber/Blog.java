package com.ikonholdings.ikoniconnects_subscriber;

public class Blog {

    private String title;
    private String description;
    private String photo;
    private String Likes;
    private String Video;

    public Blog(String title, String description, String photo, String likes, String video) {
        this.title = title;
        this.description = description;
        this.photo = photo;
        Likes = likes;
        Video = video;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getLikes() {
        return Likes;
    }

    public String getVideo() {
        return Video;
    }
}
