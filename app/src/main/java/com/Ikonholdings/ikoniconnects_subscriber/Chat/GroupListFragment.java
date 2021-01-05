package com.Ikonholdings.ikoniconnects_subscriber.Chat;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects_subscriber.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects_subscriber.Chat.Model.GroupChatListModel;
import com.Ikonholdings.ikoniconnects_subscriber.Chat.Recycler.RecyclerGroupChatList;
import com.Ikonholdings.ikoniconnects_subscriber.CustomDialogs.SelectFollowersDialog;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupListFragment extends Fragment implements SelectFollowersDialog.OnCreateGroup {
    private TextView txt_Msg;
    private RecyclerView mRecyclerView;
    private RecyclerGroupChatList mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout pullToRefresh;

    private SearchView mSearchView;

    private static Boolean isCompleted = false;
    private AlertDialog loadingDialog;

    private FloatingActionButton btn_AddGroup;
    private List<FollowersModel> follwersList;

    private DatabaseReference myRef;
    private List<GroupChatListModel> mGroupChatList;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_group,container,false);
        createReferences(v);
        new GetFollowers().execute();

        //this will contain the currentUser id
        currentUserId = PreferenceData.getUserId(getContext());

        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);//will make layout reverse
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);//always at new entry at the top
        mRecyclerView.setLayoutManager(mLayoutManager);
        mGroupChatList = new ArrayList<>();

        new GetAllGroupsOfThisSubscriber().execute();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAllGroupsOfThisSubscriber().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        btn_AddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCompleted){
                    if(follwersList.isEmpty()) {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "No User",
                                "No follower found.!");
                    } else if(follwersList.size() < 2){
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "Note",
                                "You need at least 2 followers to create group!");
                    }
                    else{
                        SelectFollowersDialog selectFollowersDialog = new SelectFollowersDialog(getContext(), follwersList);
                        selectFollowersDialog.setTargetFragment(GroupListFragment.this, 1);
                        selectFollowersDialog.show(getFragmentManager(), "Dialog");
                    }
                }else {
                    Toast.makeText(getContext(), "Please Wait Getting information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        return v;
    }

    private void filter(String searchText){
        List<GroupChatListModel> filteredlist = new ArrayList<>();
        String ConcatinatName;
        for(GroupChatListModel item: mGroupChatList) {
            ConcatinatName = item.getGroup_Name();
            if(ConcatinatName.toLowerCase().contains(searchText.toLowerCase())){
                filteredlist.add(item);
            }
        }

        mAdapter.filterList(filteredlist);
    }

    private void createReferences(View v) {
        txt_Msg = v.findViewById(R.id.msg);
        mSearchView = v.findViewById(R.id.edt_search);
        mRecyclerView = v.findViewById(R.id.recyclerViewChatGroup);
        btn_AddGroup = v.findViewById(R.id.fab);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
    }

    @Override
    public void sendListOfGroupMembers(List<Integer> UserIds, String GroupName) {
        if(!UserIds.isEmpty()){
            GroupChatListModel listModel = new GroupChatListModel();
            listModel.setGroup_Profile("no");
            listModel.setGroup_Name(GroupName);
            listModel.setCreator_Id(PreferenceData.getUserId(getContext()));

            listModel.setGroup_User_Ids(UserIds);
            DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Chats");
            listModel.setGroupId(Ref.push().getKey());
            String node = Ref.push().getKey();
            listModel.setNode(node);
            
            //Creating Group
            Ref.child("groups").child(node).setValue(listModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //GroupCreated Successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    DialogsUtils.showAlertDialog(getContext(),
                            false,
                            "Error","Something went wrong please check your internet and try again.");
                }
            });
        }
    }

    private class GetFollowers extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!isCompleted){
                loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(getContext());
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<List<FollowersModel>> call = jsonApiHolder.getFollowers("followers");

            call.enqueue(new Callback<List<FollowersModel>>() {
                @Override
                public void onResponse(Call<List<FollowersModel>> call, Response<List<FollowersModel>> response) {
                    if(response.isSuccessful()){
                        follwersList = response.body();

                    }else {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "Error",
                                "Please try again and check your internet connection");
                    }

                }
                @Override
                public void onFailure(Call<List<FollowersModel>> call, Throwable t) {
                    DialogsUtils.showResponseMsg(getContext(),true);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isCompleted = true;
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetAllGroupsOfThisSubscriber extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!currentUserId.isEmpty() && !currentUserId.equals("No Id")) {

                Query query = myRef.child("groups").orderByChild("creator_Id").equalTo(PreferenceData.getUserId(getContext()));

               query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            mGroupChatList.clear();
                            txt_Msg.setVisibility(View.GONE);
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                               GroupChatListModel listModel = snapshot.getValue(GroupChatListModel.class);
                               mGroupChatList.add(listModel);
                            }
                            mAdapter = new RecyclerGroupChatList(mGroupChatList,currentUserId);
                            mRecyclerView.setAdapter(mAdapter);
                        }else {
                            txt_Msg.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "Error",
                                "It's seems like you didn't have conversation with any Subscriber");
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mGroupChatList == null){
                txt_Msg.setVisibility(View.VISIBLE);
            }else {
                txt_Msg.setVisibility(View.GONE);
            }
        }
    }


}