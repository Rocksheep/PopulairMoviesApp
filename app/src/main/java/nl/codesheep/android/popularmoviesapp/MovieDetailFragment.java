package nl.codesheep.android.popularmoviesapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.codesheep.android.popularmoviesapp.data.FavoriteColumns;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;
import nl.codesheep.android.popularmoviesapp.models.Review;
import nl.codesheep.android.popularmoviesapp.models.Video;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIE_KEY = "movie";
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private static final int LOADER_MOVIE = 0;
    private static final int LOADER_REVIEWS = 1;
    private static final int LOADER_VIDEOS = 2;
    private Movie mMovie;

    private LayoutInflater mLayoutInflater;
    private ViewGroup mReviewsParent;

    private ViewPager mPager;
    private TrailerPagerAdapter mPagerAdapter;

    public static MovieDetailFragment newInstance (Movie movie) {
        MovieDetailFragment detailFragment = new MovieDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_KEY, movie);
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    public MovieDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Movie movie;
        Bundle bundle = getArguments();
        if (bundle != null) {
            movie = bundle.getParcelable(MOVIE_KEY);
        }
        else {
            Bundle extras = getActivity().getIntent().getExtras();
            movie = extras.getParcelable(MOVIE_KEY);
        }
        if (movie == null) {
            return rootView;
        }
        mMovie = movie;
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.favorite_fab);
        if (mMovie.isFavorite()) {
            fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white_48dp));
        }
        else {
            fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_border_white_48dp));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie.isFavorite()) {
                    getActivity().getContentResolver().delete(
                            MovieProvider.Favorites.FAVORITES,
                            FavoriteColumns.MOVIE_KEY + " = ?",
                            new String[]{ Long.toString(mMovie.getMovieId()) }
                    );
                    fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_border_white_48dp));
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteColumns.MOVIE_KEY, mMovie.getMovieId());
                    getActivity().getContentResolver().insert(
                            MovieProvider.Favorites.FAVORITES,
                            contentValues
                    );
                    fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white_48dp));
                }
            }
        });

        getLoaderManager().initLoader(LOADER_REVIEWS, null, this);
        getLoaderManager().initLoader(LOADER_VIDEOS, null, this);

        mPager = (ViewPager) rootView.findViewById(R.id.detail_movie_trailer_pager);
        mPagerAdapter = new TrailerPagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        ViewGroup reviewContainer = (ViewGroup) rootView.findViewById(R.id.review_container);
        mLayoutInflater = inflater;
        mReviewsParent = reviewContainer;

        ImageView coverImageView = (ImageView)
                rootView.findViewById(R.id.detail_movie_cover_image_view);

        Picasso.with(getActivity())
                .load(MovieService.COVER_URL + movie.getCoverUrl()).into(coverImageView);

        ImageView posterImageView = (ImageView)
                rootView.findViewById(R.id.detail_movie_poster_image_view);
        Picasso.with(getActivity())
                .load(MovieService.POSTER_URL + movie.getPosterUrl())
                .placeholder(R.drawable.placeholder)
                .into(posterImageView);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        titleTextView.setText(movie.getTitle());

        TextView releaseDateTextView = (TextView)
                rootView.findViewById(R.id.detail_movie_release_date);
        releaseDateTextView.setText(movie.getReleaseDate());

        TextView synopsisTextView = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
        synopsisTextView.setText(movie.getSynopsis());

        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_movie_rating);
        ratingBar.setRating((float) movie.getRating() / 2);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mMovie == null) return null;

        if (id == LOADER_REVIEWS) {
            return new CursorLoader(
                    getActivity(),
                    MovieProvider.Reviews.fromMovie(mMovie.getMovieId()),
                    null,
                    null,
                    null,
                    null
            );
        }
        if (id == LOADER_VIDEOS) {
            return new CursorLoader(
                    getActivity(),
                    MovieProvider.Videos.fromMovie(mMovie.getMovieId()),
                    null,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == LOADER_REVIEWS) {
            if (!cursor.moveToFirst()) {
                return;
            }
            do {
                Review review = Review.fromCursor(cursor);
                View view = mLayoutInflater.inflate(R.layout.review, mReviewsParent, false);
                TextView textView = (TextView) view.findViewById(R.id.review_text_view);
                textView.setText(review.content);
                mReviewsParent.addView(view);
            } while (cursor.moveToNext());
        }

        if (loader.getId() == LOADER_VIDEOS) {
            if (cursor.moveToFirst()) {
                do {
                    Video video = Video.fromCursor(cursor);
                    mPagerAdapter.add(video);
                } while (cursor.moveToNext());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    
}
