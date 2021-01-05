package com.Ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerNotification;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.NotificationModel;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NotificationFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_Msg;

    private AlertDialog loadingDialog;
    private List<NotificationModel> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);
        txt_Msg = v.findViewById(R.id.msg);

        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        new DownloadThisArtistBlogs().execute();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return v;
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder item, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView

            int position = item.getAdapterPosition();
            notificationList.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, notificationList.size());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.print_btn_color))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };

    private class DownloadThisArtistBlogs extends AsyncTask<Void, Void, Void> {

        Call<List<NotificationModel>> call;

        @Override
        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            call = jsonApiHolder.getNotification();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            call.enqueue(new Callback<List<NotificationModel>>() {
                @Override
                public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {

                    if (!response.isSuccessful()) {
                        loadingDialog.dismiss();
                        return;
                    }

                    notificationList = response.body();
                    if (!notificationList.isEmpty()) {
                        txt_Msg.setVisibility(View.GONE);
                        buildRecyclerView(notificationList);
                    } else {
                        txt_Msg.setVisibility(View.VISIBLE);
                    }
                    loadingDialog.dismiss();


                }

                @Override
                public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

                    loadingDialog.dismiss();
                    DialogsUtils.showResponseMsg(getContext(), true);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void buildRecyclerView(List<NotificationModel> list) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerNotification(list, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class DeleteRequest extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        int position;
        int requestId;


        public DeleteRequest(int position, int requestId) {
            this.position = position;
            this.requestId = requestId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(getContext(),
                    "Working...",
                    "Please wait. While connecting with the server.");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SuccessErrorModel> call = jsonApiHolder.deleteSongRequest(
                    requestId
            );
            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();

                    }else {
                                progressDialog.dismiss();
                                DialogsUtils.showAlertDialog(getContext(),
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");

                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                            progressDialog.dismiss();
                            DialogsUtils.showAlertDialog(getContext(),
                                    false,
                                    "No Server Connection",
                                    t.getMessage());
                        }
                    });
            return null;
        }
    }

}