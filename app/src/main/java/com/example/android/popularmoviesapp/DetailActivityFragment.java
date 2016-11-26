package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mReleaseYear;
    private TextView mRating;
    private TextView mLength;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setView(rootView);
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            Movie obj = intent.getParcelableExtra("movie_info");
            mTitle.setText(obj.getTitle());

            Picasso
                    .with(getActivity())
                    .load(obj.getPosterPath())
                    .into(mPoster);
            String[] dateSplite = obj.getRelease_date().split("-");
            mOverview.setText(obj.getOverview());
            mRating.setText(obj.getRating()+"/10");
            mReleaseYear.setText(dateSplite[0]);
        }
        return rootView;
    }

    private void setView (View rootView) {
        mPoster = ((ImageView) rootView.findViewById(R.id.move_poster));
        mTitle = ((TextView) rootView.findViewById(R.id.movie_title));
        mOverview = ((TextView) rootView.findViewById(R.id.movie_overview));
        mRating = ((TextView) rootView.findViewById(R.id.movie_rating));
        mReleaseYear = ((TextView) rootView.findViewById(R.id.movie_release_date));
        mLength = ((TextView) rootView.findViewById(R.id.movie_length));
    }

    private void queryMovieTrailer(String id) {
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... id) {

            if (id.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                String baseUrl = "https://api.themoviedb.org/3/movie/";
                String targetMovie = id + "/video";
                String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_API_KEY;
                URL url = new URL(baseUrl.concat(id[0]).concat(targetMovie).concat(apiKey));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieTrailerUrlFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

        }

    }

    private ArrayList<String> getMovieTrailerUrlFromJson(String movieJsonStr) throws JSONException  {

        final String MOVIE_TRAILER_LIST = "results";
        final String MOVIE_TRAILER_KEY = "key";

        final String BASE_PATH = "https://www.youtube.com/watch?v=";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_TRAILER_LIST);
        ArrayList<String> movieTrailerUrls = new ArrayList<>();

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieInfo = movieArray.getJSONObject(i);
            String movieTrailerUrl = BASE_PATH + movieInfo.getString(MOVIE_TRAILER_KEY);
            movieTrailerUrls.add(movieTrailerUrl);
        }

        return movieTrailerUrls;
    }
}
