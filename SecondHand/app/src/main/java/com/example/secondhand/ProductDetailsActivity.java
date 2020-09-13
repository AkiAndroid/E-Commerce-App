package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhand.Models.Products;
import com.example.secondhand.Preval.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView product_image;
    TextView product_name,model_name,descripion_text,condition,price;
    Button RequestButton;
    String productid;
    String sellerrollno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //getting productid
        productid=getIntent().getStringExtra("pid");

        product_image = findViewById(R.id.product_display_image);
        product_name=findViewById(R.id.product_name_textview);
        model_name=findViewById(R.id.model_text_view);
        descripion_text=findViewById(R.id.description_text_view);
        condition=findViewById(R.id.condition_text_view);
        price=findViewById(R.id.price_text_view);
        RequestButton =findViewById(R.id.request_button);


        getProductDetails(productid);


        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addingToCart();
            }
        });
    }

    private void addingToCart()
    {
        String saveCurrentDate,saveCurrentTime;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("MyOrders");
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productid);
        cartMap.put("productName",product_name.getText().toString());
        cartMap.put("price",price.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("sellerrollno",sellerrollno);
        cartMap.put("buyerrollno",Prevalent.CurrentOnlineUsers.getRollno());

        cartListRef.child("User View").child(Prevalent.CurrentOnlineUsers.getRollno())
                .child("Products").child(productid)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    cartListRef.child("Seller View").child(Prevalent.CurrentOnlineUsers.getRollno())
                            .child("Products").child(productid)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ProductDetailsActivity.this, "Requested Sucessfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),SellerInfo.class);
                                intent.putExtra("sellerinforollno",sellerrollno);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
        });

    }

    private void getProductDetails(String productid)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                Products products=snapshot.getValue(Products.class);

                product_name.setText(products.getProductname());
                model_name.setText("Model Name:"+ " " +products.getModelName());
                descripion_text.setText("Description:"+ " " +products.getDescription());
                condition.setText("Condition:"+ " " + products.getDuration());
                price.setText("Price Rs."+ " " +products.getPrice());
                sellerrollno=products.getSellerrollno();
                Picasso.get().load(products.getImage()).into(product_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}