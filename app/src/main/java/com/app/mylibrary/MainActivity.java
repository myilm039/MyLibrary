package com.app.mylibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addButton;
    RecyclerView recyclerView;
    private ArrayList<Book> bookInfoArrayList = new ArrayList<>();
    CollectionAdapter collectionAdapter;
    static boolean isCollection=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addButton = findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recyclerView);
        bookInfoArrayList.clear();

        if(isCollection) {
            setTitle("My Collection");

            storeData("my_collection");
        }

        else{

            setTitle("My WishList");
            storeData("my_wishlist");
        }

        collectionAdapter = new CollectionAdapter(MainActivity.this,this, bookInfoArrayList);
        recyclerView.setAdapter(collectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.delete_all);


        if(getTitle()=="My Collection"){
            SpannableString s = new SpannableString("Clear Collection");
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            menuItem.setTitle(s);
        }

        if(getTitle()=="My WishList"){
            SpannableString s = new SpannableString("Clear WishList");
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            menuItem.setTitle(s); }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.my_wishlist){

            if(getTitle()=="My WishList"){

                return true;
            }
            MainActivity.setPageType(false);
            refreshPage();
        }

        else if(item.getItemId() == R.id.my_collection){

            if(getTitle()=="My Collection"){

                return true;
            }

            MainActivity.setPageType(true);
            refreshPage();
        }

        else if(item.getItemId() == R.id.delete_all){
            confirmDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    public void openSearchActivity(){

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void refreshPage(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public static void setPageType(boolean pageType){

        isCollection=pageType;

    }


    public void storeData(String tableName){

        try {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Cursor cursor = db.readAllData(tableName);

            if (cursor.getCount() == 0) {
                return;
            }

            while (cursor.moveToNext()) {

                bookInfoArrayList.add(new Book(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getFloat(13),
                        cursor.getString(14),
                        cursor.getString(15)));

            }
            cursor.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onBackPressed() {
        if(getTitle()=="My Collection"){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        else if(getTitle()=="My WishList"){
            finish();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete All?");
        if(getTitle()=="My Collection") {
            builder.setMessage("Are you sure you want to clear Collection?");
        }

        else if(getTitle()=="My WishList"){
            builder.setMessage("Are you sure you want to clear Wishlist?");

        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(MainActivity.this);

                 if(getTitle()=="My WishList"){
                    myDB.deleteAllData("my_wishlist");
                }

                else if(getTitle()=="My Collection"){
                    myDB.deleteAllData("my_collection");
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
}
