package com.app.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "LibraryApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String WISHLIST_TABLE_NAME = "my_wishlist";
    private static final String COLLECTION_TABLE_NAME = "my_collection";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHORS = "authors";
    private static final String COLUMN_PAGES = "pages";
    private static final String COLUMN_SUBTITLE = "subtitle";
    private static final String COLUMN_PUBLISHER = "publisher";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PREVIEWLINK = "preview_link";
    private static final String COLUMN_BUYLINK = "buy_link";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_VIEWABILITY = "viewability";
    private static final String COLUMN_THUMBNAIL = "thumbnail";
    private static final String COLUMN_PUBLISHDATE = "publishing_date";
    private static final String COLUMN_CATEGORY = "category";





    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + COLLECTION_TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_SUBTITLE + " TEXT, " +
                COLUMN_AUTHORS + " TEXT, " +
                COLUMN_PUBLISHER + " TEXT, " +
                COLUMN_PUBLISHDATE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_THUMBNAIL + " TEXT, " +
                COLUMN_PREVIEWLINK + " TEXT, " +
                COLUMN_BUYLINK + " TEXT, " +
                COLUMN_VIEWABILITY + " TEXT, " +
                COLUMN_COMMENT + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CATEGORY + " TEXT);";

        String query2 = "CREATE TABLE " + WISHLIST_TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_SUBTITLE + " TEXT, " +
                COLUMN_AUTHORS + " TEXT, " +
                COLUMN_PUBLISHER + " TEXT, " +
                COLUMN_PUBLISHDATE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_THUMBNAIL + " TEXT, " +
                COLUMN_PREVIEWLINK + " TEXT, " +
                COLUMN_BUYLINK + " TEXT, " +
                COLUMN_VIEWABILITY + " TEXT, " +
                COLUMN_COMMENT + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CATEGORY + " TEXT);";



        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WISHLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTION_TABLE_NAME);
        onCreate(db);
    }

    void addBook(String tableName, Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, book.getTitle());
        cv.put(COLUMN_AUTHORS, book.getAuthors());
        cv.put(COLUMN_PAGES, book.getPageCount());
        cv.put(COLUMN_PUBLISHDATE, book.getPublishedDate());
        cv.put(COLUMN_SUBTITLE, book.getSubtitle());
        cv.put(COLUMN_PUBLISHER, book.getPublisher());
        cv.put(COLUMN_DESCRIPTION, book.getDescription());
        cv.put(COLUMN_PREVIEWLINK, book.getPreviewLink());
        cv.put(COLUMN_BUYLINK,book.getBuyLink());
        cv.put(COLUMN_COMMENT, book.getComment());
        cv.put(COLUMN_RATING, book.getRating());
        cv.put(COLUMN_VIEWABILITY, book.getViewability());
        cv.put(COLUMN_THUMBNAIL, book.getThumbnail());
        cv.put(COLUMN_CATEGORY, book.getCategory());


        long result = db.insert(tableName, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(String tableName) {
        String query = "SELECT * FROM " + tableName + " ORDER BY " + COLUMN_TITLE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public void deleteOneRow(String tableName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(id==null){
            throw new NullPointerException("NULL GELDI deleteOneRow metoduna");
        }
        long result = db.delete(tableName, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);
    }

    public void updateBook(String tableName, Book book){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, book.getTitle());
        cv.put(COLUMN_AUTHORS, book.getAuthors());
        cv.put(COLUMN_PAGES, book.getPageCount());
        cv.put(COLUMN_PUBLISHDATE, book.getPublishedDate());
        cv.put(COLUMN_SUBTITLE, book.getSubtitle());
        cv.put(COLUMN_PUBLISHER, book.getPublisher());
        cv.put(COLUMN_DESCRIPTION, book.getDescription());
        cv.put(COLUMN_PREVIEWLINK, book.getPreviewLink());
        cv.put(COLUMN_BUYLINK,book.getBuyLink());
        cv.put(COLUMN_COMMENT, book.getComment());
        cv.put(COLUMN_RATING, book.getRating());
        cv.put(COLUMN_VIEWABILITY, book.getViewability());
        cv.put(COLUMN_THUMBNAIL, book.getThumbnail());
        cv.put(COLUMN_CATEGORY, book.getCategory());

        long result = db.update(tableName, cv, "_id=?", new String[]{String.valueOf(book.getID())});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }








}
