package com.ikonholdings.ikoniconnects_subscriber.Notification;

public class Sender {

    public Data data;//Data Class ObJect
    public String to;//receiver

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
