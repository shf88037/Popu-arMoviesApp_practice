package com.example.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by note_TM on 2016/11/26.
 */
public class MovieProvider extends ContentProvider{
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 200;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return cursor;
            case MOVIE_ID:
                cursor = database.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " =? ",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long _id = database.insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0) {
                    Log.i(LOG_TAG, "insert cursor:" + _id);
                    returnUri = ContentUris.withAppendedId(uri, _id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowDeleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                rowDeleted = database.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowDeleted !=0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
