package nl.codesheep.android.popularmoviesapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();
    private static final String ARG_ORDER_BY = "order_by";
    private static final String ARG_SHOW_FAVORITES = "show_favorites";
    private static final int LOADER_ID = 0;

    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mMovies = null;
    private String mOrderBy;
    private boolean mShowFavorites;


    public static MoviePosterFragment newInstance(String orderBy, boolean showFavorites) {
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_BY, orderBy);
        args.putBoolean(ARG_SHOW_FAVORITES, showFavorites);

        MoviePosterFragment fragment = new MoviePosterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MoviePosterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovies = new ArrayList<>();

        Bundle args = getArguments();
        String orderBy = "popularity";
        boolean showFavorites = false;
        if (args != null) {
            if (args.containsKey(ARG_ORDER_BY)) {
                orderBy = args.getString(ARG_ORDER_BY);
            }
            if (args.containsKey(ARG_SHOW_FAVORITES)) {
                showFavorites = args.getBoolean(ARG_SHOW_FAVORITES);
            }
        }
        mOrderBy = orderBy;
        mShowFavorites = showFavorites;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        recyclerView.setAdapter(mMovieAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri;
        if (mShowFavorites) {
            uri = MovieProvider.Favorites.FAVORITES;
        }
        else {
            uri = MovieProvider.Movies.MOVIES;
        }
        Log.d(LOG_TAG, "Created loader for URL: " + uri.toString() + "/" + mOrderBy);
        return new CursorLoader(
                getActivity(),
                uri,
                null,
                null,
                null,
                mOrderBy + " DESC LIMIT 20"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mMovies.clear();

        if (cursor.moveToFirst()) {
            do {
                Movie movie = Movie.fromCursor(cursor);
                mMovies.add(movie);
            } while (cursor.moveToNext());
        }
        mMovieAdapter.setMovies(mMovies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
