package com.ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class NotificationModel {

    private int id;
    private String notification,
    time,
    firstname,
    lastname,
    profile_image;

    public int getId() {
        return id;
    }

    public String getNotification() {
        return notification;
    }

    public String getTime() {
        return time;
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
