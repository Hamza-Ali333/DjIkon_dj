package com.ikonholdings.ikoniconnects_subscriber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCommnets extends RecyclerView.Adapter<RecyclerCommnets.ViewHolder>{

    private ArrayList<Comments_Model> mComment_Arraylist;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_CommenterProfile;


        public TextView txt_Commenter_Name, txt_comment, txt_Comment_Date, txt_Replay;

        public ViewHolder(View itemView){
            super(itemView);
            img_CommenterProfile = itemView.findViewById(R.id.img_profile);
            txt_Commenter_Name = itemView.findViewById(R.id.txt_user_name);
            txt_comment = itemView.findViewById(R.id.txt_song);
            txt_Comment_Date = itemView.findViewById(R.id.txt_request_date);
            txt_Replay = itemView.findViewById(R.id.txt_replay);
;

        }
    }


//constructor
    public RecyclerCommnets(ArrayList<Comments_Model> comments_models) {
        this.mComment_Arraylist = comments_models;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Comments_Model currentItem = mComment_Arraylist.get(position);

        holder.img_CommenterProfile.setImageResource(currentItem.getUserimage());


        holder.txt_Commenter_Name.setText(currentItem.getUserName());
        holder.txt_Comment_Date.setText(currentItem.getCommentDate());
        holder.txt_comment.setText(currentItem.getComment());


        holder.txt_Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), EditBlogActivity.class);
//                v.getContext().startActivity(i);
                Toast.makeText(v.getContext(), "This Feature will Available Soon", Toast.LENGTH_SHORT).show();
            }
        });




}

    @Override
    public int getItemCount() {
        return mComment_Arraylist.size();
    }
}
