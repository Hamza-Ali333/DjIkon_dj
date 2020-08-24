package com.example.djikon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;


public class BlogFragment extends Fragment {

    private EditText edt_Title, edt_Discription;
    private ImageView img_Video, img_Gallery, img_Camera, img_Featured, img_Audio;
    private Button btn_Post;

    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int STORAFGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    String cameraPermission[];
    String storagePermission[];
    Uri Image_uri;

    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_blog,container,false);
       createRefrences(v);

        //camerapermission
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storagepermission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Ask for Required Permissions
        final String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE
        };

        ActivityCompat.requestPermissions((Activity) getContext(), permissions, 123);

        img_Audio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showImageImportDailog();
           }
       });
       img_Camera.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showImageImportDailog();
           }
       });


       return v;
    }

    private void createRefrences(View v){
        edt_Title= v.findViewById(R.id.txt_blog_title);
//        edt_Discription = v.findViewById(R.id.txt_blog_discription);

          img_Camera = v.findViewById(R.id.camera);
          img_Featured = v.findViewById(R.id.featuredimg);
          img_Gallery = v.findViewById(R.id.imgGallery);
          img_Audio = v.findViewById(R.id.imgAudio);
    }
    private void showImageImportDailog(){

        String[] items ={"Camera","Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(getActivity());
        dailog.setTitle("Select Image");
        dailog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 1){
                    //camera run
                    if(!checkCameraPermissions()){
                        //RequestCamera Permission
                        requestPermissionCamera();
                    }
                }else {
                    //pick Camera
                    pickCamera();
                }
                if(which == 0){
                    //open Gallary
                    if(!checkStoragePermissions()){
                        //RequestStorage Permission
                        requestPermissionStorage();
                    }
                }else {
                    //pick Gallary
                    pickGallary();
                }
            }
        });
        dailog.create().show();
    }

    private boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);


        return result && result1;
    }

    private boolean checkStoragePermissions(){
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);


        return result;
    }

    private void requestPermissionCamera(){
        ActivityCompat.requestPermissions(getActivity(),cameraPermission,CAMERA_REQUEST_CODE);
    }

    private void requestPermissionStorage(){
        ActivityCompat.requestPermissions(getActivity(),storagePermission,STORAFGE_REQUEST_CODE);
    }

    private void pickCamera(){
        //Intent to take Image for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pic");//title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");//discription of the picture
        Image_uri = getActivity().getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickGallary(){
        //  intent for the Image from Gallary
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery,IMAGE_PICK_GALLARY_REQUEST_CODE);
    }


    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickCamera();
                    }else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAFGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickGallary();
                    }else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    //Handle Image Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //get selected image Image
        if(resultCode ==RESULT_OK) {
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){

                CropImage.activity(Image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
            //from gallary
            if(requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
            //getcroped Image
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();


            }
        }else {
            Toast.makeText(getContext(), "Image is not Selected", Toast.LENGTH_SHORT).show();
        }


    }//onActivity Result


}
