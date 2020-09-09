package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.secondhand.Preval.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button welcomeloginbutton,welcomesigninbutton;
    ProgressDialog loadingbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeloginbutton=findViewById(R.id.welcomeloginbutton);
        welcomesigninbutton=findViewById(R.id.welcomesignupbutton);
        loadingbar=new ProgressDialog(this);

        Paper.init(this);

        welcomeloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                startActivity(intent);
            }
        });

        welcomesigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterAccount.class);
                startActivity(intent);
            }
        });

        String UserRollnoKey=Paper.book().read(Prevalent.UserRollnoKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordkey);

        if (UserRollnoKey!="" && UserPasswordKey!=""){
            if (!TextUtils.isEmpty(UserPasswordKey) && !TextUtils.isEmpty(UserRollnoKey)){
                AllowAcessDirectly(UserRollnoKey,UserPasswordKey);
                loadingbar.setTitle("Trying to Enter");
                loadingbar.setMessage("Getting yourself in");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }
        }
    }

    private void AllowAcessDirectly(final String rollno, final String Password) {

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(rollno).exists()){

                    Toast.makeText(MainActivity.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    loadingbar.dismiss();
                }
                else if(snapshot.child("Seller").child(rollno).exists()){
                    Toast.makeText(MainActivity.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),AdminCategoryActivity.class);
                    startActivity(intent);
                    loadingbar.dismiss();
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}