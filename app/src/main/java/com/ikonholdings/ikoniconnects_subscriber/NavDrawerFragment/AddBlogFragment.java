package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PathUtil;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerShowGalleryImages;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.GalleryImagesUri;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.SuccessErrorModel;
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

import static android.app.Activity.RESULT_OK;

public class AddBlogFragment extends Fragment {

    private EditText edt_Title, edt_Description;
    private ImageView img_Video, img_Gallery, img_Camera, img_Featured, img_Selected;
    private Button btn_Post;
    private VideoView videoView;

    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 1000;
    private static final int MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE = 3784;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2342;

    private Uri Image_uri;
    private Boolean isFeatureImageSelected= false;

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

       img_Gallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if ( PermissionHelper.checkDefaultPermissions(getActivity())) {
                   Intent i = new Intent();
                   i.setType("image/*");
                   i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                   i.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(
                           Intent.createChooser(i, "android.intent.action.SEND_MULTIPLE"), MULTIPLE_IMAGE_PICK_GALLERY_REQUEST_CODE);
               }else {
                   PermissionHelper.managePermissions(getActivity());
               }
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

       btn_Post.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isInfoRight()){
                   new UploadBlogToServer().execute();
               }

           }
       });

       return v;
    }

    private void manageImagePicker() {
        if ( PermissionHelper.checkDefaultPermissions(getActivity())) {
            showImageImportDailog();
        }else {
            PermissionHelper.managePermissions(getActivity());
        }
    }

    private void createRefrences(View v){
        edt_Title= v.findViewById(R.id.txt_blog_title);
        edt_Description = v.findViewById(R.id.txt_blog_discription);

          img_Camera = v.findViewById(R.id.camera);
          img_Featured = v.findViewById(R.id.featuredimg);
          img_Gallery = v.findViewById(R.id.imgGallery);
          img_Video = v.findViewById(R.id.imgVideo);
          img_Selected = v.findViewById(R.id.image);

          mGalleryRecycler = v.findViewById(R.id.gallryIamgesRecycler);

        btn_Post = v.findViewById(R.id.btn_publish);
        videoView = v.findViewById(R.id.videoView2);
    }

    private Boolean isInfoRight () {
        Boolean result = true;
        if(!isFeatureImageSelected){
            Toast.makeText(getContext(), "Featured image is required", Toast.LENGTH_SHORT).show();
            result = false;
        }else if(edt_Title.getText().toString().isEmpty()){
            edt_Title.setError("Required");
            edt_Title.requestFocus();
            result = false;
        }
        return result;
    }

    //image import
    private void showImageImportDailog() {
        String[] items = {"Camera", "Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(getContext());
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
        if(requestCode == 200){
            if (grantResults.length > 0) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (!cameraAccepted || !storageAccepted) {
                    PermissionHelper.showPermissionAlert(getContext());
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
                        .setCropMenuCropButtonTitle("Select")
                        .start(getContext(), this);
            }
            //from gallary
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Select")
                        .start(getContext(), this);
            }

            //getcroped Image
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                img_Featured.setImageURI(Image_uri);
                isFeatureImageSelected = true;

            }

            //video
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                String file = null;
                try {
                    file = PathUtil.getPath(getContext(), selectedImageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                // MEDIA GALLERY
                String selectedImagePath = getVideoPath(selectedImageUri);
                if (selectedImageUri != null) {
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(selectedImageUri);

                    //media controller
                    MediaController vidControl = new MediaController(getContext());
                    vidControl.setAnchorView(videoView);
                    videoView.setMediaController(vidControl);

                } else {
                    videoView.setVisibility(View.GONE);
                }
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

    private void buildGalleryImagesRecycler(List<GalleryImagesUri> galleryImagesUriList) {

        mGalleryRecycler.setHasFixedSize(true);//if the recycler view not increase run time
        galleryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        galleryAdapter = new RecyclerShowGalleryImages(galleryImagesUriList);
        new RecyclerShowGalleryImages(galleryImagesUriList);
        ((RecyclerShowGalleryImages) galleryAdapter).setOnItemClickListner(new RecyclerShowGalleryImages.onItemClickListner() {
            @Override
            public void onClick(Integer position) {
                if(position != null){
                    GalleryArray.remove(position);
                }
                else
                    Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        mGalleryRecycler.setLayoutManager(galleryLayoutManager);
        mGalleryRecycler.setAdapter(galleryAdapter);
    }

    private class UploadBlogToServer extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String Title, Description;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(getContext(),
                    "Uploading", "Please wait. While uploading blog on Server");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            List<MultipartBody.Part> galleryImages = new ArrayList<>();

            for (int i = 0; i < GalleryArray.size(); i++) {
                Uri uri = GalleryArray.get(i).getUri();
                try {
                    File file =new File(PathUtil.getPath(getContext(),uri));
                    
                    galleryImages.add(MultipartBody.Part.createFormData("gallery[]",file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Log.i("TAG", "doInBackground: Catch " +e.getMessage());
                }

            }

            File profileFile = new File(Image_uri.getPath());
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("photo",profileFile.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), profileFile));

            String name = edt_Title.getText().toString();
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    name);

            String descript = edt_Description.getText().toString();
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),
                    descript);

            Call<SuccessErrorModel> call = jsonApiHolder.uploadBlog(
                    profileImage,
                    galleryImages,
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
                                DialogsUtils.showSuccessDialog(getContext(),
                                        "Uploaded Successfully","Your blog is uploaded successfully");
                            } else {
                                DialogsUtils.showAlertDialog(getContext(),false,
                                        "Uploaded Failed","Please try again. Blog uploading is failed\n" +
                                                "And make sure you have strong internet connection");
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogsUtils.showResponseMsg(getContext(),true);
                        }
                    });
                }
            });
            return null;
        }
    }

}
