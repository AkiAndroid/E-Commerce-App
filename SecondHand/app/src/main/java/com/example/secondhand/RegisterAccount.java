package com.example.secondhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterAccount extends AppCompatActivity {

    EditText fullname,Rollno,webmail,password,repassword,phonenumber;
    Button signup;
    ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        fullname=findViewById(R.id.Nameinput);
        webmail=findViewById(R.id.signinwebmailinput);
        Rollno=findViewById(R.id.Rollnoinput);
        password=findViewById(R.id.signinpasswordinput);
        repassword=findViewById(R.id.signinrepasswordinput);
        phonenumber=findViewById(R.id.signinphonenumber);
        signup=findViewById(R.id.Signupbutton);
        loadingbar=new ProgressDialog(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name=fullname.getText().toString();
        String mailid=webmail.getText().toString();
        String rollno=Rollno.getText().toString();
        String Password=password.getText().toString();
        String RePassword=repassword.getText().toString();
        String phoneno=phonenumber.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(mailid)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }

       else if (TextUtils.isEmpty(rollno)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneno)){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }
        else if (!Password.matches(RePassword)){
            Toast.makeText(this, "Rewrite the Password", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingbar.setTitle("Making Your Dish");
            loadingbar.setMessage("Rest a While for Sometime");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidateUserDetails(name,mailid,rollno,Password,phoneno);

        }

    }

    private void ValidateUserDetails(final String name, final String mailid, final String rollno, final String password, final String phoneno) {

     final DatabaseReference Rootref;
     Rootref= FirebaseDatabase.getInstance().getReference();

     Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(!snapshot.child("Users").child(rollno).exists()){

                 HashMap<String,Object> userDataMap=new HashMap<>();
                 userDataMap.put("name",name);
                 userDataMap.put("mailid",mailid);
                 userDataMap.put("rollno",rollno);
                 userDataMap.put("password",password);
                 userDataMap.put("phoneno",phoneno);

                 Rootref.child("Users").child(rollno).updateChildren(userDataMap)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful()){
                                     Toast.makeText(RegisterAccount.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
                                     loadingbar.dismiss();

                                     Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                                     startActivity(intent);
                                 }
                                 else {
                                     Toast.makeText(RegisterAccount.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
                                     loadingbar.dismiss();
                                 }

                             }
                         });

                 Rootref.child("Seller").child(rollno).updateChildren(userDataMap);


             }

             else {
                 webmail.setError("Email id Already Exists");
                 loadingbar.dismiss();
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }
}