package com.ikonholdings.ikoniconnects_subscriber;

public class CacelAndAcceptRequest_Model {
    private int requester_image;
    private String requester_name,  service_charges, service_name, discount,address;


    public CacelAndAcceptRequest_Model(int requester_image, String service_charges, String requester_name, String service_name, String discount,  String address) {
        this.requester_image = requester_image;
        this.service_charges = service_charges;
        this.requester_name = requester_name;
        this.service_name = service_name;
        this.discount = discount;

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


    public String getAddress() {
        return address;
    }
}
