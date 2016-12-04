package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.popularmoviesapp.data.MovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Intent receiveIntent;
    private Movie receiveMovie;
    private ImageView mPoster;
    private TextView mOverview;
    private TextView mRating;
    private TextView mReleaseYear;
    private TextView mLength;

    private ImageButton mFavorite;

    private static final int MOVIE_RUNTIME_LOADER_ID = 1;
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private LoaderCallbacks<String> runtimeLoaderListener =
            new LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    if (receiveMovie != null) {
                        return new MovieRuntimeLoader(getActivity(), receiveMovie.getId());
                    }
                    return null;
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String length) {
                    if (!TextUtils.isEmpty(length)) {
                        receiveMovie.setLength(length);
                        mLength.setText(receiveMovie.getLength());
                    }
                }

                @Override
                public void onLoaderReset(Loader<String> loader) {

                }
            };

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setView(rootView);

        receiveIntent = getActivity().getIntent();
        if (receiveIntent != null) {
            receiveMovie = receiveIntent.getParcelableExtra("movie_info");
        }

        Glide
                .with(getContext())
                .load(receiveMovie.getPosterPath())
                .into(mPoster);

        String[] dateSplite = receiveMovie.getRelease_date().split("-");
        mOverview.setText(receiveMovie.getOverview());
        mRating.setText(receiveMovie.getRating() + "/10");
        mReleaseYear.setText(dateSplite[0]);
        mLength.setText(receiveMovie.getLength());
        setMyFavorite(receiveMovie, mFavorite);
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavorite.setSelected(!mFavorite.isSelected());
                if (mFavorite.isSelected()) {
                    addMovieToFavorite(receiveMovie);
                } else {
                    removeMovieFromFavorite();
                }
            }
        });

        LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(MOVIE_RUNTIME_LOADER_ID, null, runtimeLoaderListener);
        return rootView;
    }

    private void removeMovieFromFavorite() {
        Uri uri = Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI, receiveMovie.getId());
        int rowAffected = getActivity().getContentResolver().delete(uri, null, null);
        if (rowAffected == 0) {
            Toast.makeText(getContext(), "Delete Movie failed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Delete Movie successfully", Toast.LENGTH_LONG).show();
        }
    }

    private void addMovieToFavorite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, receiveMovie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, receiveMovie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, receiveMovie.getBackdropPath());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, receiveMovie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, receiveMovie.getRating());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, receiveMovie.getRelease_date());
        getContext().getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI, values);
        Toast.makeText(getContext(), "Add Movie Successfully, mFavorite state" , Toast.LENGTH_LONG).show();

    }

    private void setMyFavorite (Movie movie, ImageButton imageButton) {
        String[] projections = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
        };
        Uri uri = Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI, movie.getId());
        Cursor cursor = getActivity().getContentResolver().query(uri, projections, null, null, null);
        if (cursor.moveToFirst()) {
            imageButton.setSelected(true);
        } else {
            imageButton.setSelected(false);
        }
    }

    private void setView(View rootView) {
        mPoster = ((ImageView) rootView.findViewById(R.id.move_poster));
        mOverview = ((TextView) rootView.findViewById(R.id.movie_overview));
        mRating = ((TextView) rootView.findViewById(R.id.movie_rating));
        mReleaseYear = ((TextView) rootView.findViewById(R.id.movie_release_date));
        mLength = ((TextView) rootView.findViewById(R.id.movie_length));
        mFavorite = (ImageButton) rootView.findViewById(R.id.mark_favorite);
    }
}
