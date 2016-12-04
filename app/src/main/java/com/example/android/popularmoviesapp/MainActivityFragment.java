package com.example.android.popularmoviesapp;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int MOVIE_LOADER_ID = 1;
    private String nowQuery;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;
    private TextView mEmptyStateTextView;
    private View loadingIndicator;
    private RecyclerView mRecycleView;
    private MovieLoader mMovieLoader;
    private LoaderManager loaderManager;
    private List<Movie> dataSet;
    private EndlessRecyclerViewScrollListener scrollListener;

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
       //     dataSet = new ArrayList<>();
       // } else {
       //     dataSet = savedInstanceState.getParcelableArrayList("movies");
       // }
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) dataSet);
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                nowQuery = getString(R.string.query_popular);
                mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.action_top_rated:
                nowQuery = getString(R.string.query_top_rated);
                mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.action_now_playing:
                nowQuery =  getString(R.string.query_now_playing);
                mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.action_upcoming:
                nowQuery =  getString(R.string.query_upcoming);
                mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.action_favorite:
                nowQuery = getString(R.string.action_favorite);
                loadMyFavoriteMovies();
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMyFavoriteMovies() {
        String[] projections = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE
        };
        Cursor cursor = getActivity().getContentResolver().query(
                                        MovieContract.MovieEntry.CONTENT_URI,
                                        projections,
                                        null,
                                        null,
                                        null);
        if (cursor.moveToFirst()) {
            dataSet.clear();
            while (cursor.moveToNext()) {
                int columnIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                int columnTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                int columnPosterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                int columnBackdropPathIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
                int columnOverviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                int columnRatingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
                int columnReleaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                String id = cursor.getString(columnIdIndex);
                String title = cursor.getString(columnTitleIndex);
                String posterPath = cursor.getString(columnPosterPathIndex);
                String backdropPath = cursor.getString(columnBackdropPathIdIndex);
                String overview = cursor.getString(columnOverviewIndex);
                String rating = cursor.getString(columnRatingIndex);
                String date = cursor.getString(columnReleaseDateIndex);
                dataSet.add(new Movie(id, title, posterPath, backdropPath, overview, rating, date));
                Log.i(LOG_TAG, "Add cursor:" + title);
                //Toast.makeText(getContext(), "query movie successfully:" + title, Toast.LENGTH_SHORT).show();
            }
            mMovieRecyclerViewAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nowQuery == getString(R.string.action_favorite)) {
            loadMyFavoriteMovies();
            Log.i(LOG_TAG, "Reload my favorite movies");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        dataSet = new ArrayList<>();
        nowQuery = getString(R.string.query_popular);
        loadingIndicator = rootView.findViewById(R.id.loading_spinner);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view_text);
        mMovieRecyclerViewAdapter =
                new MovieRecyclerViewAdapter(getActivity(), dataSet, mEmptyStateTextView);
        loaderManager = getActivity().getLoaderManager();
        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.list_view_movie);
        GridLayoutManager layoutManager =
                new GridLayoutManager(mRecycleView.getContext(), 2, GridLayout.VERTICAL, false);
        mRecycleView.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        mRecycleView.setAdapter(mMovieRecyclerViewAdapter);
        mRecycleView.addOnScrollListener(scrollListener);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        //updateMovie(getString(R.string.query_popular));
        return rootView;

    }

    private void loadNextDataFromApi(int page) {
        if(page < 3000 && nowQuery != getString(R.string.action_favorite)) {
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, page);
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return mMovieLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mEmptyStateTextView.setText(R.string.no_movie);
        loadingIndicator.setVisibility(View.GONE);

        if (mMovieLoader.getPage() == 1) {
            dataSet.clear();
            mMovieRecyclerViewAdapter.notifyDataSetChanged();
            scrollListener.resetState();
        }

        if (movies != null && !movies.isEmpty() && nowQuery != getString(R.string.action_favorite)) {
            int curSize = mMovieRecyclerViewAdapter.getItemCount();
            dataSet.addAll(movies);
            mMovieRecyclerViewAdapter.notifyItemRangeChanged(curSize, dataSet.size() - 1);
            Log.i(LOG_TAG,"Movies Load finish:" + nowQuery);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        dataSet.clear();
        mMovieRecyclerViewAdapter.notifyDataSetChanged();
        scrollListener.resetState();
    }
}

