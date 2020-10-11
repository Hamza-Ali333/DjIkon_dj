package com.ikonholdings.ikoniconnects_subscriber.Chat.Model;

import java.util.List;

public class ManytoManyChatModel {

    private String receivers;
    private String sender, message, time_stemp, image, key;


    public ManytoManyChatModel() {
        //required
    }

    public ManytoManyChatModel(String receivers, String sender, String message, String time_stemp, String image, String key) {
        this.receivers = receivers;
        this.sender = sender;
        this.message = message;
        this.time_stemp = time_stemp;
        this.image = image;
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public  String getReceivers() {
        return receivers;
    }

    public void setReceivers( String receivers) {
        this.receivers = receivers;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_stemp() {
        return time_stemp;
    }

    public void setTime_stemp(String time_stemp) {
        this.time_stemp = time_stemp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}