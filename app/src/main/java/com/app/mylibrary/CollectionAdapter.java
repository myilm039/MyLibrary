package com.app.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<Book> bookInfoList;
    private ArrayList<Integer> numberList = new ArrayList<>();

    CollectionAdapter(Activity activity, Context context, ArrayList bookInfoList){
        this.activity = activity;
        this.context = context;
        this.bookInfoList = bookInfoList;
        fillNumberList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.titleField.setText(String.valueOf(bookInfoList.get(position).getTitle()));
        holder.authorField.setText(String.valueOf(bookInfoList.get(position).getAuthors()));
        holder.genreField.setText(bookInfoList.get(position).getCategory());
        holder.descriptionField.setText(bookInfoList.get(position).getShortDescription());
        Picasso.get().load(bookInfoList.get(position).getThumbnail()).into(holder.bookIV);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionDetailsActivity.setActivity(activity);
                CollectionDetailsActivity.setCurrentBook(bookInfoList.get(position));
                openCollectionDetailsActivity(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView authorField, genreField, titleField, descriptionField ;
        ImageView bookIV;
        RelativeLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleField = itemView.findViewById(R.id.titleField);
            genreField = itemView.findViewById(R.id.genreField);
            authorField = itemView.findViewById(R.id.bookAuthorField);
            bookIV = itemView.findViewById(R.id.bookIV);
            descriptionField = itemView.findViewById(R.id.shortDescriptionField);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

    }

    public void fillNumberList(){

        for(int i=1;i<bookInfoList.size()+1;i++){

            numberList.add(i);
        }

    }

    public void openCollectionDetailsActivity(int position){

        Intent intent = new Intent(context, CollectionDetailsActivity.class);
        activity.startActivityForResult(intent, 1);

    }

}
