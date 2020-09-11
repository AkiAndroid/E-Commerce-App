package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhand.Preval.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView userProfilePhoto;
    EditText userChangeNameEdittext,hostel_name,phone_number;
    Button update_button,back_button;
    Uri Image_Uri;
    String my_Image_URL="",checker="";
    StorageReference storage_profile_Reference;
    StorageTask uploadTask;
    TextView change_profile_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userProfilePhoto=findViewById(R.id.settings_profile_image);
        userChangeNameEdittext=findViewById(R.id.settings_change_name);
        update_button=findViewById(R.id.settings_update_button);
        hostel_name=findViewById(R.id.settings_hostel_name);
        phone_number=findViewById(R.id.settings_phone_no);
        change_profile_text=findViewById(R.id.change_profile_text);
        back_button=findViewById(R.id.settings_back_button);

        storage_profile_Reference= FirebaseStorage.getInstance().getReference().child("Profile pictures");

        userInfoDisplay(userProfilePhoto,userChangeNameEdittext,hostel_name,phone_number);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checker.equals("clicked"))
                {
                   userInfoSaved();
                }
                else {

                    updateUserInfoOnly();
                }

            }
        });

        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(Image_Uri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

    }

    private void updateUserInfoOnly()
    {
        //user profile update
        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap = new  HashMap<>();
        userMap.put("name",userChangeNameEdittext.getText().toString());
        userMap.put("hostelname",hostel_name.getText().toString());
        userMap.put("phoneno",phone_number.getText().toString());
        Reference.child(Prevalent.CurrentOnlineUsers.getRollno()).updateChildren(userMap);


        //seller profile update
        DatabaseReference Reference1 = FirebaseDatabase.getInstance().getReference().child("Seller");
        HashMap<String,Object> userMap1 = new  HashMap<>();
        userMap1.put("name",userChangeNameEdittext.getText().toString());
        userMap1.put("hostelname",hostel_name.getText().toString());
        userMap1.put("phoneno",phone_number.getText().toString());
        Reference1.child(Prevalent.CurrentOnlineUsers.getRollno()).updateChildren(userMap1);

        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
         CropImage.ActivityResult result=CropImage.getActivityResult(data);
         Image_Uri=result.getUri();

         userProfilePhoto.setImageURI(Image_Uri);

        }

        else
            {
                Toast.makeText(this, "Error: Try Again", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                finish();
        }
    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(userChangeNameEdittext.getText().toString()))
        {
            userChangeNameEdittext.setError("Fill your Name");

        }
        else if(TextUtils.isEmpty(hostel_name.getText().toString()))
        {
            hostel_name.setError("Hostel Name Mandatory");
        }
        else if (TextUtils.isEmpty(phone_number.getText().toString()))
        {
            phone_number.setError("Phone number is Mandatory");
        }
        else if(checker=="clicked")
            {

             uploadImage();
        }

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Making Your Profile Beautiful");
        progressDialog.setMessage("Go and Have a McDonald's Burger or Try Burger King");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(Image_Uri!=null)
        {
            final StorageReference fileRef = storage_profile_Reference
                    .child(Prevalent.CurrentOnlineUsers.getRollno()+".jpg");

            uploadTask=fileRef.putFile(Image_Uri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl= (Uri) task.getResult();
                        my_Image_URL=downloadUrl.toString();

                        //user profile update
                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap = new  HashMap<>();
                        userMap.put("name",userChangeNameEdittext.getText().toString());
                        userMap.put("hostelname",hostel_name.getText().toString());
                        userMap.put("phoneno",phone_number.getText().toString());
                        userMap.put("image",my_Image_URL);
                        Reference.child(Prevalent.CurrentOnlineUsers.getRollno()).updateChildren(userMap);

                        //seller profile update
                        DatabaseReference Reference1 = FirebaseDatabase.getInstance().getReference().child("Seller");
                        HashMap<String,Object> userMap1 = new  HashMap<>();
                        userMap1.put("name",userChangeNameEdittext.getText().toString());
                        userMap1.put("hostelname",hostel_name.getText().toString());
                        userMap1.put("phoneno",phone_number.getText().toString());
                        userMap1.put("image",my_Image_URL);
                        Reference1.child(Prevalent.CurrentOnlineUsers.getRollno()).updateChildren(userMap1);

                        progressDialog.dismiss();

                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error: Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(this, "Kindly Select Your Beautiful face", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView userProfilePhoto, EditText changeNameEdittext, final EditText hostel_name, final EditText phone_number)
    {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUsers.getRollno());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                 if (snapshot.child("image").exists())
                 {
                   String image=snapshot.child("image").getValue().toString();
                   String name=snapshot.child("name").getValue().toString();
                   String hostelname=snapshot.child("hostelname").getValue().toString();
                   String phone_no=snapshot.child("phoneno").getValue().toString();



                     Picasso.get().load(image).into(userProfilePhoto);
                     userChangeNameEdittext.setText(name);
                     phone_number.setText(phone_no);
                     hostel_name.setText(hostelname);

                 }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}