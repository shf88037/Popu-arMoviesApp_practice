package com.example.android.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private static final int View_TYPE_INFO = 0;
    private static final int View_TYPE_TRAILER = 1;
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private Context context;
    private Movie movie;
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *  @param context        The current context. Used to inflate the layout file.
     * @param trailers A List of Movie objects to display in a list
     */
    public TrailerAdapter(Context context, ArrayList<Trailer> trailers, Movie movie) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, trailers);
        this.context = context;
        this.movie = movie;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Trailer trailer = getItem(position);
        int layoutId = -1;
        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            switch (position) {
                case View_TYPE_INFO:
                    layoutId = R.layout.list_item_movie_info;
                    break;
                default:
                    layoutId = R.layout.list_item_trailers;
                    break;
            }
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }
        if (position == View_TYPE_INFO) {
            setView(convertView);
        } else if (position > View_TYPE_INFO){
            TextView title = (TextView) convertView.findViewById(R.id.list_item_trailer);
            if(trailer != null)
                title.setText(trailer.getmTitle());
        }
        return convertView;
    }

    private void setView (View rootView) {
        ImageView mPoster = ((ImageView) rootView.findViewById(R.id.move_poster));
        TextView mTitle = ((TextView) rootView.findViewById(R.id.movie_title));
        TextView mOverview = ((TextView) rootView.findViewById(R.id.movie_overview));
        TextView mRating = ((TextView) rootView.findViewById(R.id.movie_rating));
        TextView mReleaseYear = ((TextView) rootView.findViewById(R.id.movie_release_date));
        TextView mLength = ((TextView) rootView.findViewById(R.id.movie_length));
        mTitle.setText(movie.getTitle());
        Glide
                .with(getContext())
                .load(movie.getPosterPath())
                .into(mPoster);

        String[] dateSplite = movie.getRelease_date().split("-");
        mOverview.setText(movie.getOverview());
        mRating.setText(movie.getRating() + "/10");
        mReleaseYear.setText(dateSplite[0]);
        mLength.setText(movie.getLength());
    }
}
