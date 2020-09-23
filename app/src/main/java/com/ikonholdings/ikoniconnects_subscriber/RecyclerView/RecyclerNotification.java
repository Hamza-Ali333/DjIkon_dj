package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.NotificationModel;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerNotification extends RecyclerView.Adapter<RecyclerNotification.ViewHolder>{

    private List<NotificationModel> notificationModelList;

    private Context context;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_Name;
        public TextView txt_Notification;
        public TextView txt_Time;
        public ProgressBar progressBar;

        public ViewHolder(View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarProfile);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.img_profile);

            txt_Name = itemView.findViewById(R.id.txt_User_name);
            txt_Notification = itemView.findViewById(R.id.notification);
            txt_Time = itemView.findViewById(R.id.time);
        }

    }

//constructor
    public RecyclerNotification(List<NotificationModel> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final NotificationModel currentItem = notificationModelList.get(position);

       holder.txt_Name.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
       holder.txt_Notification.setText(currentItem.getNotification());
       holder.txt_Time.setText(currentItem.getTime());

        if (!currentItem.getProfile_image().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url+currentItem.getProfile_image())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_msg_Subscriber_Profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }
}

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

}
