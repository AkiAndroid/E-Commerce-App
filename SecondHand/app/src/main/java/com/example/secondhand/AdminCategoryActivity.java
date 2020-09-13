package com.example.secondhand;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.secondhand.Models.Users;
import com.example.secondhand.Preval.Prevalent;
import com.google.android.material.navigation.NavigationView;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView bookimage,deviceimage;
    DrawerLayout myDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        bookimage=findViewById(R.id.bookimage);
        deviceimage=findViewById(R.id.electronics_image);

        myDrawerLayout=findViewById(R.id.drawer_layout);
        NavigationView myNavigationView=findViewById(R.id.nav_view1);
        View headerView= myNavigationView.getHeaderView(0);





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

        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                 if(id==R.id.nav_seller_myprod)
                 {
                  Intent intent=new Intent(getApplicationContext(),YourProducts.class);
                     Log.i("hi",Prevalent.CurrentOnlineUsers.getRollno());
                  startActivity(intent);
                }

                if (id==R.id.nav_seller_logout){


                    Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });


    }
}