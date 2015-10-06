package nl.codesheep.android.popularmoviesapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.codesheep.android.popularmoviesapp.data.MovieColumns;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mMovies = null;

    public MoviePosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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
    }

    private void loadMovies() {

        if (mMovies == null) {
            mMovies = new ArrayList<>();
        }

        Cursor cursor = getActivity().getContentResolver().query(
                MovieProvider.Movies.MOVIES,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                Date date = new Date(cursor.getLong(COL_MOVIE_RELEASE_DATE));
                Movie movie = new Movie(
                        cursor.getLong(COL_MOVIE_ID),
                        cursor.getString(COL_MOVIE_TITLE),
                        cursor.getString(COL_MOVIE_POSTER_URI),
                        cursor.getString(COL_MOVIE_COVER_URI),
                        cursor.getString(COL_MOVIE_SYNOPSIS),
                        cursor.getDouble(COL_MOVIE_RATING),
                        dateFormat.format(date)
                );
                mMovies.add(movie);
            } while (cursor.moveToNext());
        }
    }

    private static final String[] MOVIE_COLUMNS = {
            MovieColumns._ID,
            MovieColumns.TITLE,
            MovieColumns.SYNOPSIS,
            MovieColumns.RELEASE_DATE,
            MovieColumns.RATING,
            MovieColumns.POSTER_URI,
            MovieColumns.COVER_URI
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_TITLE = 1;
    static final int COL_MOVIE_SYNOPSIS = 2;
    static final int COL_MOVIE_RELEASE_DATE = 3;
    static final int COL_MOVIE_RATING = 4;
    static final int COL_MOVIE_POSTER_URI = 5;
    static final int COL_MOVIE_COVER_URI = 6;

}
