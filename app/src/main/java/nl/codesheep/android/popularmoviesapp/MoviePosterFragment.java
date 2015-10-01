package nl.codesheep.android.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.data.Movie;
import nl.codesheep.android.popularmoviesapp.data.MovieResponse;
import nl.codesheep.android.popularmoviesapp.data.MovieService;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;

    public MoviePosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 2) {
                    return 2;
                }
                return 1;
            }
        });

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        recyclerView.setAdapter(mMovieAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Movie movie = mMovieAdapter.getItem(position);
//                mListener.onItemSelected(movie);
//            }
//        });

        return rootView;
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
                Log.d(LOG_TAG, response.raw().request().urlString());
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
                t.printStackTrace();
            }
        });
    }

}
