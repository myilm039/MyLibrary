package com.app.mylibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CollectionDetailsActivity extends AppCompatActivity {

    TextView titleField, authorsField, numberOfPagesField, publisherField, subtitleField, descriptionField
            ,publishDateField,previewButton,buyButton,viewabilityField;
    static Button deleteButton, saveChangesButton;
    ImageView bookIV;
    RatingBar ratingBar;
    EditText commentField;
    static Book currentBook;
    static Activity currentActivity;
    DatabaseHelper db = new DatabaseHelper(CollectionDetailsActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        titleField = findViewById(R.id.titleField);
        authorsField = findViewById(R.id.authorField);
        subtitleField = findViewById(R.id.subtitleField);
        publisherField = findViewById(R.id.publisherField);
        descriptionField = findViewById(R.id.descriptionField);
        numberOfPagesField = findViewById(R.id.numberOfPagesField);
        publishDateField = findViewById(R.id.publishingDateField);
        previewButton = findViewById(R.id.previewButton);
        buyButton = findViewById(R.id.buyButton);
        bookIV = findViewById(R.id.bookIV);
        viewabilityField = findViewById(R.id.viewabilityField);
        deleteButton = findViewById(R.id.deleteButton);
        ratingBar = findViewById(R.id.ratingBar);
        commentField = findViewById(R.id.commentField);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmDialog();
            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBook.getPreviewLink().isEmpty()){

                    Toast.makeText(CollectionDetailsActivity.this, "No preview Link present", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CollectionDetailsActivity.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.parse(currentBook.getBuyLink());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBook.setRating(ratingBar.getRating());
                currentBook.setComment(commentField.getText().toString());
                if(currentActivity.getTitle()=="My Collection") {
                    db.updateBook("my_collection",currentBook);
                }

                else{

                    db.updateBook("my_wishlist",currentBook);

                }

            }
        });

        bookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(currentBook.getThumbnail());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);            }
        });


        setInfo();

    }

    public static void setActivity(Activity activity){

        currentActivity = activity;

    }

    public static void setCurrentBook(Book book){

        currentBook = book;
    }

    @SuppressLint("SetTextI18n")
    public void setInfo(){

        if(currentActivity.getTitle() == "My Collection" ){
            deleteButton.setText("Remove from collection");
        }

        else if(currentActivity.getTitle() == "My WishList"){
            Log.d("TAG","SCOOCH");
            deleteButton.setText("Remove from wishlist");
            commentField.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            saveChangesButton.setVisibility(View.GONE);


        }
        ratingBar.setRating(currentBook.getRating());
        authorsField.setText(currentBook.getAuthors());
        titleField.setText(currentBook.getTitle());
        publisherField.setText(currentBook.getPublisher());
        publishDateField.setText(currentBook.getPublishedDate());
        numberOfPagesField.setText("Number of pages: "+ String.valueOf(currentBook.getPageCount()));
        viewabilityField.setText("Free Viewability: "+currentBook.getViewability());
        subtitleField.setText(currentBook.getSubtitle());
        descriptionField.setText(currentBook.getDescription());
        commentField.setText(currentBook.getComment());
        Picasso.get().load(currentBook.getThumbnail()).into(bookIV);

    }
    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remove Item");
        if(currentActivity.getTitle()=="My Collection") {
            builder.setMessage("Are you sure you want to delete this item from collection?");
        }

        else if(currentActivity.getTitle()=="My WishList"){
            builder.setMessage("Are you sure you want to delete this item from Wishlist?");

        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(CollectionDetailsActivity.this);


                if(currentActivity.getTitle()=="My Collection") {
                    currentBook.setCollectionStatus(false);
                    db.deleteOneRow("my_collection", String.valueOf(currentBook.getID()));
                }

                else if(currentActivity.getTitle()=="My WishList"){
                    currentBook.setWLStatus(false);
                    db.deleteOneRow("my_wishlist", String.valueOf(currentBook.getID()));

                }
                refreshPage();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void refreshPage(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

}
