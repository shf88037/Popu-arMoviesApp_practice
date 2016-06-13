package com.example.android.popularmoviesapp;

/**
 * Created by poornima-udacity on 6/26/15.
 */
public class AndroidMovie {
    String title;
    String posterPath; // drawable reference id
    String overview;
    String rating;
    String release_date;

    public AndroidMovie(String title, String posterPath, String overview, String rating, String release_date)
    {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.release_date = release_date;
    }

}