package com.ikonholdings.ikoniconnects_subscriber;

public class CacelAndAcceptRequest_Model {
    private int id;
    private String start_date,
            end_date,
            start_time,
            end_time,
            name,
            email,
            phone,
            price,
            address,
            service_name,
            service_price_type,
            user_profile_image;

    public int getId() {
        return id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_price_type() {
        return service_price_type;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }
}