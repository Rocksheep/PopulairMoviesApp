package nl.codesheep.android.popularmoviesapp.data;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public final class MovieService {
    public static final String API_URL = "http://api.themoviedb.org";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String COVER_URL = "http://image.tmdb.org/t/p/w780";

    public interface TheMovieDatabase {
        @GET("/3/discover/movie")
        Call<MovieResponse> movies(@QueryMap Map<String, String> options);
    }
}
