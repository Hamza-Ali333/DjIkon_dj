package com.ikonholdings.ikoniconnects_subscriber;

public class Services_Model {

    private int service_image;
    private String Service_Title, Service_Discription, charges;

    public Services_Model(int service_image, String service_Title, String service_Discription, String charges) {
        this.service_image = service_image;
        Service_Title = service_Title;
        Service_Discription = service_Discription;
        this.charges = charges;
    }


    public int getService_image() {
        return service_image;
    }

    public String getService_Title() {
        return Service_Title;
    }

    public String getService_Discription() {
        return Service_Discription;
    }

    public String getCharges() {
        return charges;
    }
}
