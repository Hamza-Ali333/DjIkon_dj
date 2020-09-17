package com.ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class BookingRequest {

   private  int id;
   private String firstname;
   private String lastname;
   private String profile_image;
   private String location;

    public int getId() {
        return id;
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

    public String getLocation() {
        return location;
    }
}
