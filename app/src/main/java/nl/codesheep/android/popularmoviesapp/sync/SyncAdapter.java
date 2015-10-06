package nl.codesheep.android.popularmoviesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.R;
import nl.codesheep.android.popularmoviesapp.data.MovieColumns;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Movie;
import nl.codesheep.android.popularmoviesapp.models.Review;
import nl.codesheep.android.popularmoviesapp.rest.MovieResponse;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;
import nl.codesheep.android.popularmoviesapp.sync.fetcher.ReviewSyncer;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync called");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService.TheMovieDatabase service =
                retrofit.create(MovieService.TheMovieDatabase.class);

        String apiKey = getContext().getString(R.string.api_key);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());

        String order = preferences.getString(
                getContext().getString(R.string.pref_order_key),
                getContext().getString(R.string.pref_order_default)
        );

        Map<String, String> arguments = new HashMap<>();
        arguments.put("sort_by", order);
        arguments.put("api_key", apiKey);
        arguments.put("vote_count.gte", "100");

        Call<MovieResponse> movies = service.movies(arguments);
        Log.d(LOG_TAG, "Enqueueing call");
        movies.enqueue(new retrofit.Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response) {
                deletePreviousReviews();
                deletePreviousMovies();
                Log.d(LOG_TAG, "Response received");
                Log.d(LOG_TAG, response.raw().request().urlString());
                if (response.body() != null) {
                    List<Movie> movies = response.body().results;

                    for (Movie movie : movies) {
                        ReviewSyncer reviewSyncer = new ReviewSyncer(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieColumns.MOVIE_ID, movie.getMovieId());
                        contentValues.put(MovieColumns.COVER_URI, movie.getCoverUrl());
                        contentValues.put(MovieColumns.POSTER_URI, movie.getPosterUrl());
                        contentValues.put(MovieColumns.RATING, movie.getRating());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        long timestamp;
                        try {
                            Date parsedDate = dateFormat.parse(movie.getReleaseDate());
                            timestamp = parsedDate.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            timestamp = 0;
                        }
                        contentValues.put(MovieColumns.RELEASE_DATE, timestamp);
                        contentValues.put(MovieColumns.SYNOPSIS, movie.getSynopsis());
                        contentValues.put(MovieColumns.TITLE, movie.getTitle());

                        mContentResolver.insert(
                                MovieProvider.Movies.MOVIES,
                                contentValues
                        );

                        reviewSyncer.forMovie(movie.getMovieId()).sync();
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

    private void deletePreviousMovies() {
        mContentResolver.delete(
                MovieProvider.Movies.MOVIES,
                null,
                null
        );
    }

    public static void initializeSyncAdapter(Context context) {
        Log.d(LOG_TAG, "Initializing sync adapter");
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.account_type)
        );

        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                Log.d(LOG_TAG, "Couldn't create new account");
                return null;
            }

            Log.d(LOG_TAG, "Sending on account created");
            onAccountCreated(context, newAccount);
        }
        else {
            Log.d(LOG_TAG, "Old account spotted");
        }
        return newAccount;
    }

    private static void onAccountCreated(Context context, Account account) {
        Log.d(LOG_TAG, "Account created");
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(
                account,
                context.getString(R.string.content_authority),
                true
        );
//        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Log.d(LOG_TAG, "Syncing immediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Log.d(LOG_TAG, "Getting account for sync request");
        ContentResolver.requestSync(
                getSyncAccount(context),
                context.getString(R.string.content_authority),
                bundle
        );
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.d(LOG_TAG, "Getting account for periodic sync");
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest syncRequest = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(new Bundle())
                    .build();
            ContentResolver.requestSync(syncRequest);
        }
        else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    private void deletePreviousReviews() {
        mContentResolver.delete(
                MovieProvider.Reviews.REVIEWS,
                null,
                null
        );
    }
}
