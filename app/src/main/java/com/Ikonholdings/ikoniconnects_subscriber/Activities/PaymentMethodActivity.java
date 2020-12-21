package com.Ikonholdings.ikoniconnects_subscriber.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.Ikonholdings.ikoniconnects_subscriber.R;

public class PaymentMethodActivity extends AppCompatActivity {

    private Button btn_PayNow;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        getSupportActionBar().setTitle("Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        btn_PayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, PurchaseServiceActivity.class);
                startActivity(i);
            }
        });
    }

    private void createRefrences () {
        btn_PayNow = findViewById(R.id.btn_Pay_Now);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}