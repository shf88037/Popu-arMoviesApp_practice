package com.example.android.popularmoviesapp;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = "test";

    private static final int MOVIE_LOADER_ID = 1;
    private String nowQuery;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;
    private TextView mEmptyStateTextView;
    private View loadingIndicator;
    private RecyclerView mRecycleView;
    private MovieLoader mMovieLoader;
    private LoaderManager loaderManager;
    private List<Movie> dataSet;
    private EndlessRecyclerViewScrollListener srollListener;

    /*Movie[] default_movie = {
            new Movie("Honeycomb", "3.0-3.2.6", "http://image.tmdb.org/t/p/w185//weUSwMdQIa3NaXVzwUoIIcAi85d.jpg"),
            new Movie("Ice Cream Sandwich", "4.0-4.0.4", "http://image.tmdb.org/t/p/w185//m4ZfuML89Rq8gbRGdm9ncSibjlx.jpg"),
            new Movie("Jelly Bean", "4.1-4.3.1", "http://image.tmdb.org/t/p/w185//uPqAW07bGoljf3cmT5gecdOvVol.jpg"),
            new Movie("KitKat", "4.4-4.4.4", "http://image.tmdb.org/t/p/w185//5vfRMplGxOzMiJu0FGFCA1ic44Q.jpg"),
            new Movie("Lollipop", "5.0-5.1.1", "http://image.tmdb.org/t/p/w185//s7OVVDszWUw79clca0durAIa6mw.jpg"),
            new Movie("Cupcake", "1.5", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
            new Movie("Donut", "1.6", "http://image.tmdb.org/t/p/w185//sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"),
            new Movie("Eclair", "2.0-2.1", "http://image.tmdb.org/t/p/w185//inVq3FRqcYIRl2la8iZikYYxFNR.jpg"),
            new Movie("Froyo", "2.2-2.2.3", "http://image.tmdb.org/t/p/w185//pYzHflb8QgQszkR4Ku8mWrJAYfA.jpg"),
            new Movie("GingerBread", "2.3-2.3.7", "http://image.tmdb.org/t/p/w185//zSouWWrySXshPCT4t3UKCQGayyo.jpg"),
    };*/

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular){
            //updateMovie(getString(R.string.query_popular));
            nowQuery = getString(R.string.query_popular);
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_top_rated){
            //updateMovie(getString(R.string.query_top_rated));
            nowQuery = getString(R.string.query_top_rated);
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_now_playing){
            //updateMovie(getString(R.string.query_now_playing));
            nowQuery =  getString(R.string.query_now_playing);
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_upcoming){
            //updateMovie(getString(R.string.query_upcoming));
            nowQuery =  getString(R.string.query_upcoming);
            mMovieLoader = new MovieLoader(getActivity(), nowQuery, 1);
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //private void updateMovie(String sort) {
    //    MovieLoader weatherLoader = new MovieLoader(getActivity(), sort);
    //}

    @Override
    public void onStart(){
        super.onStart();
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
        srollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        mRecycleView.setAdapter(mMovieRecyclerViewAdapter);
        mRecycleView.addOnScrollListener(srollListener);


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
        if(page < 3000) {
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
            srollListener.resetState();
        }

        if (movies != null && !movies.isEmpty()) {
            int curSize = mMovieRecyclerViewAdapter.getItemCount();
            dataSet.addAll(movies);
            mMovieRecyclerViewAdapter.notifyItemRangeChanged(curSize, dataSet.size() - 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        dataSet.clear();
        mMovieRecyclerViewAdapter.notifyDataSetChanged();
        srollListener.resetState();
    }
}

