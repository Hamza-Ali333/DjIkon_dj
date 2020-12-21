package com.Ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class MainActivityResponseModel {
    private String firstname,
            lastname,
            profile_image;
    private int account_status;

    public int getAccount_status() {
        return account_status;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getProfile_image() {
        return profile_image;
    }
}
