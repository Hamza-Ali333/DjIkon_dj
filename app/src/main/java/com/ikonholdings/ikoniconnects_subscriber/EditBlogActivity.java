package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditBlogActivity extends AppCompatActivity {

    private Button btn_UpDatePost;
    private ImageView img_Featured;
    private EditText edt_Title, edt_Description;

    private Uri Image_uri;

    private ProgressBar progressBarFeed;

    private String blogId;
    private Boolean profileChange = false;

    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_blog_activity);
        createReferences();
        btn_UpDatePost.setText("Update Post");

        Intent i = getIntent();
        String Title = i.getStringExtra("title");
        String Description = i.getStringExtra("description");
        String Url = i.getStringExtra("url");
        blogId = i.getStringExtra("id");

        Picasso.get().load(ApiClient.Base_Url + Url)
                .fit()
                .centerCrop()
                .into(img_Featured, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarFeed.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBarFeed.setVisibility(View.GONE);
                    }
                });

        edt_Title.setText(Title);
        edt_Description.setText(Description);

        img_Featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageImagePicker();
            }
        });

        btn_UpDatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_Description.getText().toString().equals(Description) && edt_Title.getText().toString().equals(Title) && profileChange == false) {
                    Toast.makeText(EditBlogActivity.this, "Already Update", Toast.LENGTH_SHORT).show();
                } else {
                    new UploadBlogToServer(edt_Title.getText().toString().trim(),
                            edt_Description.getText().toString().trim()).execute();
                }
            }
        });

    }

    private void manageImagePicker() {
        if (PermissionHelper.checkDefaultPermissions(this)) {
            showImageImportDailog();
        } else {
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

    private void pickCamera() {
        //Intent to take Image for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic");//title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");//discription of the picture
        Image_uri = this.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickGallary() {
        //  intent for the Image from Gallary
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    private void createReferences() {
        edt_Title = findViewById(R.id.txt_blog_title);
        edt_Description = findViewById(R.id.txt_blog_discription);

        btn_UpDatePost = findViewById(R.id.btn_publish);
        img_Featured = findViewById(R.id.featuredimg);

        progressBarFeed = findViewById(R.id.progressBarFeed);
    }


    //Handle Image Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get selected image Image
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                CropImage.activity(Image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            //from gallary
            if (requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            //getcroped Image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                img_Featured.setImageURI(Image_uri);
                profileChange = true;

            }
        } else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

    private class UploadBlogToServer extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String Title, Description;

        public UploadBlogToServer(String Title, String Description) {
            this.Title = Title;
            this.Description = Description;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(EditBlogActivity.this,
                    "Uploading", "Please wait. While uploading blog on Server");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(EditBlogActivity.this);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            MultipartBody.Part profileImage = null;

            if (Image_uri != null) {
                File profileFile = new File(Image_uri.getPath());
                profileImage = MultipartBody.Part.createFormData("photo", profileFile.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), profileFile));
            }

            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    Title);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),
                    Description);

            Call<SuccessErrorModel> call = jsonApiHolder.updateBlog(
                    "updateBlog/" + blogId,
                    profileImage,
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
                                        "Uploaded Successfully", "Your blog is uploaded successfully");
                            } else {
                                DialogsUtils.showAlertDialog(EditBlogActivity.this, false,
                                        "Uploaded Failed", "Please try again. Blog uploading is failed\n" +
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
                            DialogsUtils.showResponseMsg(EditBlogActivity.this, true);
                        }
                    });
                }
            });
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}