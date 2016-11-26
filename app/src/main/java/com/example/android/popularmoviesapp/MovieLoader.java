package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by note_TM on 2016/11/26.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private String mSortMode;
    private int mPage;

    public MovieLoader(Context context, String sortMode, int page) {
        super(context);
        mSortMode = sortMode;
        mPage = page;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "call onStartLoading");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (TextUtils.isEmpty(mSortMode)) {
            return null;
        }

        String baseUrl = "http://api.themoviedb.org/3/movie/";
        String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_API_KEY;
        String pageQuery = "&page=" + mPage;
        String stringUrl = baseUrl.concat(mSortMode).concat(apiKey).concat(pageQuery);
        Log.i(LOG_TAG, "Query page" + mPage);
        List<Movie> result =  QueryUtils.fetchMovieData(stringUrl);
        return result;
    }
    public int getPage () {
        return mPage;
    }
}
