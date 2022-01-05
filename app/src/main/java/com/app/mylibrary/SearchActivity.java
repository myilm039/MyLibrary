package com.app.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<Book> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchField;
    private ImageButton searchButton;
    private LinearLayout genresSVLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar = findViewById(R.id.idLoadingPB);
        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        genresSVLL = findViewById(R.id.genresSVLL);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchField.getText().toString().isEmpty()) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                getBooksInfo(searchField.getText().toString(),"");
            }
        });

        fillSV();
    }

    private void getBooksInfo(String query, String genre) {
        bookInfoArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        requestQueue.getCache().clear();
        String url ="";

        if(genre.equals("") && !query.equals("")){

             url = "https://www.googleapis.com/books/v1/volumes?q=" + query +"&maxResults=40";

        }

        else if(!genre.equals("") && query.equals("")) {
             url = "https://www.googleapis.com/books/v1/volumes?q=subject:" + genre + "&maxResults=40";
        }
        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                int counter=0;
                while(true){
                    try {
                        if (counter>= response.getJSONArray("items").length()) {
                            break;
                        }

                    }
                    catch (JSONException | NullPointerException e) {

                        e.printStackTrace();
                        Toast.makeText(SearchActivity.this, "Something went wrong " + e, Toast.LENGTH_SHORT).show();

                    }
                    try {
                        JSONArray itemsArray = response.getJSONArray("items");

                        while(counter<itemsArray.length())  {
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

                            bookInfoArrayList.add(bookInfo);

                            SearchResultsAdapter adapter = new SearchResultsAdapter(bookInfoArrayList, SearchActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchResultsRV);

                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapter);

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
                Toast.makeText(SearchActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
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

    public void fillSV(){

        genresSVLL.removeAllViewsInLayout();

        List<String> categories = Arrays.asList("antiques & collectibles","architecture","art","bibles","biography & autobiography"
                ,"body, mind & spirit","business & economics","comics & graphic novels","computers","cooking","crafts & hobbies"
                ,"crime","design","drama","education","fiction","foreign language study","games & activities"
                ,"gardening" ,"health & fitness","history","house & home","humor","juvenile fiction","juvenile nonfiction"
                ,"language arts & disciplines","law","literary collections","literary criticism","mathematics","medical"
                ,"music","nature","performing arts","pets","philosophy" ,"photography","poetry","political science"
                , "psychology","reference","religion","science","self-help", "social science","sports & recreation"
                ,"technology & engineering","transportation","travel");

            for(int i=0;i<categories.size();i++){
                Button genreButton = new Button(this);
                int finalI = i;
                genreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBooksInfo("",categories.get(finalI));
                    }
                });
                genreButton.setText(categories.get(i));
                genresSVLL.addView(genreButton);
            }

    }

}