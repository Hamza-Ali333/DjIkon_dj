package com.ikonholdings.ikoniconnects_subscriber.Chat;

public class UserChatListModel {

    private  String user_Id, user_Name, user_Uid ,imageUrl,key;
    private  String talkTime,last_send_msg;

    public UserChatListModel() {
    }

    //this constructor only for id , dJName, imageUrl
    public UserChatListModel(String user_Id, String user_Uid, String user_Name, String imageUrl,String key) {
        this.user_Id = user_Id;
        this.user_Uid = user_Uid;
        this.user_Name = user_Name;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public String getUser_Uid() {
        return user_Uid;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public void setUser_Uid(String user_Uid) {
        this.user_Uid = user_Uid;
    }

    public void setuser_Uid(String user_Uid) {
        this.user_Uid = user_Uid;
    }

    public void setuser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public void setuser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(String talkTime) {
        this.talkTime = talkTime;
    }

    public String getLast_send_msg() {
        return last_send_msg;
    }

    public void setLast_send_msg(String last_send_msg) {
        this.last_send_msg = last_send_msg;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUser_Id() {
        return user_Id;
    }

}
