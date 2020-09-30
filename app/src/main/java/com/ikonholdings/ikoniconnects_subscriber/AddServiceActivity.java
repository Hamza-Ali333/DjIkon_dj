package com.ikonholdings.ikoniconnects_subscriber;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PathUtil;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerShowGalleryImages;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.GalleryImagesUri;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
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

public class AddServiceActivity extends AppCompatActivity {

    private ImageView Featured_Image;
    private Spinner priceType;
    private EditText edt_Title, edt_Description, edt_Price;

    private Button btn_Publish, btn_Gallery;

    private String[] rateType = {"Select type", "Fix", "Rate per hour"};//for sippiner adapter
    private String SelectedPriceType = "Select Type";

    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 1000;
    private static final int MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE = 3784;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    private Uri Image_uri;
    private Boolean isFeatureImageSelected = false;
    private ProgressBar progressBar;

    private RecyclerView mGalleryRecycler;
    private RecyclerView.Adapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    //here Store All
    private  List<GalleryImagesUri> GalleryArray;

    private Boolean edit;
    private int ServiceId;

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
        setContentView(R.layout.activity_add_service);
        getSupportActionBar().setTitle("Add Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();
        GalleryArray = new ArrayList<>();

        Intent i = getIntent();
        edit = i.getBooleanExtra("edit",false);

        if(edit){
            isFeatureImageSelected = true;
            btn_Gallery.setVisibility(View.GONE);

            ServiceId = i.getIntExtra("id",0);
            edt_Title.setText(i.getStringExtra("title"));
            edt_Description.setText(i.getStringExtra("description"));
            edt_Price.setText(i.getStringExtra("price"));


            if(i.getStringExtra("price_type").equals("Fix")){
                priceType.setSelection(1);
            }else {
                priceType.setSelection(2);
            }

            if(i.getStringExtra("image") != null){
                progressBar.setVisibility(View.VISIBLE);
                Picasso.get().load(ApiClient.Base_Url + i.getStringExtra("image"))
                        .fit()
                        .centerCrop()
                        .into(Featured_Image, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }//if service is not equal to null

        }else {
            btn_Gallery.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_gender_spinner, R.id.genders, rateType);

        priceType.setAdapter(adapter);
        priceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedPriceType = rateType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Featured_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageImagePicker();
            }
        });

        btn_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( PermissionHelper.checkDefaultPermissions(AddServiceActivity.this)) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(i, "android.intent.action.SEND_MULTIPLE"), MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE);
                }else {
                    PermissionHelper.managePermissions(AddServiceActivity.this);
                }
            }
        });

        btn_Publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInfoRight()){
                        new UploadServiceToServer(edt_Title.getText().toString().trim(),
                                edt_Description.getText().toString().trim(),
                                edt_Price.getText().toString().trim())
                                .execute();
                }
            }
        });
    }

    private void manageImagePicker() {
        if ( PermissionHelper.checkDefaultPermissions(AddServiceActivity.this)) {
            showImageImportDailog();
        }else {
            PermissionHelper.managePermissions(AddServiceActivity.this);
        }
    }

    private void createReferences () {
        progressBar = findViewById(R.id.progressBarProfile);
        btn_Publish = findViewById(R.id.btn_publish);
        btn_Gallery = findViewById(R.id.btn_add_event_images);
        Featured_Image = findViewById(R.id.feature_img);
        edt_Title = findViewById(R.id.edt_service_title);
        edt_Description = findViewById(R.id.edt_service_description);
        edt_Price = findViewById(R.id.edt_service_rate);

        priceType = findViewById(R.id.ratetype);
        mGalleryRecycler = findViewById(R.id.gallryIamgesRecycler);
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

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200){
            if (grantResults.length > 0) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (!cameraAccepted || !storageAccepted) {
                    PermissionHelper.showPermissionAlert(AddServiceActivity.this);
                }
            }
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
                Featured_Image.setImageURI(Image_uri);
                isFeatureImageSelected = true;
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
                    Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
                }

            }

        }else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

    private void buildGalleryImagesRecycler(List<GalleryImagesUri> galleryImagesUriList) {
        mGalleryRecycler.setVisibility(View.VISIBLE);
        mGalleryRecycler.setHasFixedSize(true);//if the recycler view not increase run time
        galleryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryAdapter = new RecyclerShowGalleryImages(galleryImagesUriList);
        new RecyclerShowGalleryImages(galleryImagesUriList);
        ((RecyclerShowGalleryImages) galleryAdapter).setOnItemClickListner(new RecyclerShowGalleryImages.onItemClickListner() {
            @Override
            public void onClick(Integer position) {
                if(position != null){
                    GalleryArray.remove(position);
                }
                else
                    Toast.makeText(AddServiceActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        mGalleryRecycler.setLayoutManager(galleryLayoutManager);
        mGalleryRecycler.setAdapter(galleryAdapter);
    }

    private Boolean isInfoRight () {
        Boolean result = true;
        if(!isFeatureImageSelected){
            Toast.makeText(this, "Featured image is required", Toast.LENGTH_SHORT).show();
            result = false;
        }else if(edt_Title.getText().toString().isEmpty()){
            edt_Title.setError("Required");
            edt_Title.requestFocus();
            result = false;
        }else if(edt_Price.getText().toString().isEmpty()){
            edt_Price.setError("Required");
            edt_Price.requestFocus();
            result = false;
        }else if(SelectedPriceType.equals("Select Type")){
            Toast.makeText(this, "Please select service price charge type", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    private class UploadServiceToServer extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;
        String Title, Description, Price;

        public UploadServiceToServer(String Title, String Description,String Price) {
            this.Price = Price;
            this.Title = Title;
            this.Description = Description;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(AddServiceActivity.this,
                    "Uploading","Please wait. While uploading service on Server");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(AddServiceActivity.this);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            List<MultipartBody.Part> galleryImages = new ArrayList<>();

            for (int i = 0; i < GalleryArray.size(); i++) {
                Uri uri = GalleryArray.get(i).getUri();
                try {
                    File file =new File(PathUtil.getPath(AddServiceActivity.this,uri));

                    galleryImages.add(MultipartBody.Part.createFormData("gallery[]",file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Log.i("TAG", "doInBackground: Catch " +e.getMessage());
                }

            }

            MultipartBody.Part profileImage = null;
            if(Image_uri != null){
                File profileFile = new File(Image_uri.getPath());
                profileImage = MultipartBody.Part.createFormData("feature_image",profileFile.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), profileFile));
            }

            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    Title);

            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),
                    Description);

            RequestBody price = RequestBody.create(MediaType.parse("text/plain"),
                    Price);

            RequestBody priceType = RequestBody.create(MediaType.parse("text/plain"),
                    SelectedPriceType);
            Call<SuccessErrorModel> call= null;

            if(edit){
                call = jsonApiHolder.updateService(
                        "updateService/"+ ServiceId,
                        profileImage,
                        title,
                        description,
                        priceType,
                        price
                );
            }else {
                call = jsonApiHolder.uploadService(
                        profileImage,
                        galleryImages,
                        title,
                        description,
                        priceType,
                        price
                );
            }

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                DialogsUtils.showSuccessDialog(AddServiceActivity.this,
                                        "Uploaded Successfully","Your Service is uploaded successfully");
                            } else {
                                DialogsUtils.showAlertDialog(AddServiceActivity.this,false,
                                        "Uploaded Failed","Please try again. uploading is failed\n" +
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
                            DialogsUtils.showResponseMsg(AddServiceActivity.this,true);
                        }
                    });
                }
            });
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}