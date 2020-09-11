package com.example.secondhand.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.secondhand.Interfaces.ItemClickListner;
import com.example.secondhand.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView List_ProductName;
    public ImageView List_imageView;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        List_imageView = (ImageView) itemView.findViewById(R.id.item_image);
        List_ProductName = (TextView) itemView.findViewById(R.id.product_name_list);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}


