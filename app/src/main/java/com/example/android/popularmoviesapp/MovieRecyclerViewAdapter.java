package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by note_TM on 2016/11/26.
 */
public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private TextView mEmptyView;
    private List<Movie> mMovies;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mIconView;
        public Movie movie;

        public ViewHolder(View itemView) {
            super(itemView);
            mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
        }
    }

    public MovieRecyclerViewAdapter(Context context, List<Movie> items, TextView emptyView) {
        mContext = context;
        mMovies = items;
        mEmptyView = emptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.movie = mMovies.get(position);
        Glide.with(holder.mIconView.getContext())
                .load(holder.movie.getPosterPath())
                .into(holder.mIconView);
        holder.mIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie_info", holder.movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        mEmptyView.setVisibility(mMovies.size() > 0 ? View.GONE : View.VISIBLE);
        return mMovies.size();
    }
    public Movie getItem(int position) {
        return mMovies.get(position);
    }
}
