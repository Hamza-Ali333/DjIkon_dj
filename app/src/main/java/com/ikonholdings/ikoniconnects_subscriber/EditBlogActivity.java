package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PathUtil;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.GalleryImagesUri;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditBlogActivity extends AppCompatActivity {


    private Button btn_UpDatePost;
    private ImageView img_Camera, img_Featured;
    private EditText edt_Title, edt_Discription;

    private Uri Image_uri;
    private Bitmap bitmap;

    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_blog_activity);
        createRefrences();
        btn_UpDatePost.setText("Update Post");

        img_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageImagePicker();
            }
        });

        img_Featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageImagePicker();
            }
        });

        btn_UpDatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditBlogActivity.this, MainActivity.class));
            }
        });

    }
    private void manageImagePicker() {
        if ( PermissionHelper.checkDefaultPermissions(this)) {
            showImageImportDailog();
        }else {
            PermissionHelper.managePermissions(this);
        }
    }

    //image import
    private void showImageImportDailog() {
        String[] items = {"Camera", "Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(this);
        dailog.setTitle("Select Image");
        dailog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {
                    //pick Gallary
                    pickGallary();
                }
                if (which == 0) {
                    //pick Camera
                    pickCamera();
                }
            }
        });
        dailog.create().show();
    }

    private void pickCamera(){
        //Intent to take Image for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pic");//title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");//discription of the picture
        Image_uri = this.getContentResolver()
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

    private void createRefrences() {
        edt_Title= v.findViewById(R.id.txt_blog_title);
        edt_Discription = v.findViewById(R.id.txt_blog_discription);

        btn_UpDatePost = findViewById(R.id.btn_publish);
        img_Camera = findViewById(R.id.camera);
        img_Featured = findViewById(R.id.featuredimg);
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
                        .start( this);
            }
            //from gallary
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start( this);
            }

            //getcroped Image
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                img_Featured.setImageURI(Image_uri);

                //Image_uri = data.getData();//data is getting null have to check
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Image_uri);
                    //new UploadBlogToServer().execute();
                    //img_Profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "onActivityResult: "+e.getMessage());
                }

            }
        }else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

    private class UploadBlogToServer extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(EditBlogActivity.this,
                    "Uploading","Please wait. While uploading blog on Server");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(EditBlogActivity.this);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            File profileFile = new File(Image_uri.getPath());
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("photo",profileFile.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), profileFile));
            String artistName= PreferenceData.getUserName(EditBlogActivity.this);
            RequestBody artist_name = RequestBody.create(MediaType.parse("text/plain"),
                    artistName);

            String name = edt_Title.getText().toString();
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    name);

            String descript = edt_Discription.getText().toString();
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),
                    descript);

            Call<SuccessErrorModel> call = jsonApiHolder.AddBlog(
                    profileImage,
                    artist_name,
                    artist_name,
                    title,
                    description
            );

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                DialogsUtils.showSuccessDialog(EditBlogActivity.this,
                                        "Uploaded Successfully","Your blog is uploaded successfully");
                            } else {
                                DialogsUtils.showAlertDialog(EditBlogActivity.this,false,
                                        "Uploaded Failed","Please try again. Blog uploading is failed\n" +
                                                "And make sure you have strong internet connection");
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogsUtils.showResponseMsg(EditBlogActivity.this,true);
                        }
                    });
                }
            });
            return null;
        }
    }

}