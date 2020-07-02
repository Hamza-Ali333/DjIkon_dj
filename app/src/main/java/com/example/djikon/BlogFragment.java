package com.example.djikon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BlogFragment extends Fragment {

    private EditText edt_Title, edt_Discription;
    private ImageView img_Emogi, img_Camera, img_Gallery, img_Mic;
    private Button btn_Post;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_blog,container,false);
       createRefrences(v);






       return v;
    }

    private void createRefrences(View v){
        edt_Title= v.findViewById(R.id.txt_blog_title);
        edt_Discription = v.findViewById(R.id.txt_blog_discription);

        img_Emogi = v.findViewById(R.id.imogi);
        img_Camera = v.findViewById(R.id.camera);
        img_Gallery = v.findViewById(R.id.galley);
        img_Mic = v.findViewById(R.id.mic);
    }
}
