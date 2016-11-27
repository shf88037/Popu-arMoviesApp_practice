package com.example.android.popularmoviesapp;

/**
 * Created by note_TM on 2016/11/27.
 */

public class Trailer {
    private String mTitle;
    private String mUrl;
    public Trailer(String title, String url) {
        mTitle = title;
        mUrl = url;
    }

    public String getmUrl() {
        return mUrl;
    }
    public String getmTitle() {
        return mTitle;
    }
}
