package com.example.secondhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.secondhand.Models.Users;
import com.example.secondhand.Preval.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class loginactivity extends AppCompatActivity {

EditText webmailedittext,passwordedittext;
Button Login;
String mailid,rollno;

ProgressDialog loadingbar;
String Password, ParentdbName="Users";
Switch rememberMe;
TextView Adminpagelink,Userpagelink,pagename;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        webmailedittext=findViewById(R.id.webmailinput);
        passwordedittext=findViewById(R.id.passwordinput);
        Login=findViewById(R.id.loginbutton);
        loadingbar=new ProgressDialog(this);
        rememberMe=findViewById(R.id.rememberme);
        Adminpagelink=findViewById(R.id.Admin);
        Userpagelink=findViewById(R.id.User);
        pagename=findViewById(R.id.pagename);

//library for storing data(Non-sql)
        Paper.init(this);

        Adminpagelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentdbName="Seller";
                Adminpagelink.setVisibility(View.INVISIBLE);
                Userpagelink.setVisibility(View.VISIBLE);
                pagename.setText("Seller");
                Log.d("Hello",ParentdbName);

            }
        });

        Userpagelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentdbName="Users";
                Userpagelink.setVisibility(View.INVISIBLE);
                Adminpagelink.setVisibility(View.VISIBLE);
                pagename.setText("User");

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailid=webmailedittext.getText().toString();
                int iend = mailid.indexOf("@");

                //roll no retrieving

                        if(iend!=-1){

                            rollno= mailid.substring(0 , iend);
                        }

              //edittext checker
                Password= passwordedittext.getText().toString();

                        if (TextUtils.isEmpty(mailid)){
                            webmailedittext.setError("Enter the Webmail");
                        }
                        else if (TextUtils.isEmpty(Password)){
                            passwordedittext.setError("Enter Password");
                        }

                        else {
                            loadingbar.setTitle("Trying to Enter");
                            loadingbar.setMessage("Getting yourself in");
                            loadingbar.setCanceledOnTouchOutside(false);
                            loadingbar.show();
                            Log.i("Hello",ParentdbName);

                            AllowAccesestoAccount(rollno,Password);
                        }



            }

            private void AllowAccesestoAccount(final String rollno, final String password) {

                if (rememberMe.isChecked()){
                    Paper.book().write(Prevalent.UserRollnoKey,rollno);
                    Paper.book().write(Prevalent.UserPasswordkey,password);
                }

                final DatabaseReference Rootref;
                Rootref= FirebaseDatabase.getInstance().getReference();

                Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if(snapshot.child(ParentdbName).child(rollno).exists()){


                            Users userData=snapshot.child(ParentdbName).child(rollno).getValue(Users.class);

                            if (userData.getMailid().matches(mailid)){
                                if (userData.getPassword().matches(Password)){
                                    if(ParentdbName.equals("Seller")){
                                        Toast.makeText(loginactivity.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                        Intent intent=new Intent(getApplicationContext(),AdminCategoryActivity.class);
                                        startActivity(intent);
                                    }
                                    else if (ParentdbName.equals("Users")){

                                        Toast.makeText(loginactivity.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(intent);

                                    }
                                }
                                else {
                                    passwordedittext.setError("Incorrect Password");
                                    loadingbar.dismiss();
                                }
                            }
                        }
                        else {
                            webmailedittext.setError("This webmail haven't been registered");
                            loadingbar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}