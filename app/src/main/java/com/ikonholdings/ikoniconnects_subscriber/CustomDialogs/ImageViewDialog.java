package com.ikonholdings.ikoniconnects_subscriber.CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ikonholdings.ikoniconnects_subscriber.R;


public class ImageViewDialog {

    public static AlertDialog showReferralCodeDialog(Context context, Uri uri) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_image_view, null);

        ImageView img = view.findViewById(R.id.img);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog = builder.show();

        if(uri != null)
            img.setImageURI(uri);
        else {
            alertDialog.dismiss();
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

        return alertDialog;
    }

}

