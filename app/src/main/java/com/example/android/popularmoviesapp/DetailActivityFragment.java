package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

    private static final int MOVIE_RUNTIME_LOADER_ID = 1;
    private static final int MOVIE_OVERVIEW_LOADER_ID = 3;
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

        LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(MOVIE_RUNTIME_LOADER_ID, null, runtimeLoaderListener);
        return rootView;
    }

    private void setView(View rootView) {
        mPoster = ((ImageView) rootView.findViewById(R.id.move_poster));
        mOverview = ((TextView) rootView.findViewById(R.id.movie_overview));
        mRating = ((TextView) rootView.findViewById(R.id.movie_rating));
        mReleaseYear = ((TextView) rootView.findViewById(R.id.movie_release_date));
        mLength = ((TextView) rootView.findViewById(R.id.movie_length));
    }
}
