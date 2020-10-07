package com.ikonholdings.ikoniconnects_subscriber.CustomDialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.Chat.RecyclerFollwers;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.FollowersModel;

import java.util.ArrayList;
import java.util.List;

public class SelectFollowersDialog extends DialogFragment {

    private  RecyclerView mRecyclerView;
    private  RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private  Context context;
    private  List<Integer> UserIds= new ArrayList<>();
    private List<FollowersModel> followersList = new ArrayList<>();

    private static final String TAG = "Dialog";

    public interface OnCreateGroup {
        void sendListOfGroupMembers(List<Integer> UserIds, String GroupName);
    }
    public OnCreateGroup mOnCreateGroup;

    public SelectFollowersDialog(Context context, List<FollowersModel> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_followers, null);

        mRecyclerView =view.findViewById(R.id.recyclerViewFollwer);
        EditText edt_GroupName =view.findViewById(R.id.edt_Name);
        Button btn_createGroup =view.findViewById(R.id.createGroup);

        buildRecyclerView(followersList);

        btn_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_GroupName.getText().toString().isEmpty()){
                    edt_GroupName.setError("Required");
                    edt_GroupName.requestFocus();
                }else if(UserIds.size() < 2) {
                    Toast.makeText(context, "Select at least two members", Toast.LENGTH_SHORT).show();
                } else{
                    mOnCreateGroup.sendListOfGroupMembers(UserIds, edt_GroupName.getText().toString());
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    private void buildRecyclerView(List<FollowersModel> followersList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new RecyclerFollwers(followersList);

        ((RecyclerFollwers) mAdapter).setOnItemClickListner(new RecyclerFollwers.onItemClickListner() {
            @Override
            public void onClickAdd(Integer UserId) {
                UserIds.add(UserId);
            }

            @Override
            public void onClickRemove(Integer UserId) {
                for (int i: UserIds) {
                    if(i == UserId){
                        UserIds.remove(i);
                    }
                }
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnCreateGroup = (OnCreateGroup) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
