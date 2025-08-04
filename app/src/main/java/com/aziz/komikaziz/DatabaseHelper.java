package com.aziz.komikaziz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "comic_database";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_HISTORY = "reading_history";

    // Favorite table columns
    private static final String KEY_ID = "id";
    private static final String KEY_COMIC_ID = "comic_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_RATING = "rating";
    private static final String KEY_DATE_ADDED = "date_added";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_COMIC_ID + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_AUTHOR + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_RATING + " REAL,"
                + KEY_DATE_ADDED + " TEXT"
                + ")";

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_COMIC_ID + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DATE_ADDED + " TEXT"
                + ")";

        db.execSQL(CREATE_FAVORITES_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    // Insert comic to favorites
    public void addFavorite(String comicId, String title, String author, String imageUrl, float rating, String dateAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMIC_ID, comicId);
        values.put(KEY_TITLE, title);
        values.put(KEY_AUTHOR, author);
        values.put(KEY_IMAGE_URL, imageUrl);
        values.put(KEY_RATING, rating);
        values.put(KEY_DATE_ADDED, dateAdded);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    // Get all favorite comics
    public List<Comic> getAllFavorites() {
        List<Comic> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM favorites ORDER BY date_added DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Comic comic = new Comic();
                comic.setComicId(cursor.getString(cursor.getColumnIndexOrThrow("comic_id")));
                comic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                comic.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                comic.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("image_url")));
                comic.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
                list.add(comic);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Check if a comic is in favorites
    public boolean isFavorite(String comicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{KEY_ID},
                KEY_COMIC_ID + "=?",
                new String[]{comicId}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Delete from favorites
    public void removeFavorite(String comicId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_COMIC_ID + "=?", new String[]{comicId});
        db.close();
    }

    // Add to history
    public void addToHistory(String comicId, String title, String dateAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMIC_ID, comicId);
        values.put(KEY_TITLE, title);
        values.put(KEY_DATE_ADDED, dateAdded);
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    // Get all reading history
    public List<String> getReadingHistory() {
        List<String> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + KEY_DATE_ADDED + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
