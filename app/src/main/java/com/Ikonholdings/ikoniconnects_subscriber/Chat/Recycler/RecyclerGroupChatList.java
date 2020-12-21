package com.Ikonholdings.ikoniconnects_subscriber.Chat.Recycler;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.Chat.GroupChatViewerActivity;
import com.Ikonholdings.ikoniconnects_subscriber.Chat.Model.GroupChatListModel;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class RecyclerGroupChatList extends RecyclerView.Adapter<RecyclerGroupChatList.ViewHolder>{

    private List<GroupChatListModel> mChatList;
    private DatabaseReference myRef;
    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_GroupProfile;
        public TextView txt_msg_Sender_Name;
        private ProgressBar mProgressBar;

        public ViewHolder(View itemView){
            super(itemView);
            img_GroupProfile = itemView.findViewById(R.id.img_msg_sender);
            txt_msg_Sender_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }

//constructor
    public RecyclerGroupChatList(List<GroupChatListModel> chat_List_modelArrayList, String currentUserId) {
        this.mChatList = chat_List_modelArrayList;
        myRef = FirebaseDatabase.getInstance().getReference("Chats").child("groups").child(currentUserId);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final GroupChatListModel currentItem = mChatList.get(position);

       holder.txt_msg_Sender_Name.setText(currentItem.getGroup_Name());

        if (currentItem.getGroup_Profile() != null && !currentItem.getGroup_Profile().equals("no")) {
            holder.mProgressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getGroup_Profile())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_GroupProfile, new Callback() {
                        @Override
                        public void onSuccess() {
                       holder.mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.mProgressBar.setVisibility(View.GONE);
                        }
                    });
        }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(), GroupChatViewerActivity.class);
               i.putExtra("userList",String.valueOf(currentItem.getGroup_User_Ids()));
               i.putExtra("list",(Serializable)currentItem.getGroup_User_Ids());
               i.putExtra("groupKey",currentItem.getGroupId());
               i.putExtra("node",currentItem.getNode());
               i.putExtra("groupName",currentItem.getGroup_Name());
               i.putExtra("groupImage",currentItem.getGroup_Profile());
               view.getContext().startActivity(i);
           }
       });

        //long clicked
       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public boolean onLongClick(View view) {
               PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.itemView);
               popupMenu.inflate(R.menu.chat_option);
               popupMenu.setGravity(Gravity.END);
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.delete:
                              deleteNode(currentItem.getGroupId(),position);
                               break;
                           default:
                               break;
                       }
                       return true;
                   }
               });
               popupMenu.show();
               return true;
           }
       });

}

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public void filterList(List<GroupChatListModel> list) {
        mChatList = list;
        notifyDataSetChanged();
    }

    private void deleteNode(String Key,int position) {
        mChatList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChatList.size());

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
