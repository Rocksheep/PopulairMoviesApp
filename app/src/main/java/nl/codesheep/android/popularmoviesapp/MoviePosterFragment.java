package nl.codesheep.android.popularmoviesapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mMovies = null;

    public static MoviePosterFragment newInstance() {

        Bundle args = new Bundle();

        MoviePosterFragment fragment = new MoviePosterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MoviePosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        loadMovies();
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

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

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadMovies() {

        if (mMovies == null) {
            mMovies = new ArrayList<>();
        }

        Cursor cursor = getActivity().getContentResolver().query(
                MovieProvider.Movies.MOVIES,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Movie movie = Movie.fromCursor(cursor);
                mMovies.add(movie);
            } while (cursor.moveToNext());
        }
    }

}
