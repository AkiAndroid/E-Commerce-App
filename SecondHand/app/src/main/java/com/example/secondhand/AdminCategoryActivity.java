package com.example.secondhand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView foodimage,bookimage,deviceimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        foodimage=findViewById(R.id.foodimage);
        bookimage=findViewById(R.id.bookimage);
        deviceimage=findViewById(R.id.electronics_image);

        foodimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductAddingActivity.class);
                intent.putExtra("category","Food");
                startActivity(intent);
            }
        });

        bookimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductAddingActivity.class);
                intent.putExtra("category","Book");
                startActivity(intent);
            }
        });

        deviceimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductAddingActivity.class);
                intent.putExtra("category","Electronics");
                startActivity(intent);
            }
        });


    }
}