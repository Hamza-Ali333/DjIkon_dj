package com.Ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class RequestedSongsModel {
    private int user_id;
    private int id;
    private String firstname,
            lastname,
            profile_image,
            request_date,
            song_name;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getRequest_date() {
        return request_date;
    }

    public String getSong_name() {
        return song_name;
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
