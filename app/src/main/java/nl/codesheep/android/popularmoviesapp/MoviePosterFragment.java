package nl.codesheep.android.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.data.Movie;
import nl.codesheep.android.popularmoviesapp.data.MovieResponse;
import nl.codesheep.android.popularmoviesapp.data.MovieService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private Callback mListener;

    public MoviePosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid_view);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                mListener.onItemSelected(movie);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService.TheMovieDatabase service =
                retrofit.create(MovieService.TheMovieDatabase.class);

        String apiKey = getString(R.string.api_key);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        String order = preferences.getString(
                getString(R.string.pref_order_key),
                getString(R.string.pref_order_default)
        );

        Map<String, String> arguments = new HashMap<>();
        arguments.put("sort_by", order);
        arguments.put("api_key", apiKey);
        arguments.put("vote_count.gte", "100");

        Call<MovieResponse> movies = service.movies(arguments);
        movies.enqueue(new retrofit.Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response) {
                if (response.body() != null) {
                    List<Movie> movies = response.body().results;
                    for (Movie movie : movies) {
                        mMovieAdapter.add(movie);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Could not create entries");
            }
        });
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }
}
