package com.ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class Transaction {

    Integer amount,
            status;
     String created_at;

    public Integer getAmount() {
        return amount;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }
}
