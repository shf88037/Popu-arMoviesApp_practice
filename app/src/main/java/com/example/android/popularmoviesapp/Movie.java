package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by poornima-udacity on 6/26/15.
 */
public class Movie implements Parcelable {
    private String id;
    private String title;
    private String posterPath; // drawable reference id
    private String backdropPath;
    private String overview;
    private String rating;
    private String release_date;
    private String length;

    public Movie(String id, String title, String posterPath, String backdropPath, String overview, String rating, String release_date) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.rating = rating;
        this.release_date = release_date;
        this.length = null;
    }
    public Movie(Parcel in) {
        Log.d("TestParcel","SourceObject(Parcel in)");
        readFromParcel(in);
    }

    public int describeContents () {
        Log.d("TestParcel","describeContents()");
        return 0;
    }

    public void writeToParcel (Parcel dest, int flags) {
        Log.d("TestParcel","writeToParcel()");
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(release_date);
    }

    private void readFromParcel (Parcel in) {
        Log.d("TestParcel","readFromParcel()");
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        rating = in.readString();
        release_date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            Log.d("TestParcel","createFromParcel()");
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            Log.d("TestParcel","newArray()");
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getBackdropPath() { return backdropPath; }
    public String getOverview() {
        return overview;
    }
    public String getRating() {
        return rating;
    }
    public String getRelease_date() {
        return release_date;
    }
    public String getLength() {return length;}
    public void setLength(String length) {this.length = length;}

}