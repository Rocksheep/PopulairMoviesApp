package nl.codesheep.android.popularmoviesapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.codesheep.android.popularmoviesapp.models.Movie;
import nl.codesheep.android.popularmoviesapp.sync.SyncAdapter;

public class MainActivity extends AppCompatActivity implements MovieAdapter.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(
                                R.id.movie_detail_container,
                                MovieDetailFragment.newInstance(null),
                                DETAILFRAGMENT_TAG
                        )
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MoviePosterFragment.setUseWideView(true);
        }
        else {
            MoviePosterFragment.setUseWideView(false);
        }

        SyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra("movie", movie);
            startActivity(movieDetailIntent);
        }
    }
}
