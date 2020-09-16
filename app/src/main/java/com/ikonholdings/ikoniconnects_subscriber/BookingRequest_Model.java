package com.ikonholdings.ikoniconnects_subscriber;

public class BookingRequest_Model {
    private int requester_image;
    private String requester_name,  service_charges, service_name, discount, start_date, end_date, address;


    public BookingRequest_Model(int requester_image, String service_charges, String requester_name, String service_name, String discount, String start_date, String end_date, String address) {
        this.requester_image = requester_image;
        this.service_charges = service_charges;
        this.requester_name = requester_name;
        this.service_name = service_name;
        this.discount = discount;
        this.start_date = start_date;
        this.end_date = end_date;
        this.address = address;
    }


    public int getRequester_image() {
        return requester_image;
    }

    public String getService_charges() {
        return service_charges;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public String getService_name() {
        return service_name;
    }

    public String getDiscount() {
        return discount;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getAddress() {
        return address;
    }
}
