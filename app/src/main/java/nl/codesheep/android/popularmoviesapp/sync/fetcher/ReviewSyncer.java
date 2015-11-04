package nl.codesheep.android.popularmoviesapp.sync.fetcher;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.R;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Review;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;
import nl.codesheep.android.popularmoviesapp.rest.ReviewResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ReviewSyncer implements Callback<ReviewResponse> {

    private static final String LOG_TAG = ReviewSyncer.class.getSimpleName();
    long mMovieId;
    Context mContext;

    public ReviewSyncer(Context context) {
        mContext = context;
    }

    public ReviewSyncer forMovie(long movieId) {
        mMovieId = movieId;
        return this;
    }

    public void sync() {
        if (mMovieId == 0) return;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService.TheMovieDatabase service =
                retrofit.create(MovieService.TheMovieDatabase.class);

        String apiKey = mContext.getString(R.string.api_key);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("api_key", apiKey);

        Call<ReviewResponse> call = service.reviews(mMovieId, arguments);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<ReviewResponse> response) {
        if (response.body() == null) return;
        ArrayList<Review> reviews = response.body().results;

        ArrayList<ContentValues> values = new ArrayList<>();
        ContentValues[] valueArray = new ContentValues[reviews.size()];
        for (Review review : reviews) {
            review.movieId = mMovieId;
            Log.d(LOG_TAG, "Syncing for movieId " + mMovieId);
            values.add(review.toContentValues());
        }
        values.toArray(valueArray);
        mContext.getContentResolver().bulkInsert(
                MovieProvider.Reviews.REVIEWS,
                valueArray
        );
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
    }
}
