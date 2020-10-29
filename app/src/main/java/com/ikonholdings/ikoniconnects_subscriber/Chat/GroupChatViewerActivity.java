package com.ikonholdings.ikoniconnects_subscriber.Chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Model.ManytoManyChatModel;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.APIService;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Client;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.MyResponse;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Notification;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Sender;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Token;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Recycler.RecyclerGroupChat;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.KeyBoard;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatViewerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircularImageView currentUserProfile;
    private TextView toolBarTitle;

    private SwipeRefreshLayout pullToRefresh;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference myRef;

    private Button btn_SendMsg;
    private EditText edt_Massage;

    private List<ManytoManyChatModel> mManytoManyChatModels;

    private ProgressDialog mProgressDialog;
    private Boolean alreadyHaveChat = false;

    private String groupName, imgProfileUrl, groupId, groupNode;

    private APIService apiService;
    private Boolean notify = false;

    private String Msg;

    private Uri Image_uri;

    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_viewer);
        createReferences();
        setSupportActionBar(toolbar);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        //give the Current Time and Date
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        //tool bar UserProfile
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //group profile image click on it
                if ( PermissionHelper.checkDefaultPermissions(GroupChatViewerActivity.this)) {
                    showImageImportDailog();
                }else {
                    PermissionHelper.managePermissions(GroupChatViewerActivity.this);
                }
            }
        });



        //getting data of the Receiver
        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        groupId = i.getStringExtra("groupKey");
        imgProfileUrl = i.getStringExtra("groupImage");
        groupNode = i.getStringExtra("node");
        List<Integer> list  = i.getIntegerArrayListExtra("list");
        setSubscriberProfile(imgProfileUrl);


        getSupportActionBar().setTitle("");
        toolBarTitle.setText(groupName);//set Subscriber Name in tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference("Chats");

        checkHaveChatOrNot();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkHaveChatOrNot();
                pullToRefresh.setRefreshing(false);
            }
        });

        mProgressDialog = DialogsUtils.showProgressDialog(this,"Getting Massages","Please Wait");


        btn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                KeyBoard.hideKeyboard(GroupChatViewerActivity.this);
                String currentDateAndTime = sdf.format(new Date());
                if(!edt_Massage.getText().toString().isEmpty()){
                    if(!alreadyHaveChat){
                        //MAke Node for this new User if they are texting first time

                    }

                    Msg = edt_Massage.getText().toString();
                    sendMassage(Msg
                            ,PreferenceData.getUserId(GroupChatViewerActivity.this),
                            list,
                            currentDateAndTime);
                }else{
                    Toast.makeText(GroupChatViewerActivity.this, "You Can't Send Empty massage", Toast.LENGTH_SHORT).show();
                }
                edt_Massage.setText("");
            }
        });

    }

    private void readMassages() {
        mManytoManyChatModels = new ArrayList<>();

        myRef.child("GroupMessages").child(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mManytoManyChatModels.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // snapshot object is every child of "Restaurant" that match the filter
                        //now here set data in to the field
                        ManytoManyChatModel list = snapshot.getValue(ManytoManyChatModel.class);
                        mManytoManyChatModels.add(list);

                    }
                    mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time

                    mLayoutManager = new LinearLayoutManager(GroupChatViewerActivity.this);
                    mAdapter = new RecyclerGroupChat(mManytoManyChatModels,
                            PreferenceData.getUserId(GroupChatViewerActivity.this),
                            groupId,
                            PreferenceData.getUserImage(GroupChatViewerActivity.this));

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    //scroll to end item of list
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                    scrollRecyclerToTheBottom();
                    mProgressDialog.dismiss();

                }else {
                    //open msg dailog
                    mProgressDialog.dismiss();
                    DialogsUtils.showAlertDialog(GroupChatViewerActivity.this,
                            false,
                            "No Data Found",
                            "Sorry You Not Have"
                    );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(GroupChatViewerActivity.this, "not connected to the firebase", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSubscriberProfile(String imageUrl){
        if (imageUrl != null && !imageUrl.equals("No Image") && !imageUrl.equals("no") ){
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(currentUserProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {
                            // Toast.makeText(getC, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void checkHaveChatOrNot(){
        //check if User have already chat with this Subscriber
        myRef.child("GroupMessages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Here check Node of this User Email and Subscriber Email is exit or not
                if (snapshot.hasChild(groupId)) {

                    alreadyHaveChat = true;
                    readMassages();
                }
                else {

                    alreadyHaveChat = false;
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupChatViewerActivity.this, "They Do not had any chat", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMassage (String Massage, String Sender, List<Integer> Receivers,String sendTime) {
        ManytoManyChatModel manytoManyChatModel = new ManytoManyChatModel();
        manytoManyChatModel.setSender_Id(Sender);
        manytoManyChatModel.setSender_Name(PreferenceData.getUserName(this));
        manytoManyChatModel.setReceivers(Receivers);
        manytoManyChatModel.setMessage(Massage);
        manytoManyChatModel.setTime_stemp(sendTime);

        if(PreferenceData.getUserImage(GroupChatViewerActivity.this).equals("no")
                || PreferenceData.getUserImage(GroupChatViewerActivity.this).equals("No Image")){
            manytoManyChatModel.setImage("no");
        }
        else {
            manytoManyChatModel.setImage(PreferenceData.getUserImage(GroupChatViewerActivity.this));
        }

        myRef.child("GroupMessages").child(groupId).push().setValue(manytoManyChatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(GroupChatViewerActivity.this, "Send", Toast.LENGTH_SHORT).show();
                if(!alreadyHaveChat){
                    checkHaveChatOrNot();
                }
                alreadyHaveChat= true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupChatViewerActivity.this, "Not Send", Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //nead to check this line what is the propose of this line
                // String user= dataSnapshot.getValue(String.class);
                if(notify){
                   // sendNotification("userUid",PreferenceData.getUserName(GroupChatViewerActivity.this),Msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver,final String userName,final String messaage) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Notification data = new Notification(false,
                            PreferenceData.getUserId(GroupChatViewerActivity.this),
                            R.mipmap.ic_launcher,
                            userName + ": " + messaage,
                            "New Message",
                            "Notification Recieverid");

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(!response.isSuccessful()){
//                                        if(response.body().success != 1)
//                                        Toast.makeText(ChatViewerActivity.this, "Failed to send Notification", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void scrollRecyclerToTheBottom () {
        //it will scroll recycler view to the bottom when keyboard open
        if (Build.VERSION.SDK_INT >= 11) {
            mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.smoothScrollToPosition(
                                        mRecyclerView.getAdapter().getItemCount());
                            }
                        }, 100);
                    }
                }
            });
        }
    }

    private void createReferences() {
        toolbar = findViewById(R.id.toolbar);
        toolBarTitle = findViewById(R.id.toolbar_title);
        currentUserProfile = findViewById(R.id.currentUserProfile);

        pullToRefresh =findViewById(R.id.pullToRefresh);

        edt_Massage = findViewById(R.id.edt_sendmsg);
        btn_SendMsg = findViewById(R.id.btn_send_msg);
        mRecyclerView = findViewById(R.id.withdraw_recycler);
    }

    //image import
    private void showImageImportDailog() {
        String[] items = {"Camera", "Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(GroupChatViewerActivity.this);
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
        startActivityForResult(gallery, IMAGE_PICK_GALLARY_REQUEST_CODE);
    }

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200){
            if (grantResults.length > 0) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (!cameraAccepted || !storageAccepted) {
                    PermissionHelper.showPermissionAlert(GroupChatViewerActivity.this);
                }
            }
        }
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
                        .setCropMenuCropButtonTitle("Select")
                        .start(GroupChatViewerActivity.this);
            }
            //from gallary
            if (requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropMenuCropButtonTitle("Select")
                        .start(this);
            }
            //getcroped Image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                currentUserProfile.setImageURI(Image_uri);
                if(Image_uri != null){
                    UploadImageToFirebase.uploadimage(Image_uri, groupNode,this);
                }
            }

        } else {
            Toast.makeText(this, "Image is not Selected try Again", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }


}