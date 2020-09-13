package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.secondhand.Models.yourproducts;
import com.example.secondhand.Preval.Prevalent;
import com.example.secondhand.ViewHolder.MyorderViewHolder;
import com.example.secondhand.ViewHolder.YourProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YourProducts extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_products);

        recyclerView=findViewById(R.id.YourOrderList);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference myProductRef = FirebaseDatabase.getInstance().getReference().child("My Products");

        FirebaseRecyclerOptions<yourproducts> options=
                new FirebaseRecyclerOptions.Builder<yourproducts>()
                .setQuery(myProductRef
                        .child(Prevalent.CurrentOnlineUsers.getRollno()).child("Products"),yourproducts.class)
                .build();

        FirebaseRecyclerAdapter<yourproducts, YourProductsViewHolder> adapter
                =new FirebaseRecyclerAdapter<yourproducts, YourProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull YourProductsViewHolder holder, int position, @NonNull yourproducts model) {

                holder.productName.setText(model.getProductname());

            }

            @NonNull
            @Override
            public YourProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_product_item,parent,false);
                YourProductsViewHolder holder=new YourProductsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}