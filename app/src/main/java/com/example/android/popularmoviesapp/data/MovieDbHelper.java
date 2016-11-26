package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by note_TM on 2016/11/26.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "movie.db";
    private final static String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "
            + MovieContract.MovieEntry.TABLE_NAME + " ("
            + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_RATING + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT);";

    private final static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + MovieContract.MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
