package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.secondhand.Models.Products;
import com.example.secondhand.Models.myorder;
import com.example.secondhand.Preval.Prevalent;
import com.example.secondhand.ViewHolder.MyorderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView=findViewById(R.id.myOrderList);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference myOrderRef = FirebaseDatabase.getInstance().getReference().child("MyOrders");

        FirebaseRecyclerOptions<myorder> options=
                new FirebaseRecyclerOptions.Builder<myorder>()
                .setQuery(myOrderRef.child("User View")
                .child(Prevalent.CurrentOnlineUsers.getRollno()).child("Products"),myorder.class)
                        .build();

        FirebaseRecyclerAdapter<myorder, MyorderViewHolder> adapter
                =new FirebaseRecyclerAdapter<myorder, MyorderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyorderViewHolder holder, int position, @NonNull final myorder model)
            {
              holder.productName.setText(model.getProductName());

              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v)
                  {
                      CharSequence options[] = new CharSequence[]
                              {
                                   "Seller Contact",
                                   "Remove"
                              };

                      AlertDialog.Builder builder= new AlertDialog.Builder(MyOrdersActivity.this);
                      builder.setTitle("Request");

                      builder.setItems(options, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which)
                          {

                              if (which==0)
                              {
                                  Intent intent=new Intent(getApplicationContext(),SellerInfo.class);
                                  intent.putExtra("sellerinforollno",model.getSellerrollno());
                                  startActivity(intent);
                              }

                              if (which==1)
                              {
                                  myOrderRef.child("User View")
                                          .child(Prevalent.CurrentOnlineUsers.getRollno())
                                          .child("Products")
                                          .child(model.getPid())
                                          .removeValue()
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task)
                                              {
                                                 if (task.isSuccessful())
                                                 {
                                                     Toast.makeText(MyOrdersActivity.this, "Removed", Toast.LENGTH_SHORT).show();

                                                 }
                                              }
                                          });

                                  myOrderRef.child("Seller View")
                                          .child(Prevalent.CurrentOnlineUsers.getRollno())
                                          .child("Products")
                                          .child(model.getPid())
                                          .removeValue();
                              }

                          }
                      });
                      builder.show();

                  }
              });

            }

            @NonNull
            @Override
            public MyorderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item,parent,false);
                MyorderViewHolder holder=new MyorderViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}