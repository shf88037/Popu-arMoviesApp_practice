package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by note_TM on 2016/12/03.
 */
public class TrailerListFragment extends Fragment {

    private Intent receiveIntent;
    private TrailerRecyclerViewAdapter mTrailerRecyclerViewAdapter;
    private Movie receiveMovie;
    private List<Trailer> mTrailers;
    private LoaderManager loaderManager;


    private static final int MOVIE_TRAILERS_LOADER_ID = 2;
    private static final String LOG_TAG = TrailerListFragment.class.getSimpleName();

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            if (receiveMovie != null) {
                return new MovieTrailerLoader(getActivity(), receiveMovie.getId());
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailers) {
            if (trailers != null && !trailers.isEmpty()) {
                mTrailers.clear();
                mTrailers.addAll(trailers);
                mTrailerRecyclerViewAdapter.notifyDataSetChanged();

            }
            for (Trailer trailer : mTrailers)
                Log.i(LOG_TAG, "mTrailerRecyclerViewAdapter " + trailer.getIconUrl());
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            mTrailers.clear();
            mTrailerRecyclerViewAdapter.notifyDataSetChanged();
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

        mTrailers = new ArrayList<>();
        mTrailerRecyclerViewAdapter = new TrailerRecyclerViewAdapter(getActivity(), mTrailers);
        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(MOVIE_TRAILERS_LOADER_ID, null, trailerLoaderListener);
        rootView.setAdapter(mTrailerRecyclerViewAdapter);

        return rootView;
    }

    public static class TrailerRecyclerViewAdapter extends
            RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder> {

        private List<Trailer> mTrailers;
        private Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final ImageView mTrailerIconView;
            public Trailer trailer;

            public ViewHolder(View itemView) {
                super(itemView);
                mTrailerIconView = (ImageView) itemView.findViewById(R.id.list_trailer_icon);
            }
        }

        public TrailerRecyclerViewAdapter(Context context, List<Trailer> items) {
            mContext = context;
            mTrailers = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_trailers, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.trailer = mTrailers.get(position);
            Glide.with(holder.mTrailerIconView.getContext())
                    .load(holder.trailer.getIconUrl())
                    .into(holder.mTrailerIconView);
            holder.mTrailerIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    String url = holder.trailer.getTrailerUrl();
                    Uri link = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, link);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTrailers.size();
        }
        public Trailer getItem(int position) {
            return mTrailers.get(position);
        }
    }
}
