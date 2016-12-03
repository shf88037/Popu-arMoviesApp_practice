package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by note_TM on 2016/12/03.
 */
public class ReviewListFragment extends Fragment {

    private Intent receiveIntent;
    private ReviewRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private Movie receiveMovie;
    private List<Review> mReviews;
    private LoaderManager loaderManager;


    private static final int MOVIE_REVIEWS_LOADER_ID = 3;
    private static final String LOG_TAG = ReviewListFragment.class.getSimpleName();

    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            if (receiveMovie != null) {
                return new MovieReviewLoader(getActivity(), receiveMovie.getId());
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> trailers) {
            if (trailers != null && !trailers.isEmpty()) {
                mReviews.clear();
                mReviews.addAll(trailers);
                mReviewRecyclerViewAdapter.notifyDataSetChanged();

            }
            for (Review overview : mReviews)
                Log.i(LOG_TAG, "mReviewRecyclerViewAdapter " + overview.getReview());
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            mReviews.clear();
            mReviewRecyclerViewAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView rootView = (RecyclerView) inflater.inflate(
                R.layout.fragment_recycle, container, false);

        receiveIntent = getActivity().getIntent();
        if (receiveIntent != null) {
            receiveMovie = receiveIntent.getParcelableExtra("movie_info");
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(rootView.getContext());
        rootView.setLayoutManager(layoutManager);

        mReviews = new ArrayList<>();
        mReviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(getActivity(), mReviews);
        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(MOVIE_REVIEWS_LOADER_ID, null, reviewLoaderListener);
        rootView.setAdapter(mReviewRecyclerViewAdapter);
        return rootView;
    }

    public static class ReviewRecyclerViewAdapter extends
            RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

        private List<Review> mReviews;
        private Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mAuthor;
            public final TextView mReview;
            public Review review;

            public ViewHolder(View itemView) {
                super(itemView);
                mAuthor = (TextView) itemView.findViewById(R.id.author);
                mReview = (TextView) itemView.findViewById(R.id.review);
            }
        }

        public ReviewRecyclerViewAdapter(Context context, List<Review> items) {
            mContext = context;
            mReviews = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_review, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.review = mReviews.get(position);
            holder.mAuthor.setText(holder.review.getAuthor());
            holder.mReview.setText(holder.review.getReview());
        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }
        public Review getItem(int position) {
            return mReviews.get(position);
        }
    }
}
