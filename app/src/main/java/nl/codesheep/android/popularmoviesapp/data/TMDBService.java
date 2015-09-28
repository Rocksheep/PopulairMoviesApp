package nl.codesheep.android.popularmoviesapp.data;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface TMDBService {

    //http://api.themoviedb.org/3/
    @GET("/discover/movie")
    Call<List<Movie>> getMovies(@QueryMap Map<String, String> options);
}
