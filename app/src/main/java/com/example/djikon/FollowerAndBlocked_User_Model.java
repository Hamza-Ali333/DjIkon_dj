package com.example.djikon;

public class FollowerAndBlocked_User_Model {
    private int follwer_image;
    private String follwer_Name;


    public FollowerAndBlocked_User_Model(int follwer_image, String follwer_Name) {
        this.follwer_image = follwer_image;
        this.follwer_Name = follwer_Name;
    }

    public int getFollwer_image() {
        return follwer_image;
    }

    public String getFollwer_Name() {
        return follwer_Name;
    }
}
