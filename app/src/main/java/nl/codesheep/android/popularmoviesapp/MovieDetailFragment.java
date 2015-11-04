package nl.codesheep.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.data.FavoriteColumns;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;
import nl.codesheep.android.popularmoviesapp.models.Review;
import nl.codesheep.android.popularmoviesapp.models.Video;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIE_KEY = "movie";
    private static final String SCROLLVIEW_POSITION_KEY = "scroll_position";
    private static final String VIEWPAGER_POSITION_KEY = "pager_position";
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private static final int LOADER_REVIEWS = 1;
    private static final int LOADER_VIDEOS = 2;
    private Movie mMovie;
    private ScrollView mScrollView;
    private int mPosition;
    private int mPagerPosition;

    private LayoutInflater mLayoutInflater;
    private ViewGroup mReviewsParent;

    private ViewPager mPager;
    private TrailerPagerAdapter mPagerAdapter;
    private ShareActionProvider mShareActionProvider;
    private ArrayList<Video> mVideos;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPosition = 0;
        mPagerAdapter = new TrailerPagerAdapter(getChildFragmentManager());
        mVideos = new ArrayList<>();

        if (savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(VIEWPAGER_POSITION_KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent shareIntent = createShareIntent();
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");

        String shareText;
        if (mVideos.size() > 0) {
            shareText = String.format(
                    getString(R.string.share_with_trailer_text),
                    mMovie.getTitle(),
                    mVideos.get(0).getVideoUrl());
        }
        else {
            shareText = String.format(getString(R.string.share_text), mMovie.getTitle());
        }
        Log.d(LOG_TAG,shareText);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        return intent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(MOVIE_KEY);
            mPosition = savedInstanceState.getInt(SCROLLVIEW_POSITION_KEY);
        }
        else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mMovie = bundle.getParcelable(MOVIE_KEY);
            } else {
                Bundle extras = getActivity().getIntent().getExtras();
                mMovie = extras.getParcelable(MOVIE_KEY);
            }
        }
        if (mMovie == null) {
            return rootView;
        }

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.favorite_fab);
        if (mMovie.isFavorite()) {

            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white_48dp));
        }
        else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_white_48dp));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie.isFavorite()) {
                    getActivity().getContentResolver().delete(
                            MovieProvider.Favorites.FAVORITES,
                            FavoriteColumns.MOVIE_KEY + " = ?",
                            new String[]{Long.toString(mMovie.getMovieId())}
                    );
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_white_48dp));
                    mMovie.setIsFavorite(false);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteColumns.MOVIE_KEY, mMovie.getMovieId());
                    getActivity().getContentResolver().insert(
                            MovieProvider.Favorites.FAVORITES,
                            contentValues
                    );
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white_48dp));
                    mMovie.setIsFavorite(true);
                }
            }
        });

        mPager = (ViewPager) rootView.findViewById(R.id.detail_movie_trailer_pager);
        mPager.setAdapter(mPagerAdapter);

        ViewGroup reviewContainer = (ViewGroup) rootView.findViewById(R.id.review_container);
        mLayoutInflater = inflater;
        mReviewsParent = reviewContainer;

        ImageView coverImageView = (ImageView)
                rootView.findViewById(R.id.detail_movie_cover_image_view);

        Picasso.with(getActivity())
                .load(MovieService.COVER_URL + mMovie.getCoverUrl())
                .placeholder(R.drawable.backdrop_placeholder).into(coverImageView);

        ImageView posterImageView = (ImageView)
                rootView.findViewById(R.id.detail_movie_poster_image_view);
        Picasso.with(getActivity())
                .load(MovieService.POSTER_URL + mMovie.getPosterUrl())
                .placeholder(R.drawable.placeholder)
                .into(posterImageView);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        titleTextView.setText(mMovie.getTitle());

        TextView releaseDateTextView = (TextView)
                rootView.findViewById(R.id.detail_movie_release_date);
        releaseDateTextView.setText(mMovie.getReleaseDate());

        TextView synopsisTextView = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
        synopsisTextView.setText(mMovie.getSynopsis());

        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_movie_rating);
        ratingBar.setRating((float) mMovie.getRating() / 2);

        getLoaderManager().initLoader(LOADER_REVIEWS, null, this);
        getLoaderManager().initLoader(LOADER_VIDEOS, null, this);

        mScrollView = (ScrollView) rootView;
        Log.d(LOG_TAG, "Scrolling to " + mPosition);
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.scrollTo(0, mPosition);
            }
        });

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
            mReviewsParent.removeAllViews();
            do {
                Review review = Review.fromCursor(cursor);
                View view = mLayoutInflater.inflate(R.layout.review, mReviewsParent, false);
                TextView textView = (TextView) view.findViewById(R.id.review_text_view);
                textView.setText(review.content);
                ((TextView) view.findViewById(R.id.review_author_text_view)).setText(review.author);
                mReviewsParent.addView(view);
            } while (cursor.moveToNext());
        }

        if (loader.getId() == LOADER_VIDEOS) {
            mPagerAdapter.clear();
            mVideos.clear();
            if (cursor.moveToFirst()) {
                do {
                    Video video = Video.fromCursor(cursor);
                    mPagerAdapter.add(video);
                    mVideos.add(video);
                } while (cursor.moveToNext());
            }
            mPager.setCurrentItem(mPagerPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_KEY, mMovie);
        outState.putInt(SCROLLVIEW_POSITION_KEY, mScrollView.getScrollY());
        outState.putInt(VIEWPAGER_POSITION_KEY, mPager.getCurrentItem());
        Log.d(LOG_TAG, "saving " + mScrollView.getScrollY());
        super.onSaveInstanceState(outState);
    }


}
