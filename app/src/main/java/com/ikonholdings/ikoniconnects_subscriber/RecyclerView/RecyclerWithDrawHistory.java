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
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.WithDrawModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerWithDrawHistory extends RecyclerView.Adapter<RecyclerWithDrawHistory.ViewHolder>{

    private List<WithDrawModel> mWithDrawModels;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_Sender_Name;
        public TextView txt_Amount;
        public TextView  txt_Date;
        public TextView  txt_TransactionId;
        public ProgressBar progressBar;

        public ViewHolder(View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarProfile);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.sender_profile);

            txt_Sender_Name = itemView.findViewById(R.id.sender_name);
            txt_Amount = itemView.findViewById(R.id.amount);
            txt_Date = itemView.findViewById(R.id.date);
            txt_TransactionId = itemView.findViewById(R.id.transaction_id);
        }


    }

//constructor
    public RecyclerWithDrawHistory(List<WithDrawModel> mWithDrawModels) {
        this.mWithDrawModels = mWithDrawModels;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_history,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final WithDrawModel currentItem = mWithDrawModels.get(position);

       holder.txt_Sender_Name.setText(currentItem.getSender_firstname()+" "+currentItem.getSender_lastname());
       holder.txt_Amount.setText("$"+currentItem.getAmount());
       holder.txt_TransactionId.setText(currentItem.getTransaction_id());
       holder.txt_Date.setText(currentItem.getCreated_at());

        if (currentItem.getSender_profileImage() != null) {
            Picasso.get().load(ApiClient.Base_Url+currentItem.getSender_profileImage())
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
        return mWithDrawModels.size();
    }

}
