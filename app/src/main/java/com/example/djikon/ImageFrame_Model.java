package com.example.djikon;

public class ImageFrame_Model {
    private int image;
    private String Title;

    public ImageFrame_Model(int image, String title) {
        this.image = image;
        Title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return Title;
    }
}
