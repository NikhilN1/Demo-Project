package com.yoska.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailView extends AppCompatActivity {

    TextView textView1, textView2, textView3;
    String first_name, last_name, email, avatar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        setTitle("Detail Info");

        textView1 = findViewById(R.id.first_name);
        textView2 = findViewById(R.id.last_name);
        textView3 = findViewById(R.id.email);
        imageView = findViewById(R.id.imageView3);

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        email = getIntent().getStringExtra("email");
        avatar = getIntent().getStringExtra("avatar");

        textView1.setText(first_name);
        textView2.setText(last_name);
        textView3.setText(email);
        Picasso.with(getApplicationContext()).load(avatar).into(imageView);
    }
}