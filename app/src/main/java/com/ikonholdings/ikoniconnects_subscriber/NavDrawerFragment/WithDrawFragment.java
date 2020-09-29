package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerTransactionDetail;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.Transaction;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.WithDrawModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class WithDrawFragment extends Fragment {
    TextView txt_Total_Earning, txt_CurrentBalance;
    Button btn_CreateNew;

    ConstraintLayout parentLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar recyclerProgressBar;
    private ProgressBar progressBar;
    private Integer currentBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.withdraws_fragment,container,false);
        mRecyclerView = v.findViewById(R.id.withdraw_recycler);
        createReferences(v);

       new GetWalletCurrentDetail().execute();
       new GetTansactionList().execute();

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
        txt_Total_Earning = v.findViewById(R.id.total);
        txt_CurrentBalance = v.findViewById(R.id.currentBalance);
        btn_CreateNew = v.findViewById(R.id.createNew);
        parentLayout = v.findViewById(R.id.parent);

        recyclerProgressBar = v.findViewById(R.id.progressRecycler);
        progressBar = v.findViewById(R.id.progressBar);
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
                if(Integer.parseInt(edt_Amount.getText().toString()) > currentBalance){
                    edt_Amount.setError("");
                }else {
                    alertDialog.dismiss();
                    new PostWithDrawRequest(edt_Amount.getText().toString()).execute();
                }
            }
        });
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
                     currentBalance = detail.getTotalEarning();
                    txt_CurrentBalance.setText("$"+detail.getWallet());
                    txt_Total_Earning.setText("$"+currentBalance);
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

                    List<Transaction> transactionList = response.body();
                    if(transactionList.isEmpty()){
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "No Transaction",
                                "it's seems like you did not have any transaction yet!");
                    }else {
                        buildRecyclerView(transactionList);
                    }

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

    private class PostWithDrawRequest extends AsyncTask<Void,Void,Void> implements com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment.PostWithDrawRequest {

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
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(),
                            "You can get your amount in 2 to 3 Business Days.", Toast.LENGTH_LONG).show();
                    Fragment frag = new WithDrawFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
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
