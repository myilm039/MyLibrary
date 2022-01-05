package com.app.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

    private ArrayList<Book> bookInfoArrayList;
    private Context context;

    public SearchResultsAdapter(ArrayList<Book> bookInfoArrayList, Context context) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Book bookInfo = bookInfoArrayList.get(position);
        holder.titleField.setText(bookInfo.getTitle());
        holder.authorField.setText(bookInfo.getAuthors());
        holder.genreField.setText(bookInfo.getCategory());

        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BookDetailsActivity.class);
                BookDetailsActivity.setCurrentBook(bookInfoArrayList.get(position));
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {

        return bookInfoArrayList.size();
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        TextView titleField, authorField, genreField;
        ImageView bookIV;

        public SearchResultsViewHolder(View itemView) {
            super(itemView);
            titleField = itemView.findViewById(R.id.titleField);
            authorField = itemView.findViewById(R.id.bookAuthorField);
            genreField = itemView.findViewById(R.id.genreField);
            bookIV = itemView.findViewById(R.id.bookIV);


        }
    }
}
