package com.example.secondhand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.secondhand.Models.Products;
import com.example.secondhand.Preval.Prevalent;
import com.example.secondhand.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout myDrawerLayout;
    DatabaseReference productsRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDrawerLayout=findViewById(R.id.drawer_layout);
        NavigationView myNavigationView=findViewById(R.id.nav_view);

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        View headerView= myNavigationView.getHeaderView(0);
        TextView userName= headerView.findViewById(R.id.user_name_nav);
        CircleImageView profile_pic=headerView.findViewById(R.id.profile_image);


        recyclerView=findViewById(R.id.recyler_menu);
        //recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userName.setText(Prevalent.CurrentOnlineUsers.getName());
        Picasso.get().load(Prevalent.CurrentOnlineUsers.getImage()).into(profile_pic);

        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                 if(id==R.id.nav_order)
                 {
                  Intent intent=new Intent(getApplicationContext(),MyOrdersActivity.class);
                  startActivity(intent);
                }
                else if(id==R.id.nav_categories)
                {

                }
                else if (id==R.id.nav_settings)
                {
                   Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                   startActivity(intent);
                }
                else {
                    Paper.book().destroy();

                    Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                myDrawerLayout.closeDrawers();
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        holder.List_ProductName.setText(model.getProductname());
                        Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.List_imageView);
                        Log.i("imageuri",model.getImage());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(),ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }
}