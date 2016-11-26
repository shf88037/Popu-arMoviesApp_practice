package com.example.android.popularmoviesapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by note_TM on 2016/11/26.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {}

    public static List<Movie> fetchMovieData(String requestsUrl) {

        Log.i(LOG_TAG, "call fetchMovieDate. ");

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        String jsonResponse = null;

        URL url = createUrl(requestsUrl);

        jsonResponse = makeHttpRequest(url);

        List<Movie> movies = null;
        try {
            movies = fetchMovieFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        return movies;



    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = bufferReader.readLine();
            while (line != null) {
                output.append(line + "\n");
                line = bufferReader.readLine();
            }
        }
        if (output.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }


    private static List<Movie> fetchMovieFromJson(String movieJSON) throws JSONException  {

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        final String OWM_LIST = "results";
        final String OWM_PATH = "poster_path";
        final String OWM_ID = "id";
        final String OWM_TITLE = "title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RATING = "vote_average";
        final String OWM_RELEASE = "release_date";

        final String BASE_PATH = "http://image.tmdb.org/t/p/w185/";

        JSONObject movieJson = new JSONObject(movieJSON);
        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieInfo = movieArray.getJSONObject(i);
            String posterPath = BASE_PATH + movieInfo.getString(OWM_PATH);
            String title = movieInfo.getString(OWM_TITLE);
            String id = movieInfo.getString(OWM_ID);
            String overview = movieInfo.getString(OWM_OVERVIEW);
            String rating = movieInfo.getString(OWM_RATING);
            String release_date = movieInfo.getString(OWM_RELEASE);
            movies.add(new Movie(id, title, posterPath, overview, rating, release_date));
        }

        return movies;
    }
}
