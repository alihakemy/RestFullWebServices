package com.hakemy.linkedin_webservices.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hakemy.linkedin_webservices.MyJsonClass.JsonFromPojo;
import com.hakemy.linkedin_webservices.R;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashMap;

public class MyRecViewClass extends RecyclerView.Adapter<MyViewHolder> {
// this custom adapter

    private JsonFromPojo jsonFromPojo [] ;
    private Context context ;
    private MyViewHolder myViewHolder ;
    private HashMap<String, Bitmap> Bitmap;

    public MyRecViewClass(JsonFromPojo[] jsonFromPojo, Context context,HashMap<String,Bitmap> bitmap) {
        this.jsonFromPojo = jsonFromPojo;
        this.context = context;
        this.Bitmap=bitmap;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context =viewGroup.getContext();
        int layout_for_list_item = R.layout.recyclerview_units;
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view =layoutInflater.inflate(layout_for_list_item,viewGroup,false);
        myViewHolder =new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.textView.setText(jsonFromPojo[i].getItemName());
        if(!Bitmap.isEmpty()) {
            myViewHolder.imageView.setImageBitmap(Bitmap.get(jsonFromPojo[i].getItemName()));
        }

    }

    @Override
    public int getItemCount() {
        return jsonFromPojo.length;
    }



}
