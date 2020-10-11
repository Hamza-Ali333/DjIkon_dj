package com.ikonholdings.ikoniconnects_subscriber.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class UploadImageToFirebase {

    public static void uploadimage( Uri imageUri, String GroupId, Context context){

        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference mstorageRef = FirebaseStorage.getInstance().getReference("GroupImage");
        final StorageReference fileRefrence = mstorageRef.child(
                GroupId);

        fileRefrence.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //can hundle the success of the image uploading
                fileRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Task<Void> myRef =
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference("Chats")
                                        .child("groups")
                                        .child(GroupId).child("group_Profile").setValue(uri.toString());

                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failed to Upload try Again", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {

                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(
                            UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(
                                "Uploaded "
                                        + (int)progress + "%");
                    }
                });
    }

    private static class updateImageUrl extends AsyncTask<Void, Void, Void>{
        private Task<Void> myRef;
        private String groupNode;
        private String Url;
        public updateImageUrl(String groupNode, String Url) {
            this.groupNode = groupNode;
            this.Url = Url;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
