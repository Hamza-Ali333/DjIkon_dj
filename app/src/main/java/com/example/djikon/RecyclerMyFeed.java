package com.example.djikon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMyFeed extends RecyclerView.Adapter<RecyclerMyFeed.ViewHolder>{

    private ArrayList<MyFeed_Model> mLatestFeedItemArrayList;
    private Context context;


    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes, img_menu;


        public TextView txt_uploaderName, txt_uploadTime, txt_Description, txt_ReadMore, txt_LikesNo, txt_ChatNo;

        public ViewHolder(View itemView){
            super(itemView);
            img_uploaderProfile = itemView.findViewById(R.id.img_uploaderImage);
            img_feedImage = itemView.findViewById(R.id.img_feedImage);
            img_Likes = itemView.findViewById(R.id.img_likes);
            img_Chat = itemView.findViewById(R.id.img_chat);
            img_menu = itemView.findViewById(R.id.menu);



            txt_uploaderName = itemView.findViewById(R.id.txt_uploaderName);
            txt_uploadTime = itemView.findViewById(R.id.txt_uploadTime);
            txt_Description = itemView.findViewById(R.id.txt_imgDescription);
            txt_ReadMore = itemView.findViewById(R.id.txt_ReadMore);
            txt_LikesNo = itemView.findViewById(R.id.txt_LikesNo);
            txt_ChatNo = itemView.findViewById(R.id.txt_chatNo);
        }


    }


//constructor
    public RecyclerMyFeed(ArrayList<MyFeed_Model> latestFeedItemArrayList,Context context) {
        this.mLatestFeedItemArrayList = latestFeedItemArrayList;
        this.context =context;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_feeds_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyFeed_Model currentItem = mLatestFeedItemArrayList.get(position);

        holder.img_uploaderProfile.setImageResource(currentItem.getImg_UploaderProfile());
        holder.img_feedImage.setImageResource(currentItem.getImg_FeedImage());

        holder.txt_uploaderName.setText(currentItem.getTxt_UploaderName());
        holder.txt_uploadTime.setText(currentItem.getTxt_UploadTime());
        holder.txt_Description.setText(currentItem.getTxt_Description());
        holder.txt_LikesNo.setText(currentItem.getTxt_LikesNo());
        holder.txt_ChatNo.setText(currentItem.getTxt_ChatNo());

        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceType")
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.img_menu);
                popupMenu.inflate(R.menu.feedoptions);
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Intent i = new Intent(v.getContext(), EditBlogActivity.class);
                                v.getContext().startActivity(i);
                                break;
                            case R.id.delete:
                                Toast.makeText(v.getContext(), "Delete Option Will Available Soon", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                //custom popupmenu
              // initiatePopupWindow(context,holder);
            }
        });



        holder.img_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CommnetActivity.class);
                v.getContext().startActivity(i);
            }
        });
//
//        holder.img_feedImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), DjPrpfileActivity.class);
//                v.getContext().startActivity(i);
//            }
//        });

}




    @Override
    public int getItemCount() {
        return mLatestFeedItemArrayList.size();
    }



    private PopupWindow initiatePopupWindow(Context context,ViewHolder viewHolder) {

        try {

            mInflater = (LayoutInflater) context.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.menu, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            //final TextView itema = (TextView) layout.findViewById(R.id.ItemA);


           // final TextView itemb = (TextView) layout.findViewById(R.id.ItemB);



            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout,FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
            Drawable background = context.getResources().getDrawable(android.R.drawable.menuitem_background);
            mDropdown.setBackgroundDrawable(background);
//            mDropdown.showAtLocation();
            mDropdown.showAsDropDown(viewHolder.img_menu, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }



}
