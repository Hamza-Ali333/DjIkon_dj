package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.RequestedSongsModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerSongsRequest extends RecyclerView.Adapter<RecyclerSongsRequest.ViewHolder>{

    private List<RequestedSongsModel> requestedSongsModels;
    private DatabaseReference myRef;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_Requester_Name;
        public TextView  txt_SongName;
        public TextView  txt_Date;
        public ProgressBar progressBar;

        public ViewHolder(View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarProfile);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.img_profile);

            txt_Requester_Name = itemView.findViewById(R.id.txt_user_name);
            txt_SongName = itemView.findViewById(R.id.txt_song);
            txt_Date = itemView.findViewById(R.id.txt_request_date);
        }


    }

//constructor
    public RecyclerSongsRequest(List<RequestedSongsModel> requestedSongsModels) {
        this.requestedSongsModels = requestedSongsModels;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_requests,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final RequestedSongsModel currentItem = requestedSongsModels.get(position);

       holder.txt_Requester_Name.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
       holder.txt_SongName.setText(currentItem.getSong_name());
       holder.txt_Date.setText(currentItem.getRequest_date());

        if (!currentItem.getProfile_image().equals("no")) {
            Picasso.get().load(ApiClient.Base_Url+currentItem.getProfile_image())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_msg_Subscriber_Profile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
}

    @Override
    public int getItemCount() {
        return requestedSongsModels.size();
    }

    private void deleteNode(String Key,int position) {
        requestedSongsModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, requestedSongsModels.size());

        myRef.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                Log.i("TAG", "onDataChange: Done ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("TAG", "onDataChange: Cancle ");
            }
        });
    }
}
