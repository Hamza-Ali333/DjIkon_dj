package com.Ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.KeyBoard;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerTransactionDetail;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.Transaction;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.WithDrawModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WithDrawFragment extends Fragment {

    private TextView txt_Total_Earning, txt_CurrentBalance, txt_Empty;
    private FloatingActionButton btn_CreateNew;

    private ConstraintLayout parentLayout;

    private RecyclerView mRecyclerView;
    private RecyclerTransactionDetail mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar recyclerProgressBar;
    private ProgressBar progressBar;
    private Integer currentBalance;

    private String[] filterArray = {"All", "Accepted", "Rejected","In Process"};//for sippiner adapter

    private List<Transaction> transactionList;

    private Spinner mSpinner;

    private Boolean activityAlreadyRuned = false;//this variable for spinner first time code run avoid


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.withdraws_fragment,container,false);
       mRecyclerView = v.findViewById(R.id.withdraw_recycler);
       createReferences(v);

       transactionList = new ArrayList<>();

       new GetWalletCurrentDetail().execute();
       new GetTansactionList().execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_gender_spinner, R.id.genders, filterArray);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int status, long l) {

                if(activityAlreadyRuned){
                    switch (status){
                        case 0:
                            mAdapter.filterList(transactionList);
                            break;
                        case 1:
                        case 2:
                            filter(status);
                            break;
                        case 3:
                            filter(0);
                            break;
                    }
                }else {
                    activityAlreadyRuned = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       btn_CreateNew.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(currentBalance > 5) {
                   openRequestWithDrawDialog();
               }else {
                   DialogsUtils.showAlertDialog(getContext(),
                           false,
                           "Note",
                           "You can't make request Your Current balance is low from $5");
               }
           }
       });

       return v;
    }

    private void buildRecyclerView(List<Transaction> transactionList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerTransactionDetail(transactionList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createReferences (View v) {
        txt_Empty = v.findViewById(R.id.info);
        txt_Total_Earning = v.findViewById(R.id.total);
        txt_CurrentBalance = v.findViewById(R.id.currentBalance);
        btn_CreateNew = v.findViewById(R.id.createNew);
        parentLayout = v.findViewById(R.id.parent);

        recyclerProgressBar = v.findViewById(R.id.progressRecycler);
        progressBar = v.findViewById(R.id.progressBar);
        mSpinner = v.findViewById(R.id.spinner);
    }

    private void openRequestWithDrawDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_request_withdraw, null);

        EditText edt_Amount = view.findViewById(R.id.edt_Amount);
        Button btn_Submit = view.findViewById(R.id.btn_submit);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_Amount.getText().toString().isEmpty()){
                    edt_Amount.setError("Enter some value.");
                    edt_Amount.requestFocus();
                }
                else if(Integer.parseInt(edt_Amount.getText().toString()) > currentBalance){
                    edt_Amount.setError("low balance check your current balance");
                    edt_Amount.requestFocus();
                }else if(Integer.parseInt(edt_Amount.getText().toString()) < 5){
                    edt_Amount.setError("Amount should be greater then 5");
                    edt_Amount.requestFocus();
                } else {
                    alertDialog.dismiss();
                    KeyBoard.hideKeyboard(getActivity());
                    new PostWithDrawRequest(edt_Amount.getText().toString()).execute();
                }
            }
        });
    }

    private void filter(Integer searchStatus){
        List<Transaction> transactionList = new ArrayList<>();
        Integer Status;
        for(Transaction item: this.transactionList) {
            Status = item.getStatus();
            if(Status == searchStatus){
                transactionList.add(item);
            }
        }

        mAdapter.filterList(transactionList);
    }

    private class GetWalletCurrentDetail extends AsyncTask<Void,Void,Void> {

        Call<WithDrawModel> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getWalletDetail();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<WithDrawModel>() {
                @Override
                public void onResponse(Call<WithDrawModel> call, Response<WithDrawModel> response) {
                    if (!response.isSuccessful()) {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    WithDrawModel detail = response.body();
                    currentBalance = detail.getWallet();
                    txt_CurrentBalance.setText("$"+detail.getWallet());
                    txt_Total_Earning.setText("$"+detail.getTotalEarning());
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<WithDrawModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                            DialogsUtils.showResponseMsg(getContext(),true);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class GetTansactionList extends AsyncTask<Void,Void,Void> {

        Call<List<Transaction>> call;
        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getTransaction();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<Transaction>>() {
                @Override
                public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                    if(!response.isSuccessful()){
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error","Something happened wrong\nplease try again!");
                        recyclerProgressBar.setVisibility(View.GONE);
                        return;
                    }

                    transactionList = response.body();
                    if(transactionList.isEmpty()){
                        txt_Empty.setVisibility(View.VISIBLE);
                    }else {
                        txt_Empty.setVisibility(View.GONE);
                    }
                        buildRecyclerView(transactionList);


                    recyclerProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<List<Transaction>> call, Throwable t) {
                    recyclerProgressBar.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(),true);

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class PostWithDrawRequest extends AsyncTask<Void,Void,Void> {

        Call<SuccessErrorModel> call;
        String Amount;
        AlertDialog loadingDialog;

        public PostWithDrawRequest(String Amount) {
            this.Amount = Amount;
           loadingDialog = DialogsUtils.showProgressDialog(getContext(),
                    "Posting",
                    "Please Wait While Posting Request");
        }

        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.postPaymentRequest(Amount);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(!response.isSuccessful()){
                        DialogsUtils.showAlertDialog(getContext(),
                                false,"Error",
                                "Something happened wrong\nplease try again!");
                        loadingDialog.dismiss();
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = sdf.format(new Date());
                    Transaction newTrans = new Transaction(Integer.parseInt(Amount),0,currentDate);

                    transactionList.add(0,newTrans);
                    loadingDialog.dismiss();
                    mAdapter.notifyItemInserted(0);
                    mRecyclerView.smoothScrollToPosition(0);

                    currentBalance = currentBalance - Integer.parseInt(Amount);
                    txt_CurrentBalance.setText("$"+currentBalance);
                    txt_Empty.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    loadingDialog.dismiss();
                    DialogsUtils.showResponseMsg(getContext(),true);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}

