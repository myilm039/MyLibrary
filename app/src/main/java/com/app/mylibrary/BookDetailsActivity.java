package com.app.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class BookDetailsActivity extends AppCompatActivity {

    TextView titleField, subtitleField, publisherField, descriptionField, numberOfPagesField, publishingDateField, authorsField,
            viewabilityField, genreField;
    Button previewButton, buyButton;
    private ImageView bookIV, bookmarkIV, wishListIV;
    RecyclerView sameCategoryRV;
    static Book currentBook;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleField = findViewById(R.id.titleField);
        subtitleField = findViewById(R.id.subtitleField);
        publisherField = findViewById(R.id.publisherField);
        descriptionField = findViewById(R.id.descriptionField);
        numberOfPagesField = findViewById(R.id.numberOfPagesField);
        publishingDateField = findViewById(R.id.publishingDateField);
        previewButton = findViewById(R.id.previewButton);
        buyButton = findViewById(R.id.buyButton);
        bookIV = findViewById(R.id.bookIV);
        authorsField = findViewById(R.id.authorsField);
        viewabilityField = findViewById(R.id.viewabilityField);
        sameCategoryRV = findViewById(R.id.sameCategoryRV);
        genreField = findViewById(R.id.genreField);
        bookmarkIV = findViewById(R.id.bookmarkIV);
        wishListIV = findViewById(R.id.wishListIV);


        titleField.setText(currentBook.getTitle());
        genreField.setText(currentBook.getCategory());
        subtitleField.setText(currentBook.getSubtitle());
        publisherField.setText(currentBook.getPublisher());
        publishingDateField.setText("Published On : " + currentBook.getPublishedDate());
        descriptionField.setText((currentBook.getDescription()));
        numberOfPagesField.setText("No Of Pages : " + currentBook.getPageCount());
        Picasso.get().load(currentBook.getThumbnail()).into(bookIV);
        authorsField.setText(currentBook.getAuthors());

        viewabilityField.setText(viewabilityField.getText() + ": " + currentBook.getViewability());

        getSameCategoryRVData(currentBook.getCategory());
        setIcons();


        bookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(currentBook.getThumbnail());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBook.getPreviewLink().isEmpty()) {

                    Toast.makeText(BookDetailsActivity.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(currentBook.getPreviewLink());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBook.getBuyLink().isEmpty()) {
                    Toast.makeText(BookDetailsActivity.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.parse(currentBook.getBuyLink());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

            bookmarkIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper db = new DatabaseHelper(BookDetailsActivity.this);
                    if (!currentBook.getCollectionStatus()) {
                        currentBook.setCollectionStatus(true);
                        db.addBook("my_collection", currentBook);
                        setIcons();
                    }

                    else if(currentBook.getCollectionStatus()){
                        currentBook.setCollectionStatus(false);
                        db.deleteOneRow("my_collection", currentBook);
                        setIcons();
                    }
                }
            });


            wishListIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper db = new DatabaseHelper(BookDetailsActivity.this);
                    if (!currentBook.getWLStatus()) {
                        currentBook.setWLStatus(true);
                        db.addBook("my_wishlist", currentBook);
                        setIcons();
                    }

                    else if(currentBook.getWLStatus()){
                        currentBook.setWLStatus(false);
                        db.deleteOneRow("my_wishlist", currentBook);
                        setIcons();
                    }
                }
            });

    }


    public static void setCurrentBook(Book book){

        currentBook = book;
    }

    public void getSameCategoryRVData(String category){
        ArrayList<Book> sameCategoryBooksList= new ArrayList<>() ;
        requestQueue = Volley.newRequestQueue(BookDetailsActivity.this);
        requestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=subject:" + category +"&maxResults=40";
        RequestQueue queue = Volley.newRequestQueue(BookDetailsActivity.this);
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int counter=0;
                while(true){
                    try {
                        if (counter>= response.getJSONArray("items").length()) {
                            break;
                        }

                        if(response.getJSONArray("items").length()==0){

                            return;
                        }
                    }
                    catch (JSONException | NullPointerException e) {

                        e.printStackTrace();
                        break;

                    }
                    try {
                        JSONArray itemsArray = response.getJSONArray("items");

                        while(counter<itemsArray.length()) {

                            JSONObject itemsObj = itemsArray.getJSONObject(counter);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                            String category = volumeObj.getJSONArray("categories").
                                    optString(0);
                            String title = volumeObj.optString("title");
                            String subtitle = volumeObj.optString("subtitle");
                            JSONArray authorsArray = volumeObj.getJSONArray("authors");
                            String publisher = volumeObj.optString("publisher");
                            String publishedDate = volumeObj.optString("publishedDate");
                            String description = volumeObj.optString("description");
                            int pageCount = volumeObj.optInt("pageCount");
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String thumbnail = imageLinks.optString("thumbnail");
                            String previewLink = volumeObj.optString("previewLink");
                            JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                            String buyLink = saleInfoObj.optString("buyLink");
                            JSONObject accessObj = itemsObj.getJSONObject("accessInfo");
                            String viewability = accessObj.optString("viewability");
                            String uniqueID = itemsObj.optString("id");



                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray.length() != 0) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j));
                                }
                            }
                            Book bookInfo = new Book(0,title, subtitle, getAuthors(authorsArrayList), publisher,
                                    publishedDate, description, pageCount, thumbnail, previewLink,
                                    buyLink,viewability,"",0,category, uniqueID);

                            sameCategoryBooksList.add(bookInfo);
                            Collections.shuffle(sameCategoryBooksList);


                            HorizontalRVAdapter adapter = new HorizontalRVAdapter(BookDetailsActivity.this,getApplicationContext(),sameCategoryBooksList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.sameCategoryRV);

                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(adapter);
                            counter++;
                        }

                    }
                    catch (JSONException | NullPointerException e) {
                        counter++;
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailsActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(booksObjrequest);

    }


    public String getAuthors(ArrayList<String> authors){

        String authorsStr ="";

        for(int i=0;i<authors.size();i++){

            authorsStr+=authors.get(i);

            if(i!=authors.size()-1){

                authorsStr+=", ";
            }
        }
        return authorsStr;
    }

    public void setIcons(){

        if(currentBook.getWLStatus()){
            wishListIV.setImageResource(R.drawable.full_heart);
        }

        else if(!currentBook.getWLStatus()){
            wishListIV.setImageResource(R.drawable.empty_heart);

        }

        if(currentBook.getCollectionStatus()){
            bookmarkIV.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }

        else if(!currentBook.getCollectionStatus()){
            bookmarkIV.setImageResource(R.drawable.ic_baseline_bookmark_border_24);

        }

    }





}