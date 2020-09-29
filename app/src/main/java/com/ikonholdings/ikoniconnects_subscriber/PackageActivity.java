package com.ikonholdings.ikoniconnects_subscriber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PackageActivity extends AppCompatActivity {

    Button btn_Monthly_Pakage, btn_Yearly_Package;
    ProgressDialog progressDialog;

    Context context;

    private String BrainTreeToken;
    private String planId;

    private static final int DROP_IN_REQUEST_CODE = 777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_package);
        getSupportActionBar().hide();

        context = this;

        btn_Monthly_Pakage = findViewById(R.id.btn_mothly_package);
        btn_Yearly_Package = findViewById(R.id.btn_year_package);


        btn_Monthly_Pakage.setOnClickListener(new View.OnClickListener() {
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
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", Nonce);
        params.put("plan_id", planId);

        client.post(ApiClient.Base_Url+"api/subscribe", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        progressDialog.dismiss();
                        DialogsUtils.showAlertDialog(context,
                                false,"Nonce","is post Successful");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        progressDialog.dismiss();
                        DialogsUtils.showAlertDialog(context,
                                false,
                                "Note",
                                "Please check your internet and try again \n"+error.getMessage());
                    }
                }
        );
    }


    private void lunchMainActivity(){
                Intent i = new Intent(PackageActivity.this,
                        PaymentMethodActivity.class);
                startActivity(i);
    }
}