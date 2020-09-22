package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.MyBookingRequests;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BankDetailFragment extends Fragment {

    private Spinner mSpinner;
    private Group bankTransfer;
    private Group paypal;

    private Button btn_submit;

    private EditText edt_IBAN_NO, edt_Account, edt_Address, edt_Swift_Code, edt_Email, edt_Reference;

    private String SelectedMethod = "Select Your Payment Method";
    private String[] MethodArray = {"Select Your Payment Method", "Paypal", "Bank Transfer"};//for sippiner adapter


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.activity_bank_details,container,false);
       createRefrences(v);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_gender_spinner, R.id.genders, MethodArray);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedMethod = MethodArray[i];
                if(SelectedMethod.equals("Paypal")){
                    bankTransfer.setVisibility(View.GONE);
                    paypal.setVisibility(View.VISIBLE);
                }else if(SelectedMethod.equals("Bank Transfer")) {
                    bankTransfer.setVisibility(View.VISIBLE);
                    paypal.setVisibility(View.GONE);
                }else {
                    bankTransfer.setVisibility(View.GONE);
                    paypal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInfoRight()){
                    showAlertDialog();
                }
            }
        });

       return v;
    }

    private boolean isInfoRight() {
        boolean result = true;
        if(SelectedMethod.equals("Paypal")) {
            if (edt_IBAN_NO.getText().toString().trim().isEmpty()) {
                edt_IBAN_NO.setError("Required");
                edt_IBAN_NO.requestFocus();
                result = false;
            } else if (edt_Account.getText().toString().trim().isEmpty()) {
                edt_Account.setError("Required");
                edt_Account.requestFocus();
                result = false;
            } else if (edt_Address.getText().toString().trim().isEmpty()) {
                edt_Address.setError("Required");
                edt_Address.requestFocus();
                result = false;
            } else if (edt_Swift_Code.getText().toString().trim().isEmpty()) {
                edt_Swift_Code.setError("Required");
                edt_Swift_Code.requestFocus();
                result = false;
            }
        }else {
             if (edt_Email.getText().toString().trim().isEmpty()) {
                 edt_Email.setError("Required");
                 edt_Email.requestFocus();
                result = false;
            }
        }
        return result;
    }

    public void  showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Note");
        builder.setMessage("Are you sure you want submit this withdraw method.\n" +
                "This can't be edit or change able in future.");
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sendDetailToServer();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);

        builder.show();
    }


    private void sendDetailToServer(){
        AlertDialog progressDialog = DialogsUtils.showProgressDialog(
                getContext(),
                "Submitting Detail",
                "Please While uploading details on sever.");
        Retrofit retrofit = ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.postBankDetail(
                SelectedMethod,
                edt_Email.getText().toString().trim(),
                edt_IBAN_NO.getText().toString().trim(),
                edt_Account.getText().toString().trim(),
                edt_Address.getText().toString().trim(),
                edt_Swift_Code.getText().toString().trim(),
                edt_Reference.getText().toString().trim()
        );

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    DialogsUtils.showSuccessDialog(
                            getContext(),
                            "Successfully Added",
                            "Congratulation your withdraw method detail sumbited.\n" +
                                    "Now you can withdraw you amount by your added method\n" +
                                    "Keep in mind you can't change this method in feature.Thank You.!");
                }else {
                    DialogsUtils.showResponseMsg(getContext(),false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                DialogsUtils.showResponseMsg(getContext(),true);
                progressDialog.dismiss();
            }
        });

    }

    private void createRefrences (View v) {
        mSpinner = v.findViewById(R.id.spinner);
        bankTransfer = v.findViewById(R.id.bank_transfer);
        paypal = v.findViewById(R.id.paypal);

        edt_IBAN_NO = v.findViewById(R.id.iban_No);
        edt_Account = v.findViewById(R.id.account_No);
        edt_Address = v.findViewById(R.id.address);
        edt_Swift_Code = v.findViewById(R.id.swift_code);
        edt_Email = v.findViewById(R.id.email);
        edt_Reference = v.findViewById(R.id.refrence);

        btn_submit = v.findViewById(R.id.submit);
    }
}
