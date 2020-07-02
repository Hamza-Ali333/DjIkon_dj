package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class RecyclerFollwers extends RecyclerView.Adapter<RecyclerFollwers.ViewHolder>{

    private ArrayList<Follower_Model> follower_modelArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Follwer_Profile;
        public TextView txt_Follwer_Name;

        public TextView txt_Edit;
        public RelativeLayout rlt_Follwer;

        public ViewHolder(View itemView){
            super(itemView);
            img_Follwer_Profile = itemView.findViewById(R.id.img_follwer);

            txt_Follwer_Name = itemView.findViewById(R.id.txt_follwer_name);

            txt_Edit = itemView.findViewById(R.id.txt_edit);
            rlt_Follwer = itemView.findViewById(R.id.follwer_layout);
        }
    }

//constructor
    public RecyclerFollwers(ArrayList<Follower_Model> follower_modelArrayList) {
        this.follower_modelArrayList = follower_modelArrayList;

    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followers,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Follower_Model currentItem = follower_modelArrayList.get(position);

       holder.img_Follwer_Profile.setImageResource(currentItem.getFollwer_image());
       holder.txt_Follwer_Name.setText(currentItem.getFollwer_Name());

       holder.rlt_Follwer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(), EditFollwerActivity.class);
               view.getContext().startActivity(i);
           }
       });


}

    @Override
    public int getItemCount() {
        return follower_modelArrayList.size();
    }


}
