package com.ikonholdings.ikoniconnects_subscriber.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Model.ManytoManyChatModel;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.APIService;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Client;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Data;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.MyResponse;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Sender;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Notification.Token;
import com.ikonholdings.ikoniconnects_subscriber.Chat.Recycler.RecyclerGroupChat;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

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


    private String groupName, imgProfileUrl, groupKey;
    private String[] ReceiverList;

    private String CurrentSubscriberId;
    private String CurrentSubscriberName;

    private APIService apiService;
    private FirebaseUser fuser;
    private Boolean notify = false;

    private String Msg;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentSubscriberName = PreferenceData.getUserName(this);

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
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        //tool bar UserProfile
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //group profile image click on it
            }
        });

        //getting data of the Receiver
        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        groupKey = i.getStringExtra("groupKey");
        imgProfileUrl = i.getStringExtra("groupImage");
        String userlist = i.getStringExtra("userList");
        setSubscriberProfile(imgProfileUrl);

        //this part should only use in user application
        userlist = userlist.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
        String[] list = userlist.split(",");

        ReceiverList= new String[list.length-1];

        if(list != null){
            for (int j = 0; j < list.length-1 ; j++) {
                if(!list[j].equals(PreferenceData.getUserId(this))){
                    ReceiverList[j] = list[j];
                }
            }
        }


        getSupportActionBar().setTitle("");
        toolBarTitle.setText(groupName);//set Subscriber Name in tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference("Chats");

        CurrentSubscriberId = PreferenceData.getUserId(this);

        checkHaveChatOrNot();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkHaveChatOrNot();
                pullToRefresh.setRefreshing(false);
            }
        });

        mProgressDialog = DialogsUtils.showProgressDialog(this,"Getting Massages","Please Wait");

        String finalUserlist = userlist;
        btn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String currentDateAndTime = sdf.format(new Date());
                if(!edt_Massage.getText().toString().isEmpty()){
                    if(!alreadyHaveChat){
                        //MAke Node for this new User if they are texting first time

                    }

                    Msg = edt_Massage.getText().toString();
                    sendMassage(Msg
                            ,PreferenceData.getUserId(GroupChatViewerActivity.this),
                            finalUserlist,
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

        myRef.child("GroupMessages").child(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mManytoManyChatModels.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // snapshot object is every child of "Restaurant" that match the filter
                        //now here set data in to the field
//                        ManytoManyChatModel list = snapshot.getValue(ManytoManyChatModel.class);
//                        mOneToOneChatModel.add(list);
                        mManytoManyChatModels.add(new ManytoManyChatModel(
                                snapshot.child("receivers").getValue(String.class),
                                snapshot.child("sender").getValue(String.class),
                                snapshot.child("message").getValue(String.class),
                                snapshot.child("time_stemp").getValue(String.class),
                                snapshot.child("image").getValue(String.class),
                                snapshot.getKey()
                        ));

                    }
                    mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time

                    mLayoutManager = new LinearLayoutManager(GroupChatViewerActivity.this);
                    mAdapter = new RecyclerGroupChat(mManytoManyChatModels,
                            PreferenceData.getUserId(GroupChatViewerActivity.this),
                            groupKey,
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
            Picasso.get().load(ApiClient.Base_Url+imageUrl)
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
                if (snapshot.hasChild(groupKey)) {

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

    private void sendMassage (String Massage, String Sender, String Receivers,String sendTime) {

        ManytoManyChatModel manytoManyChatModel = new ManytoManyChatModel();
        manytoManyChatModel.setSender(Sender);
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

        myRef.child("GroupMessages").child(groupKey).push().setValue(manytoManyChatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,userName+": "+messaage,"New Message",
                            String.valueOf(ReceiverList));

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
}