package com.app.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizontalRVAdapter extends RecyclerView.Adapter<HorizontalRVAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Book> bookInfoList;
    private Activity activity;

    HorizontalRVAdapter(Activity activity, Context context, ArrayList bookInfoList){
        this.context = context;
        this.activity = activity;
        this.bookInfoList = bookInfoList;

    }


    @NonNull
    @Override
    public HorizontalRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.horizontal_rv_item, parent, false);
        return new HorizontalRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(bookInfoList.get(position).getThumbnail()).into(holder.bookIV);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailsActivity.setCurrentBook(bookInfoList.get(position));
                openBookDetailsActivity();

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookIV;
        RelativeLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIV = itemView.findViewById(R.id.bookIV);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

    }

    public void openBookDetailsActivity(){

        Intent intent = new Intent(context, BookDetailsActivity.class);
        activity.startActivityForResult(intent, 1);
    }



}
