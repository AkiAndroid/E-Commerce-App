package com.example.secondhand.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhand.Interfaces.ItemClickListner;
import com.example.secondhand.R;

public class YourProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productName;
    public ItemClickListner itemClickListner;

    public YourProductsViewHolder(@NonNull View itemView)
    {
        super(itemView);
        productName=itemView.findViewById(R.id.my_order_product_name);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }

}
