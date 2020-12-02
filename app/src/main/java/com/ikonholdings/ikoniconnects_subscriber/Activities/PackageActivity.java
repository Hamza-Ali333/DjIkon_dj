package com.ikonholdings.ikoniconnects_subscriber.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PackageActivity extends AppCompatActivity {

    Button btn_Monthly_Package, btn_Yearly_Package;
    ProgressDialog progressDialog;

    Context context;

    private String BrainTreeToken;
    private String planId;

    private static final int DROP_IN_REQUEST_CODE = 777;

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
        setContentView(R.layout.activtiy_package);
        getSupportActionBar().hide();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        context = this;

        btn_Monthly_Package = findViewById(R.id.btn_mothly_package);
        btn_Yearly_Package = findViewById(R.id.btn_year_package);


        btn_Monthly_Package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planId = "9sgr";
                new GetBrainTreeToken().execute();
            }
        });
        btn_Yearly_Package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planId = "mf4g";
                new GetBrainTreeToken().execute();
            }
        });

    }

    public void onBrainTreeSubmit(String brainTreeToken) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(brainTreeToken);

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DROP_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                progressDialog = DialogsUtils.showProgressDialog(context, "Post Amount", "Please wait while confirming collecting amount of service");

                PostNonceToServer(paymentMethodNonce);

            } else if (resultCode == RESULT_CANCELED) {
                //the user canceled
                Toast.makeText(this, "Payment Cancle", Toast.LENGTH_SHORT).show();
            } else {
                // handle errors here, an exception may be available in
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.i("TAG", "onActivityResult: " + error);
            }
        } else {
            Toast.makeText(this, "Card not found.Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetBrainTreeToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(context, "Posting Request", "Please Wait");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit  retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SuccessErrorModel> call = jsonApiHolder.getBrainTreeToken();

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if (response.isSuccessful()) {
                        SuccessErrorModel successErrorModel = response.body();
                        BrainTreeToken = successErrorModel.getSuccess();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                onBrainTreeSubmit(BrainTreeToken);
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        DialogsUtils.showResponseMsg(context, false);
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            DialogsUtils.showResponseMsg(context, true);
                        }
                    });
                }
            });
            return null;
        }
    }

    private void PostNonceToServer(String Nonce) {

        Retrofit  retrofit = ApiClient.retrofit(context);
        JSONApiHolder  jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.postPackage(
                Nonce,
                planId
        );

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    startActivity(new Intent(PackageActivity.this, MainActivity.class));
                    finish();
                }else {
                    DialogsUtils.showAlertDialog(context,
                            false,"Nonce","is post Successful");
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                DialogsUtils.showAlertDialog(context,
                        false,
                        "Note",
                        "Please check your internet and try again \n"+t.getMessage());
            }
        });
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