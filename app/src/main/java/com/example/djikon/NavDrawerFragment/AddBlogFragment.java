package com.example.djikon.NavDrawerFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.PathUtil;
import com.example.djikon.Models.GalleryImagesUri;
import com.example.djikon.RecyclerView.RecyclerShowGalleryImages;
import com.example.djikon.ResponseModels.SuccessErrorModel;
import com.example.djikon.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddBlogFragment extends Fragment {

    private EditText edt_Title, edt_Discription;
    private ImageView img_Video, img_Gallery, img_Camera, img_Featured, img_Audio, img_Selected;
    private Button btn_Post;

    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 1000;
    private static final int MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE = 3784;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2342;
    private static final int REQUEST_TAKE_Audio = 1376;

    private String cameraPermission[];
    private String storagePermission[];
    private Uri Image_uri;
    private Bitmap bitmap;

    private AlertDialog alertDialog;

    private RecyclerView mGalleryRecycler;
    private RecyclerView.Adapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    //here Store All
    private  List<GalleryImagesUri> GalleryArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_blog,container,false);
       createRefrences(v);

        GalleryArray = new ArrayList<>();

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

       img_Gallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent();
               i.setType("image/*");
               //i.setType("video/*");
               i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
               i.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(
                       Intent.createChooser(i, "android.intent.action.SEND_MULTIPLE"), MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE);
           }
       });

       img_Video.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();
               intent.setType("video/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
           }
       });

       img_Audio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent_upload = new Intent();
               intent_upload.setType("audio/*");
               intent_upload.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(intent_upload,REQUEST_TAKE_Audio);
           }
       });

       btn_Post.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new UploadBlogToServer().execute();
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
          img_Video = v.findViewById(R.id.imgVideo);
          img_Selected = v.findViewById(R.id.image);

        mGalleryRecycler = v.findViewById(R.id.gallryIamgesRecycler);

        btn_Post = v.findViewById(R.id.btn_publish);
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
        ActivityCompat.requestPermissions(getActivity(),storagePermission, STORAGE_REQUEST_CODE);
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
        startActivityForResult(gallery, IMAGE_PICK_GALLERY_REQUEST_CODE);
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

            case STORAGE_REQUEST_CODE:
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
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }

            //getcroped Image
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                img_Featured.setImageURI(Image_uri);

                //Image_uri = data.getData();//data is getting null have to check
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Image_uri);
                    //new UploadBlogToServer().execute();
                    //img_Profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "onActivityResult: "+e.getMessage());
                }


            }

            //video
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getVideoPath(selectedImageUri);
                if (selectedImagePath != null) {

//                    Intent intent = new Intent(HomeActivity.this,
//                            VideoplayAvtivity.class);
//                    intent.putExtra("path", selectedImagePath);
//                    startActivity(intent);
                }
            }

            //audio
            if (requestCode == REQUEST_TAKE_Audio){
                Uri uri = data.getData();
                try {
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    //    String path = myFile.getAbsolutePath();
                    String displayName = null;
                    String path2 = getAudioPath(uri);
                    File f = new File(path2);
                    long fileSizeInBytes = f.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB > 8) {
                        //customAlterDialog("Can't Upload ", "sorry file size is large");
                    } else {
                       // profilePicUrl = path2;
                        //isPicSelect = true;
                    }
                } catch (Exception e) {
                    //handle exception
                    Toast.makeText(getContext(), "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
                String path1 = uri.getPath();
            }

                    //Multiple Images For BlogGallery
            if(requestCode == MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE){

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                       GalleryArray.add(new GalleryImagesUri(item.getUri()));
                    }
                    buildGalleryImagesRecycler(GalleryArray);
                } else  {
                    Toast.makeText(getContext(), "No Image Found", Toast.LENGTH_SHORT).show();
                }

               //As of now use static position 0 use as per itemcount.
//                Bitmap bitmap = null;
//                //Uri selectedImage1 = data.getData();
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("+++ clipdate" + selectedImage);
            }

        }else {
            Toast.makeText(getContext(), "Image is not Selected", Toast.LENGTH_SHORT).show();
        }


    }//onActivity Result


    // UPDATED!
    public String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

   // This function is use for absolute path of audio file
    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void buildGalleryImagesRecycler(List<GalleryImagesUri> galleryImagesUriList) {

        mGalleryRecycler.setHasFixedSize(true);//if the recycler view not increase run time
        galleryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        galleryAdapter = new RecyclerShowGalleryImages(galleryImagesUriList);

        mGalleryRecycler.setLayoutManager(galleryLayoutManager);
        mGalleryRecycler.setAdapter(galleryAdapter);
    }

    private class UploadBlogToServer extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            List<MultipartBody.Part> galleryImages = new ArrayList<>();

            for (int i = 0; i < GalleryArray.size(); i++) {
                Uri uri = GalleryArray.get(i).getUri();
                try {
                    File file =new File(PathUtil.getPath(getContext(),uri));
                    galleryImages.add(MultipartBody.Part.createFormData("image","imageName",
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Log.i("TAG", "doInBackground: Catch " +e.getMessage());
                }

            }

            File profileFile = new File(Image_uri.getPath());
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("image","imageName",
                    RequestBody.create(MediaType.parse("multipart/form-data"), profileFile));

            RequestBody artist_name = RequestBody.create(MediaType.parse("text/plain"),
                    "Subscriber 1");
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    "My First Belog");

            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),
                    "This si my first Belog from mobile");

            Call<SuccessErrorModel> call = jsonApiHolder.AddBlog(
                    profileImage,
                    galleryImages,
                    artist_name,
                    title,
                    description
            );

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Successfull", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "onResponse: Uploaded Successfully");
                            } else {
                                Toast.makeText(getContext(), "Failed "+response.code(), Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "onResponse: Uploading Failed");
                            }
                        }
                    });

                }
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i("TAG", "onResponse: Uploading Error  "+ t.getMessage());
                }
            });
            return null;
        }
    }

}
