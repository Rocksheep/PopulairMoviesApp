package nl.codesheep.android.popularmoviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import nl.codesheep.android.popularmoviesapp.data.Movie;
import nl.codesheep.android.popularmoviesapp.data.MovieService;
import nl.codesheep.android.popularmoviesapp.data.Review;
import nl.codesheep.android.popularmoviesapp.rest.ReviewResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_KEY = "movie";
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

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

        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.d(LOG_TAG, "No bundle");
            return rootView;
        }
        Movie movie = bundle.getParcelable("movie");
        if (movie == null) {
            return rootView;
        }

        ViewGroup reviewContainer = (ViewGroup) rootView.findViewById(R.id.review_container);
        retrieveReviews(movie.getMovieId(), inflater, reviewContainer);

        ImageView coverImageView = (ImageView)
                rootView.findViewById(R.id.detail_movie_cover_image_view);

        Picasso.with(getActivity()).load(MovieService.COVER_URL + movie.getCoverUrl()).into(coverImageView);

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

    private void retrieveReviews(long movieId, final LayoutInflater inflater, final ViewGroup parent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService.TheMovieDatabase service =
                retrofit.create(MovieService.TheMovieDatabase.class);

        String apiKey = getString(R.string.api_key);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("api_key", apiKey);

        Call<ReviewResponse> reviewResponseCall = service.reviews(movieId, arguments);
        reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Response<ReviewResponse> response) {
                if(response.body() != null) {
                    for (Review review : response.body().results) {
                        View view = inflater.inflate(R.layout.review, parent, false);
                        TextView textView = (TextView) view.findViewById(R.id.review_text_view);
                        textView.setText(review.content);
                        parent.addView(view);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
