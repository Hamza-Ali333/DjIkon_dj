package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditBlogActivity extends AppCompatActivity {


    Button btn_UpDatePost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blog);


      createRefrences();
      btn_UpDatePost.setText("Update Post");

      btn_UpDatePost.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(EditBlogActivity.this,MainActivity.class));
          }
      });

    }

    private void createRefrences(){
       // btn_UpDatePost = findViewById(R.id.btn_post);
    }
}