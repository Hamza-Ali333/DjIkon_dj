package com.Ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.Activities.BlogDetailActivity;
import com.Ikonholdings.ikoniconnects_subscriber.Activities.EditBlogActivity;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.MyFeedBlogModel;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerMyFeed extends RecyclerView.Adapter<RecyclerMyFeed.ViewHolder> {

    private List<MyFeedBlogModel> mBlogs;
    private Context context;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes, img_menu;

        public ProgressBar progressBarProfile, progressBarFeed;

        public TextView txt_BlogTitle, txt_uploadTime, txt_Description, txt_LikesNo, txt_ChatNo;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBarProfile = itemView.findViewById(R.id.progressBarProfile);
            progressBarFeed = itemView.findViewById(R.id.progressBarFead);

            img_uploaderProfile = itemView.findViewById(R.id.img_uploaderImage);
            img_feedImage = itemView.findViewById(R.id.img_feedImage);
            img_Likes = itemView.findViewById(R.id.img_likes);
            img_Chat = itemView.findViewById(R.id.img_chat);
            img_menu = itemView.findViewById(R.id.menu);

            txt_BlogTitle = itemView.findViewById(R.id.txt_uploaderName);
            txt_uploadTime = itemView.findViewById(R.id.txt_uploadTime);
            txt_Description = itemView.findViewById(R.id.txt_imgDescription);
            txt_LikesNo = itemView.findViewById(R.id.txt_LikesNo);
            txt_ChatNo = itemView.findViewById(R.id.txt_chatNo);
        }


    }

    //constructor
    public RecyclerMyFeed(List<MyFeedBlogModel> blogs, Context context) {
        this.context = context;
        this.mBlogs = blogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_feeds_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyFeedBlogModel currentItem = mBlogs.get(position);

        holder.img_feedImage.setImageResource(R.drawable.ic_avatar);

        holder.txt_BlogTitle.setText(currentItem.getTitle());
        holder.txt_uploadTime.setText(currentItem.getCreated_at());
        holder.txt_Description.setText(currentItem.getDescription());
        holder.txt_LikesNo.setText(String.valueOf(currentItem.getLikes()));
        holder.txt_ChatNo.setText(String.valueOf(currentItem.getComments()));

//featured Image
        if (!currentItem.getPhoto().isEmpty()) {
            holder.progressBarFeed.setVisibility(View.VISIBLE);
            Picasso.get().load((ApiClient.Base_Url+currentItem.getPhoto()))
                    .into(holder.img_feedImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarFeed.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarFeed.setVisibility(View.GONE);
                        }
                    });
        }

        //Artist Image
            if (!currentItem.getArtist_image().isEmpty() && !currentItem.getArtist_image().equals("no")) {
                holder.progressBarProfile.setVisibility(View.VISIBLE);
                Picasso.get().load(ApiClient.Base_Url+currentItem.getArtist_image())
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.img_uploaderProfile, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBarProfile.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                holder.progressBarProfile.setVisibility(View.GONE);
                            }
                        });

                //0 means unlik
                if(currentItem.getLike_status()==0){
                    holder.img_Likes.setImageResource(R.drawable.ic_unlike);
                }else {
                    holder.img_Likes.setImageResource(R.drawable.ic_heart_fill);
                }


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
                            switch (item.getItemId()) {
                                case R.id.add:
                                        Intent i = new Intent(v.getContext(), EditBlogActivity.class);
                                        i.putExtra("url", currentItem.getPhoto());
                                        i.putExtra("id", String.valueOf(currentItem.getId()));
                                        i.putExtra("title",holder.txt_BlogTitle.getText().toString());
                                        i.putExtra("description",holder.txt_Description.getText().toString());
                                        v.getContext().startActivity(i);
                                    break;
                                case R.id.delete:
                                    new DeleteBlog(position,currentItem.getId()).execute();
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

                    //custom popupmenu
                    //initiatePopupWindow(context,holder);
                }
            });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), BlogDetailActivity.class);
                        i.putExtra("url",currentItem.getId());
                        i.putExtra("featured_image",currentItem.getPhoto());
                        view.getContext().startActivity(i);
                    }
                });

            holder.img_Chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

    private class DeleteBlog extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        int position;
        int BlogId;


        public DeleteBlog(int position,int BlogId) {
            this.position = position;
            this.BlogId = BlogId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(context,
                    "Working...",
                    "Please wait. While connecting with the server.");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SuccessErrorModel> call = jsonApiHolder.deleteBlog(
                    BlogId
            );
            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                        mBlogs.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mBlogs.size());
                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Please try again and check your internet connection");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "No Server Connection",
                                    t.getMessage());
                        }
                    });
                }
            });
            return null;
        }
    }

}