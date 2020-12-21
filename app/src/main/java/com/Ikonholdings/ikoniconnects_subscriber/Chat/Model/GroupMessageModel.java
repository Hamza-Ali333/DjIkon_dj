package com.Ikonholdings.ikoniconnects_subscriber.Chat.Model;

public class GroupMessageModel {
    private String message, sender_profile, sender_Uid, receivers_UIds;

    public GroupMessageModel() {
    }

    public GroupMessageModel(String message, String sender_profile, String sender_Uid, String receivers_UIds) {
        this.message = message;
        this.sender_profile = sender_profile;
        this.sender_Uid = sender_Uid;
        this.receivers_UIds = receivers_UIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_profile() {
        return sender_profile;
    }

    public void setSender_profile(String sender_profile) {
        this.sender_profile = sender_profile;
    }

    public String getSender_Uid() {
        return sender_Uid;
    }

    public void setSender_Uid(String sender_Uid) {
        this.sender_Uid = sender_Uid;
    }

    public String getReceivers_UIds() {
        return receivers_UIds;
    }

    public void setReceivers_UIds(String receivers_UIds) {
        this.receivers_UIds = receivers_UIds;
    }
}
