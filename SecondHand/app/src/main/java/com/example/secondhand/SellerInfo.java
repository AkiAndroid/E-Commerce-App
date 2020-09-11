package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondhand.Models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerInfo extends AppCompatActivity {

    CircleImageView seller_info_image;
    TextView name,hostel,phonenumber;
    String sellerrollno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        sellerrollno=getIntent().getStringExtra("sellerinforollno");

        seller_info_image=findViewById(R.id.seller_profile_image);
        name=findViewById(R.id.seller_info_Name);
        hostel=findViewById(R.id.seller_hostel_name);
        phonenumber=findViewById(R.id.seller_info_contact_no);




        setUserinfo(sellerrollno);


    }

    private void setUserinfo(final String sellerrollno)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Users userData=snapshot.child("Seller").child(sellerrollno).getValue(Users.class);
                name.setText("Name: "+userData.getName());
                hostel.setText("Hostel: "+userData.getHostelname());
                phonenumber.setText("Contact: "+ userData.getPhoneno());
                Picasso.get().load(userData.getImage()).fit().centerCrop().into(seller_info_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}