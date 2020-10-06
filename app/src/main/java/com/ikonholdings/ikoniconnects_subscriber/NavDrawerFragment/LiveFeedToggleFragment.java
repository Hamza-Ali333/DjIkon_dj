package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ikonholdings.ikoniconnects_subscriber.R;

import java.util.List;


public class LiveFeedToggleFragment extends Fragment {

    private WebView web_view;
    private ImageView btn_Toggle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_live_feed_toggle,container,false);
       btn_Toggle = v.findViewById(R.id.togle_btn);
       web_view = (WebView) v.findViewById(R.id.webview);

        btn_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  final ProgressDialog progressDialog = new ProgressDialog(getContext());
//                progressDialog.setMessage("Loading Data...");
//                progressDialog.setCancelable(false);
//
//                web_view.requestFocus();
//                web_view.getSettings().setLightTouchEnabled(true);
//                web_view.getSettings().setJavaScriptEnabled(true);
//                web_view.getSettings().setGeolocationEnabled(true);
//                web_view.setSoundEffectsEnabled(true);
//                web_view.loadUrl("https://www.youtube.com/watch?v = atnyX5yjLj0");
//                web_view.setWebChromeClient(new WebChromeClient() {
//                    public void onProgressChanged(WebView view, int progress) {
//                        if (progress < 100) {
//                            progressDialog.show();
//                        }
//                        if (progress == 100) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                });
                validateMobileLiveIntent(getContext());
            }
        });

       return v;
    }

    private boolean canResolveMobileLiveIntent(Context context) {
        Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
                .setPackage("com.google.android.youtube");
        PackageManager pm = context.getPackageManager();
        List resolveInfo =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    private void validateMobileLiveIntent(Context context) {
        if (canResolveMobileLiveIntent(context)) {
           startMobileLive(context);
        } else {
            // Prompt user to install or upgrade the YouTube app
        }
    }

    private Intent createMobileLiveIntent(Context context, String description) {
        Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
                .setPackage("com.google.android.youtube");
        Uri referrer = new Uri.Builder()
                .scheme("android-app")
                .appendPath(context.getPackageName())
                .build();

        intent.putExtra(Intent.EXTRA_REFERRER, referrer);
        if (!TextUtils.isEmpty(description)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, description);
        }
        return intent;
    }


    private void startMobileLive(Context context) {
        Intent mobileLiveIntent = createMobileLiveIntent(context, "Streaming via ...");
        startActivity(mobileLiveIntent);
    }


}
