package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class RecyclerBlockedUser extends RecyclerView.Adapter<RecyclerBlockedUser.ViewHolder>{

    private ArrayList<BlockedUser_Model> mSubscribeToArtistArrayList;
    private String st_Activity_Name;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView  txt_Subscribe_Artist_Status;
        public TextView  txt_UnFollow;
        public RelativeLayout rlt_SubscribeArtist;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_follwer);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_follwer_name);
            txt_Subscribe_Artist_Status = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            txt_UnFollow = itemView.findViewById(R.id.txt_UnFollow);
            rlt_SubscribeArtist = itemView.findViewById(R.id.follwer_layout);
        }
    }

//constructor
    public RecyclerBlockedUser(ArrayList<BlockedUser_Model> subscribeToArtistArrayList, String ActivityName) {
        this.mSubscribeToArtistArrayList = subscribeToArtistArrayList;
        this.st_Activity_Name= ActivityName;

    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_artist_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BlockedUser_Model currentItem = mSubscribeToArtistArrayList.get(position);

       holder.img_Subscribe_Artist_Profile.setImageResource(currentItem.getImg_Subscribe_Artist());
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getTxt_SubscribeArtistName());
       holder.txt_Subscribe_Artist_Status.setText(currentItem.getTxt_SubscribeArtistStatus());



       if(st_Activity_Name.equals("Notification")){
           holder.txt_UnFollow.setVisibility(View.GONE);
       }

       holder.rlt_SubscribeArtist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(view.getContext(), DjPrpfileActivity.class);
               view.getContext().startActivity(intent);
           }
       });

        holder.txt_UnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });




}

    @Override
    public int getItemCount() {
        return mSubscribeToArtistArrayList.size();
    }
}
