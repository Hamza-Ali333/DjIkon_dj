package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.Activities.EditFollowerActivity;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;
import com.ikonholdings.ikoniconnects_subscriber.UserAdminControrls.BlockUnBlockDeleteUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerFollowerAndBlockedUser extends RecyclerView.Adapter<RecyclerFollowerAndBlockedUser.ViewHolder>{

    private List<FollowersModel> follower_List;
    private Integer UserType;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Follwer_Profile;
        public TextView txt_Follwer_Name;
        public TextView txt_Follwer_Location;
        public ProgressBar progressBar;

        public TextView txt_Action;

        public ViewHolder(View itemView){
            super(itemView);
            img_Follwer_Profile = itemView.findViewById(R.id.img_profile);
            progressBar = itemView.findViewById(R.id.progressBar);

            txt_Follwer_Name = itemView.findViewById(R.id.txt_User_name);
            txt_Follwer_Location = itemView.findViewById(R.id.location);

            txt_Action = itemView.findViewById(R.id.txt_action);
        }
    }

//constructor
    public RecyclerFollowerAndBlockedUser(List<FollowersModel> follower_List, Integer UserType) {
        this.follower_List = follower_List;
        this.UserType = UserType;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followers_and_blocked_user,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FollowersModel currentItem = follower_List.get(position);

       holder.txt_Follwer_Name.setText(currentItem.getFirstname()+" "+ currentItem.getLastname());
       holder.txt_Follwer_Location.setText(currentItem.getLocation());

       switch (UserType){
           case 1:
           case 2:
               holder.txt_Action.setText("Unfollow");
               break;
           case 3:
               holder.txt_Action.setText("UnBlock");
               break;
           case 4:
               holder.txt_Action.setText("Give Access");
               break;
       }

        if(!currentItem.getProfile_image().equals("no") &&
                currentItem.getProfile_image() != null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load((ApiClient.Base_Url+currentItem.getProfile_image()))
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_Follwer_Profile, new com.squareup.picasso.Callback() {
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

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent i = new Intent(view.getContext(), EditFollowerActivity.class);
                   i.putExtra("id",String.valueOf(currentItem.getId()));
                   view.getContext().startActivity(i);
               }
           });

        holder.txt_Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType == 1 || UserType == 2){
                    new BlockUnBlockDeleteUser("unFollowUser/"+currentItem.getId(),
                            1,
                            v.getContext()).execute();
                }
                else if(UserType == 3){
                    new BlockUnBlockDeleteUser("blockUser/"+currentItem.getId(),
                            0,
                            v.getContext()).execute();
                }else if(UserType == 4){
                    new BlockUnBlockDeleteUser("blockReferralUserAccess/"+currentItem.getId(),
                            1,
                            v.getContext()).execute();
                }
            }
        });

}

    @Override
    public int getItemCount() {
        return follower_List.size();
    }

}
