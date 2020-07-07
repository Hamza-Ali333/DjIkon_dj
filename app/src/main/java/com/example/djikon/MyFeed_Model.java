package com.example.djikon;

public class MyFeed_Model {

    private int Img_UploaderProfile,
            img_FeedImage;


    private String title,
            txt_UploadTime,
            desciption,
            Likes,
            video;


    public MyFeed_Model(int img_UploaderProfile, int img_FeedImage,

                        String title, String txt_UploadTime,
                        String desciption, String Likes,
                        String video) {

        this.Img_UploaderProfile = img_UploaderProfile;
        this.img_FeedImage = img_FeedImage;


        this.title = title;
        this.txt_UploadTime = txt_UploadTime;
        this.desciption = desciption;
        this.Likes = Likes;
        this.video = video;
    }

    public int getImg_UploaderProfile() {
        return Img_UploaderProfile;
    }

    public int getImg_FeedImage() {
        return img_FeedImage;
    }

    public String getTitle() {
        return title;
    }

    public String getTxt_UploadTime() {
        return txt_UploadTime;
    }

    public String getDesciption() {
        return desciption;
    }

    public String getLikes() {
        return Likes;
    }

    public String getVideo() {
        return video;
    }
}
