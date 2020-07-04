package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class RecyclerFollowerAndBlockedUser extends RecyclerView.Adapter<RecyclerFollowerAndBlockedUser.ViewHolder>{

    private ArrayList<FollowerAndBlocked_User_Model> follower_modelArrayList;
    private String st_Activity_Name;
    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Follwer_Profile;
        public TextView txt_Follwer_Name;

        public TextView txt_Action;
        public RelativeLayout rlt_Follwer;

        public ViewHolder(View itemView){
            super(itemView);
            img_Follwer_Profile = itemView.findViewById(R.id.img_profile);

            txt_Follwer_Name = itemView.findViewById(R.id.txt_User_name);

            txt_Action = itemView.findViewById(R.id.txt_action);
            rlt_Follwer = itemView.findViewById(R.id.follwer_layout);
        }
    }

//constructor
    public RecyclerFollowerAndBlockedUser(ArrayList<FollowerAndBlocked_User_Model> follower_modelArrayList ,String activityName) {
        this.follower_modelArrayList = follower_modelArrayList;
        this.st_Activity_Name = activityName;

    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followers_and_blocked_user,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FollowerAndBlocked_User_Model currentItem = follower_modelArrayList.get(position);

       holder.img_Follwer_Profile.setImageResource(currentItem.getFollwer_image());
       holder.txt_Follwer_Name.setText(currentItem.getFollwer_Name());

       if(st_Activity_Name.equals("BlockedUsers")){
           holder.txt_Action.setText("UnBlock");
       }else {
           holder.txt_Action.setText("Edit");
           holder.rlt_Follwer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent i = new Intent(view.getContext(), EditFollwerActivity.class);
                   view.getContext().startActivity(i);
               }
           });
       }



}

    @Override
    public int getItemCount() {
        return follower_modelArrayList.size();
    }


}
