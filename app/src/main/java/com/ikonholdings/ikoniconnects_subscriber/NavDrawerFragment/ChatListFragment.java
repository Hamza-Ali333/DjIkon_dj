package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects_subscriber.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects_subscriber.ResponseModels.UserChatListModel;
import com.ikonholdings.ikoniconnects_subscriber.Notification.Token;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.RecyclerView.RecyclerChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ChatListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerChatList mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout pullToRefresh;

    private SearchView mSearchView;

    DatabaseReference myRef;
    List<UserChatListModel> mUserChatList;

    String currentUserId;

    AlertDialog loadingDialog;

    private FirebaseUser fuser;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_area,container,false);
        createReferences(v);

        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);//will make layout reverse
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);//always at new entry at the top
        mRecyclerView.setLayoutManager(mLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //this will contain the currentUser id
        currentUserId = PreferenceData.getUserId(getContext());

        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        mUserChatList = new ArrayList<>();
        getChatList();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatList();
                pullToRefresh.setRefreshing(false);
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

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


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return v;
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder item, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView

            int position = item.getAdapterPosition();
            mUserChatList.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, mUserChatList.size());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.print_btn_color))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        if(fuser != null)
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void filter(String searchText){
        List<UserChatListModel> filteredlist = new ArrayList<>();
        String ConcatinatName;
        for(UserChatListModel item: mUserChatList) {
            ConcatinatName = item.getUser_Name();
            if(ConcatinatName.toLowerCase().contains(searchText.toLowerCase())){
                filteredlist.add(item);
            }
        }

        mAdapter.filterList(filteredlist);
    }


    private void getChatList(){
        //if get some data or not get the default value(No Email) form preferences
        if (!currentUserId.isEmpty() && !currentUserId.equals("No Id")) {
            myRef.child("chatListOfSubscriber").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        mUserChatList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mUserChatList.add(new UserChatListModel(
                                    snapshot.child("user_Id").getValue(String.class),
                                    snapshot.child("user_Uid").getValue(String.class),
                                    snapshot.child("user_Name").getValue(String.class),
                                    snapshot.child("imgProfileUrl").getValue(String.class),
                                    snapshot.child("status").getValue(String.class),
                                    snapshot.getKey()
                            ));
                        }

                        mAdapter = new RecyclerChatList(mUserChatList,currentUserId);
                        mRecyclerView.setAdapter(mAdapter);
                        loadingDialog.dismiss();

                    }else {
                         DialogsUtils.showAlertDialog(getContext(),
                                 false,
                                 "Note",
                                 "It's seems like you didn't have conversation with any User");
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogsUtils.showAlertDialog(getContext(),
                            false,
                            "Error",
                            "It's seems like you didn't have conversation with any Subscriber");
                    loadingDialog.dismiss();
                }
            });
        }
    }

    private void createReferences(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerView_Chat);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
        mSearchView = v.findViewById(R.id.edt_search);
    }

}
