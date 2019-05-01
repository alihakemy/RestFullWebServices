package com.hakemy.linkedin_webservices.RecyclerView;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hakemy.linkedin_webservices.R;

/*

A view holde r---> Inside your adapter, you will create a ViewHolder class that contains the view information for displaying
one item from the item's layout.
 */

// when call this class must pass layout

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView textView ;
    public ImageView imageView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.Rec_text_1);
        imageView=(ImageView) itemView.findViewById(R.id.imageView2);

    }


}
