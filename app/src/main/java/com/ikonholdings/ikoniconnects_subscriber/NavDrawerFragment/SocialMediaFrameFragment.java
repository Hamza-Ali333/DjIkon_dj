package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.ImageFrame_Model;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerImageFrame;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerSocialMediaFrames;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FramesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SocialMediaFrameFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<FramesModel> listOFImagesName;
    private AlertDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_social_media_fram,container,false);
       mRecyclerView = v.findViewById(R.id.recycler_frame);
       loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
       getFrames();

       return v;
    }

    private void getFrames(){
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<FramesModel>> call = jsonApiHolder.getFrames();

        call.enqueue(new Callback<List<FramesModel>>() {
            @Override
            public void onResponse(Call<List<FramesModel>> call, Response<List<FramesModel>> response) {

                if(response.isSuccessful()){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    List<FramesModel> framesModelList= response.body();
                    loadingDialog.dismiss();
                    if(framesModelList.isEmpty()) {
                        //if no data then show dialoge to user
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "No Frames","it's seems like you did't upload any frame png\nfor you followers");
                    } else{

                        sperationOfArray(framesModelList);
                    }

                }else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<FramesModel>> call, Throwable t) {
                loadingDialog.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });
    }

    private void sperationOfArray(List<FramesModel> list){
        for (int i = 0; i < list.size() ; i++) {
            String Gallery= list.get(i).getFrame();
            Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
            String[] GalleryArray = Gallery.split(",");

            for (int j = 0; j <= GalleryArray.length - 1; j++) {
                listOFImagesName.add(new FramesModel(ApiClient.Base_Url+"post_images/" + GalleryArray[j]));
            }
        }

        buildRecyclerView(listOFImagesName);
    }

    private void buildRecyclerView(List<FramesModel> imageslist){

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mAdapter = new RecyclerSocialMediaFrames(imageslist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
