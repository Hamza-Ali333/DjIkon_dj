package com.ikonholdings.ikoniconnects_subscriber.ResponseModels;

public class PaymentHistoryModel {
    private int id;
    private String amount,
            transaction_id,
            created_at,
            sender_firstname,
            sender_lastname,
            sender_profileImage,
            type;


    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSender_firstname() {
        return sender_firstname;
    }

    public String getSender_lastname() {
        return sender_lastname;
    }

    public String getSender_profileImage() {
        return sender_profileImage;
    }

    public String getType() {
        return type;
    }
}
