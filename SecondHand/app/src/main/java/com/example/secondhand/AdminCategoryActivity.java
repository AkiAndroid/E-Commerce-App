package com.example.secondhand;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.secondhand.Models.Users;
import com.example.secondhand.Preval.Prevalent;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView foodimage,bookimage,deviceimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        bookimage=findViewById(R.id.bookimage);
        deviceimage=findViewById(R.id.electronics_image);



        bookimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),ProductAddingActivity.class);
                intent.putExtra("category","Book");
                intent.putExtra("sellerrollno", Prevalent.CurrentOnlineUsers.getRollno());
                startActivity(intent);
            }
        });

        deviceimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ProductAddingActivity.class);
                intent.putExtra("sellerrollno", Prevalent.CurrentOnlineUsers.getRollno());
                intent.putExtra("category","Electronics");
                startActivity(intent);
            }
        });


    }
}