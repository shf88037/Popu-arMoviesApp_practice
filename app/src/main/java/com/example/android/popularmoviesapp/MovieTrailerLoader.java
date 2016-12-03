package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by note_TM on 2016/11/27.
 */
public class MovieTrailerLoader extends AsyncTaskLoader<List<Trailer>> {

    private String movieId;

    public MovieTrailerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        String baseUrl = "http://api.themoviedb.org/3/movie/";
        String video = "/videos";
        String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_API_KEY;
        String stringUrl = baseUrl.concat(movieId).concat(video).concat(apiKey);
        List<Trailer> result = QueryUtils.fetchMovieTrailers(stringUrl);
        return result;
    }

}
