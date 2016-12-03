package com.example.android.popularmoviesapp;

/**
 * Created by note_TM on 2016/11/27.
 */

public class Trailer {
    private String mIconUrl;
    private String mTrailerUrl;
    public Trailer(String iconUrl, String trailerUrl) {
        mIconUrl = iconUrl;
        mTrailerUrl = trailerUrl;
    }

    public String getTrailerUrl() {
        return mTrailerUrl;
    }
    public String getIconUrl() {
        return mIconUrl;
    }
}
