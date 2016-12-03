package com.example.android.popularmoviesapp;

/**
 * Created by note_TM on 2016/12/03.
 */
public class Review {
    private String mAuthor;
    private String mReview;
    public Review(String author, String review) {
        mAuthor = author;
        mReview = review;
    }

    public String getReview() {
        return mReview;
    }
    public String getAuthor() {
        return mAuthor;
    }
}
