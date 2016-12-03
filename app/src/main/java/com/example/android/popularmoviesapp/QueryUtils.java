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

    public static List<Trailer> fetchMovieTrailers(String requestsUrl){
        String jsonResponse = null;

        URL url = createUrl(requestsUrl);

        jsonResponse = makeHttpRequest(url);

        List<Trailer> trailers = null;
        try {
            trailers = fetchMovieTrailersFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        return trailers;
    }

    private static List<Trailer> fetchMovieTrailersFromJson(String movieJSON) throws JSONException {

        final String OWM_LIST= "results";
        final String OWN_VIDEO_KEY = "key";

        final String TRAILER_BASE_PATH = "https://www.youtube.com/watch?v=";
        final String TRAILER_ICON_BASE_PATH = "http://img.youtube.com/vi/";
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        JSONObject movieJson = new JSONObject(movieJSON);
        JSONArray videoArray = movieJson.getJSONArray(OWM_LIST);
        List<Trailer> trailers = new ArrayList<>();
        for(int i = 0; i < videoArray.length(); i++) {
            JSONObject videos = videoArray.getJSONObject(i);
            String trailerIconUrl = TRAILER_ICON_BASE_PATH + videos.getString(OWN_VIDEO_KEY) + "/0.jpg";
            Log.i(LOG_TAG, "icon url:" + trailerIconUrl);
            String trailerUrl = TRAILER_BASE_PATH + videos.getString(OWN_VIDEO_KEY);
            trailers.add(new Trailer(trailerIconUrl, trailerUrl));
        }

        return trailers;
    }

    public static List<Review> fetchMovieReview(String requestsUrl) {
        String jsonResponse = null;

        URL url = createUrl(requestsUrl);

        jsonResponse = makeHttpRequest(url);

        List<Review> review = new ArrayList<>();
        try {
            review = fetchMovieReviewFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        return review;
    }

    private static List<Review> fetchMovieReviewFromJson(String movieJSON) throws JSONException {

        final String OWM_LIST= "results";
        final String OWN_AUTHOR = "author";
        final String OWN_CONTENT = "content";

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        JSONObject movieJson = new JSONObject(movieJSON);
        JSONArray reviewsArray = movieJson.getJSONArray(OWM_LIST);
        List<Review> contents = new ArrayList<>();
        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviews = reviewsArray.getJSONObject(i);
            String author = reviews.getString(OWN_AUTHOR);
            String review = reviews.getString(OWN_CONTENT);
            contents.add(new Review(author, review));
        }
        return contents;
    }

    public static int fetchMovieRunTime(String requestsUrl) {
        String jsonResponse = null;

        URL url = createUrl(requestsUrl);

        jsonResponse = makeHttpRequest(url);

        int runtime = 0;
        try {
            runtime = fetchMovieRuntimeFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        return runtime;

    }

    private static int fetchMovieRuntimeFromJson(String movieJSON) throws JSONException {
        int runtime = 120;
        final String OWM_runtime = "runtime";
            JSONObject movieJson = new JSONObject(movieJSON);
            runtime = movieJson.getInt(OWM_runtime);
        return runtime;
    }

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
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_BACKDROP_PATH = "backdrop_path";
        final String OWM_ID = "id";
        final String OWM_TITLE = "title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RATING = "vote_average";
        final String OWM_RELEASE = "release_date";

        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
        final String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w780/";

        JSONObject movieJson = new JSONObject(movieJSON);
        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieInfo = movieArray.getJSONObject(i);
            String posterPath = BASE_POSTER_PATH + movieInfo.getString(OWM_POSTER_PATH);
            String backdropPath = BASE_BACKDROP_PATH + movieInfo.getString(OWM_BACKDROP_PATH);
            String title = movieInfo.getString(OWM_TITLE);
            String id = movieInfo.getString(OWM_ID);
            String overview = movieInfo.getString(OWM_OVERVIEW);
            String rating = movieInfo.getString(OWM_RATING);
            String release_date = movieInfo.getString(OWM_RELEASE);
            movies.add(new Movie(id, title, posterPath, backdropPath, overview, rating, release_date));
        }

        return movies;
    }
}
