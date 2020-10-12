package com.ikonholdings.ikoniconnects_subscriber.Chat.Model;

import java.io.Serializable;


public class ReceiverList implements Serializable {

    private String name;
    private Integer id;

    public ReceiverList() {
    }

    public ReceiverList(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
