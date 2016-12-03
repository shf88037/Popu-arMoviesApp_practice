package com.example.android.popularmoviesapp;

/**
 * Created by note_TM on 2016/12/03.
 */
public class Trailer {
    private String mTrailerIconUrl;
    private String mTrailerUrl;

    public Trailer(String trailerIconUrl, String trailerUrl) {
        mTrailerIconUrl = trailerIconUrl;
        mTrailerUrl = trailerUrl;
    }

    public String getIconUrl() { return mTrailerIconUrl; }
    public String getTrailerUrl() { return mTrailerUrl; }
}
