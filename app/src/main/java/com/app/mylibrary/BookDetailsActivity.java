package com.app.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    Button previewButton, buyButton, markAsReadButton, wishListButton;
    private ImageView bookIV;
    RecyclerView sameCategoryRV;
    static Book currentBook;
    private RequestQueue requestQueue;
    DatabaseHelper db = new DatabaseHelper(BookDetailsActivity.this);


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
        markAsReadButton = findViewById(R.id.markAsReadButton);
        wishListButton = findViewById(R.id.wishListButton);
        authorsField = findViewById(R.id.authorsField);
        viewabilityField = findViewById(R.id.viewabilityField);
        sameCategoryRV = findViewById(R.id.sameCategoryRV);
        genreField = findViewById(R.id.genreField);

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
        System.out.println(currentBook.getAuthors().split(",")[0]);


        bookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(currentBook.getThumbnail());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBook.getPreviewLink().isEmpty()){

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

        markAsReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addBook("my_collection",currentBook);
                markAsReadButton.setEnabled(false);
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addBook("my_wishlist",currentBook);
                wishListButton.setEnabled(false);}
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
                            Log.d("HATA", "ilk if e girdi");
                            break;
                        }

                        if(response.getJSONArray("items").length()==0){

                            Log.d("NOPE", "Hicbir alakali kitap bulunamadi");
                            return;
                        }
                    }
                    catch (JSONException | NullPointerException e) {

                        e.printStackTrace();
                        Log.d("HATA", "JSONException veya NullPointerException a girdi");

                    }
                    try {
                        JSONArray itemsArray = response.getJSONArray("items");
                        System.out.println(itemsArray.length());

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


                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray.length() != 0) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j));
                                }
                            }
                            Book bookInfo = new Book(0,title, subtitle, getAuthors(authorsArrayList), publisher,
                                    publishedDate, description, pageCount, thumbnail, previewLink,
                                    buyLink,viewability,"",0,category);

                            sameCategoryBooksList.add(bookInfo);
                            Collections.shuffle(sameCategoryBooksList);

                            Log.d("SUCCESS", String.valueOf(sameCategoryBooksList.size()));

                            HorizontalRVAdapter adapter = new HorizontalRVAdapter(BookDetailsActivity.this,getApplicationContext(),sameCategoryBooksList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.sameCategoryRV);

                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(adapter);
                            counter++;
                        }

                    }
                    catch (JSONException | NullPointerException e) {
                        Log.d("HATA", String.valueOf(e));
                        System.out.println(e.getStackTrace()[0]);

                        counter++;
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HATA", "onErrorResponse a girdi");
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




}