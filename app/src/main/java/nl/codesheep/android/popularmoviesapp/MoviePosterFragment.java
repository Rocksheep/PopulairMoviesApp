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

import nl.codesheep.android.popularmoviesapp.data.MovieColumns;
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

    private static boolean sUseWideView = false;


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
        Log.d(LOG_TAG, "I have been created");
        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

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
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        int columns = sUseWideView ? 3 : 2;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columns);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        recyclerView.setAdapter(mMovieAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = MovieProvider.Movies.MOVIES;
        String whereStatement = null;
        String[] whereArgs = null;
        if (mShowFavorites) {
            whereStatement = MovieColumns.IS_FAVORITE + " = 1";
        }
        Log.d(LOG_TAG, "Created loader for URL: " + uri.toString() + "/" + mOrderBy);
        return new CursorLoader(
                getActivity(),
                uri,
                MovieColumns.PROJECTION,
                whereStatement,
                whereArgs,
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
        Log.d(LOG_TAG, "Movie set changed");
        mMovieAdapter.setMovies(mMovies);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static void setUseWideView(boolean useWideView) {
        sUseWideView = useWideView;
    }
}
