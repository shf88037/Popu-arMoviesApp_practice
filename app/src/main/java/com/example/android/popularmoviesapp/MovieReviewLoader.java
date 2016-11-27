package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Created by note_TM on 2016/11/27.
 */
public class MovieReviewLoader extends AsyncTaskLoader<String[]> {

    private static final String LOG_TAG = MovieRuntimeLoader.class.getSimpleName();
    private String movieId;

    public MovieReviewLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "call onStartLoading");
        forceLoad();
    }

    @Override
    public String[] loadInBackground() {

        String baseUrl = "http://api.themoviedb.org/3/movie/";
        String review = "/reviews";
        String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_API_KEY;
        final String stringUrl = baseUrl.concat(movieId).concat(review).concat(apiKey);
        String[] result = QueryUtils.fetchMovieReview(stringUrl);
        return result;
    }
}
