package com.ikonholdings.ikoniconnects_subscriber.Chat.Notification;

public class Sender {

    public Notification notification;//Data Class ObJect
    public String to;//receiver

    public Sender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

}
