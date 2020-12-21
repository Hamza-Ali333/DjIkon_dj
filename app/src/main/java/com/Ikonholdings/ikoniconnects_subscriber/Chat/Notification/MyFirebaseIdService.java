package com.Ikonholdings.ikoniconnects_subscriber.Chat.Notification;


import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        updateToken(refreshToken);
    }

    private void updateToken(String refreshToken) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);

        reference.child(PreferenceData.getUserId(this)).setValue(token);
    }
}
