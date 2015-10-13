package nl.codesheep.android.popularmoviesapp.sync.fetcher;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.R;
import nl.codesheep.android.popularmoviesapp.data.MovieProvider;
import nl.codesheep.android.popularmoviesapp.models.Video;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;
import nl.codesheep.android.popularmoviesapp.rest.VideoResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class VideoSyncer implements Callback<VideoResponse> {

    private static final String LOG_TAG = VideoSyncer.class.getSimpleName();
    private Context mContext;
    private long mMovieId;

    public VideoSyncer(Context context) {
        mContext = context;
    }

    public VideoSyncer forMovie(long movieId) {
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

        Call<VideoResponse> call = service.videos(mMovieId, arguments);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<VideoResponse> response) {
        if (response.body() == null) return;
        ArrayList<Video> videos = response.body().results;

        ArrayList<ContentValues> values = new ArrayList<>();
        ContentValues[] valueArray = new ContentValues[videos.size()];
        for (Video video : videos) {
            if (video.site.equals("YouTube")) {
                video.movieId = mMovieId;
                Log.d(LOG_TAG, "Syncing for movieId " + mMovieId);
                values.add(video.toContentValues());
            }
        }
        values.toArray(valueArray);
        mContext.getContentResolver().bulkInsert(
                MovieProvider.Videos.VIDEOS,
                valueArray
        );
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
    }
}
