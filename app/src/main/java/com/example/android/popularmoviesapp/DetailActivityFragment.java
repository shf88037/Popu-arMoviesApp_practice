package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();


        if (intent != null) {
            AndroidMovie obj = intent.getParcelableExtra("movie_info");
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(obj.getTitle());
            ImageView postView = ((ImageView) rootView.findViewById(R.id.move_poster));
            Picasso
                    .with(getActivity())
                    .load(obj.getPosterPath())
                    .fit() // will explain later
                    .into(postView);
            ((TextView) rootView.findViewById(R.id.movie_overview)).setText(obj.getOverview());
            ((TextView) rootView.findViewById(R.id.movie_rating)).setText("RATING: " + obj.getRating());
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText("RELEASE DATE: " + obj.getRelease_date());
        }
        return rootView;
    }
}
