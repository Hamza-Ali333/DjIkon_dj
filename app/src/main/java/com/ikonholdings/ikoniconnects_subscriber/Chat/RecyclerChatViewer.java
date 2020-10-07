package com.ikonholdings.ikoniconnects_subscriber.Chat;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.OneToOneChatModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerChatViewer extends RecyclerView.Adapter<RecyclerChatViewer.ViewHolder>{

    private List<OneToOneChatModel> mChat_model;
    public  String currentSubscriberUid;

    public DatabaseReference myRef;

    public static final int MSG_TYPE_RIGHT = 0;
    public static final int MSG_TYPE_LEFT = 1;
    public Boolean sender = false;

    public String senderImage, receiverImage;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Profile;
        public TextView txt_msg, txt_Time;
        public RelativeLayout rlt_ChatItem;

        public ViewHolder(View itemView){
            super(itemView);
            img_Profile = itemView.findViewById(R.id.profile_image);
            txt_Time = itemView.findViewById(R.id.time);
            txt_msg = itemView.findViewById(R.id.msg);


            rlt_ChatItem = itemView.findViewById(R.id.rlt_chatitem);
        }
    }

    //constructor
    public RecyclerChatViewer(List<OneToOneChatModel> chat_modelList,
                              String currentSubscriberUid,
                              String chatMainNode,
                              String senderimg,
                              String recieverimg) {
        this.mChat_model = chat_modelList;
        this.currentSubscriberUid = currentSubscriberUid;
        this.senderImage = senderimg;
        this.receiverImage = recieverimg;
        myRef = FirebaseDatabase.getInstance().getReference("Chats").child("Massages").child(chatMainNode);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v;
        if (viewType == MSG_TYPE_RIGHT) {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
        }else {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OneToOneChatModel currentItem = mChat_model.get(position);

        holder.txt_msg.setText(currentItem.getMessage());
        holder.txt_Time.setText(currentItem.getTime_stemp());
        String imageUrl = null;
        if(sender){
            imageUrl = senderImage;
        }else {
            imageUrl = receiverImage;
        }
        if(imageUrl != null){
            Picasso.get().load((ApiClient.Base_Url+imageUrl))
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_Profile);
        }

        //image setting remaining

/*       holder.rlt_ChatItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

//               Intent i = new Intent(view.getContext(),ChatViewerActivity.class);
//               i.putExtra("email",currentItem.getMsg_Sender_Name());
//               view.getContext().startActivity(i);

           }
       });*/

        holder.rlt_ChatItem.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.rlt_ChatItem);
                popupMenu.inflate(R.menu.chat_option);
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                deleteNode(currentItem.getKey(),position);

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
    public int getItemViewType(int position) {
        if(mChat_model.get(position).getSender().equals(currentSubscriberUid)){
            sender = true;
            return MSG_TYPE_RIGHT;
        }else {
            sender = false;
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChat_model.size();
    }


    private void deleteNode(String Key,int position) {
        mChat_model.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChat_model.size());

        myRef.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //note is not deleted
            }
        });
    }
}