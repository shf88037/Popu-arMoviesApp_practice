package com.example.android.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by note_TM on 2016/11/26.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.movies";
    public static final Uri BASE_CONTENT_URT = Uri.parse("content://" + CONTENT_AUTHORITY);

    private MovieContract(){}

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite_movie";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path"; // drawable reference path
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "date";
    }

}
