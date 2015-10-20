package nl.codesheep.android.popularmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
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
                                new PosterFragment(),
                                DETAILFRAGMENT_TAG
                        )
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }

        String whereStatement = "is_favorite = ?";
        String[] whereArgs = new String[] { "1" };

        Cursor cursor = getContentResolver().query(
                MovieProvider.Favorites.FAVORITES,
                null,
                null,
                null,
                null
        );

        Log.d(LOG_TAG, "Num of favorites: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Movie movie = Movie.fromCursor(cursor);
                Log.d(LOG_TAG, "Favorite movie: " + movie.getTitle());
            } while (cursor.moveToNext());
        }
        cursor.close();

        SyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
