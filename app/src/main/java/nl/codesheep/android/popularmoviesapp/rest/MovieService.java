package nl.codesheep.android.popularmoviesapp.rest;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

public final class MovieService {
    public static final String API_URL = "http://api.themoviedb.org/3/";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String COVER_URL = "http://image.tmdb.org/t/p/w780";

    public interface TheMovieDatabase {
        @GET("movie/popular")
        Call<MovieResponse> popularMovies(@QueryMap Map<String, String> options);

        @GET("movie/top_rated")
        Call<MovieResponse> topRatedMovies(@QueryMap Map<String, String> options);

        @GET("movie/{id}/reviews")
        Call<ReviewResponse> reviews(@Path("id") long id, @QueryMap Map<String, String> options);

        @GET("movie/{id}/videos")
        Call<VideoResponse> videos(@Path("id") long id, @QueryMap Map<String, String> options);
    }

}
