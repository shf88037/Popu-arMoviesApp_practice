package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

public class MainActivityFragment extends Fragment {

    private AndroidMovieAdapter mMovieAdapter;
    /*AndroidMovie[] default_movie = {
            new AndroidMovie("Honeycomb", "3.0-3.2.6", "http://image.tmdb.org/t/p/w185//weUSwMdQIa3NaXVzwUoIIcAi85d.jpg"),
            new AndroidMovie("Ice Cream Sandwich", "4.0-4.0.4", "http://image.tmdb.org/t/p/w185//m4ZfuML89Rq8gbRGdm9ncSibjlx.jpg"),
            new AndroidMovie("Jelly Bean", "4.1-4.3.1", "http://image.tmdb.org/t/p/w185//uPqAW07bGoljf3cmT5gecdOvVol.jpg"),
            new AndroidMovie("KitKat", "4.4-4.4.4", "http://image.tmdb.org/t/p/w185//5vfRMplGxOzMiJu0FGFCA1ic44Q.jpg"),
            new AndroidMovie("Lollipop", "5.0-5.1.1", "http://image.tmdb.org/t/p/w185//s7OVVDszWUw79clca0durAIa6mw.jpg"),
            new AndroidMovie("Cupcake", "1.5", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
            new AndroidMovie("Donut", "1.6", "http://image.tmdb.org/t/p/w185//sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"),
            new AndroidMovie("Eclair", "2.0-2.1", "http://image.tmdb.org/t/p/w185//inVq3FRqcYIRl2la8iZikYYxFNR.jpg"),
            new AndroidMovie("Froyo", "2.2-2.2.3", "http://image.tmdb.org/t/p/w185//pYzHflb8QgQszkR4Ku8mWrJAYfA.jpg"),
            new AndroidMovie("GingerBread", "2.3-2.3.7", "http://image.tmdb.org/t/p/w185//zSouWWrySXshPCT4t3UKCQGayyo.jpg"),
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
            updateMovie(getString(R.string.query_popular));
            return true;
        }
        if (id == R.id.action_top_rated){
            updateMovie(getString(R.string.query_top_rated));
            return true;
        }
        if (id == R.id.action_now_playing){
            updateMovie(getString(R.string.query_now_playing));
            return true;
        }
        if (id == R.id.action_upcoming){
            updateMovie(getString(R.string.query_upcoming));
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovie(String sort) {
        FetchMovieTask weatherTask = new FetchMovieTask();
        weatherTask.execute(sort);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new AndroidMovieAdapter(getActivity(), new ArrayList<AndroidMovie>());

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.list_view_movie);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AndroidMovie movieData = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie_info", movieData);
                startActivity(intent);
            }
        });
        updateMovie(getString(R.string.query_popular));
        return rootView;

    }

    public class FetchMovieTask extends AsyncTask<String, Void, AndroidMovie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected AndroidMovie[] doInBackground(String... sortMode) {

            if (sortMode.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/";
                String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_API_KEY;
                URL url = new URL(baseUrl.concat(sortMode[0]).concat(apiKey));

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
                return getMoviePictureFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AndroidMovie[] result) {
            if (result != null) {
            mMovieAdapter.clear();
            for (AndroidMovie movieInfo : result) {
                mMovieAdapter.add(movieInfo);
            }
                }
            }

        }

    private AndroidMovie[] getMoviePictureFromJson(String movieJsonStr) throws JSONException  {

        final String OWM_LIST = "results";
        final String OWM_PATH = "poster_path";
        final String OWM_TITLE = "title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RATING = "vote_average";
        final String OWM_RELEASE = "release_date";

        final String BASE_PATH = "http://image.tmdb.org/t/p/w185/";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);
        AndroidMovie[] movie = new AndroidMovie[movieArray.length()];

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieInfo = movieArray.getJSONObject(i);
            String posterPath = BASE_PATH + movieInfo.getString(OWM_PATH);
            String title = movieInfo.getString(OWM_TITLE);
            String overview = movieInfo.getString(OWM_OVERVIEW);
            String rating = movieInfo.getString(OWM_RATING);
            String release_date = movieInfo.getString(OWM_RELEASE);
            movie[i] = new AndroidMovie(title, posterPath, overview, rating, release_date);
        }

        return movie;
    }
}

