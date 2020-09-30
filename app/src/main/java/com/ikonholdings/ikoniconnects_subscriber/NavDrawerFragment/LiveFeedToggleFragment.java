package com.ikonholdings.ikoniconnects_subscriber.NavDrawerFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Data...");
                progressDialog.setCancelable(false);

                web_view.requestFocus();
                web_view.getSettings().setLightTouchEnabled(true);
                web_view.getSettings().setJavaScriptEnabled(true);
                web_view.getSettings().setGeolocationEnabled(true);
                web_view.setSoundEffectsEnabled(true);
                web_view.loadUrl("https://www.youtube.com/watch?v = atnyX5yjLj0");
                web_view.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        if (progress < 100) {
                            progressDialog.show();
                        }
                        if (progress == 100) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

       return v;
    }
}
