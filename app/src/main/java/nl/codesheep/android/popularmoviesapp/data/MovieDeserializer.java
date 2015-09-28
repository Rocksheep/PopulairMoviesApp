package nl.codesheep.android.popularmoviesapp.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by rien on 28-9-15.
 */
public class MovieDeserializer implements JsonDeserializer<List<Movie>> {

    private final String KEY_RESULTS = "results";
    private final String KEY_TITLE = "title";
    private final String KEY_SYNOPSIS = "overview";
    private final String KEY_POSTER = "poster_path";
    private final String KEY_RELEASE_DATE = "release_date";
    private final String KEY_RATING = "vote_average";
    private final String KEY_COVER = "backdrop_path";


    @Override
    public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonElement element = json.getAsJsonObject();
        return null;
    }




//    final String KEY_RESULTS = "results";
//    final String KEY_TITLE = "title";
//    final String KEY_SYNOPSIS = "overview";
//    final String KEY_POSTER = "poster_path";
//    final String KEY_RELEASE_DATE = "release_date";
//    final String KEY_RATING = "vote_average";
//    final String KEY_COVER = "backdrop_path";
//    final String POSTER_PATH = "http://image.tmdb.org/t/p/w185";
//    final String COVER_PATH = "http://image.tmdb.org/t/p/w780";
//
//    JSONObject popularMoviesJson = new JSONObject(popularMoviesJsonStr);
//    JSONArray moviesJsonArray = popularMoviesJson.getJSONArray(KEY_RESULTS);
//
//    Movie[] movies = new Movie[moviesJsonArray.length()];
//    for (int i = 0; i < movies.length; i++) {
//        JSONObject movieJson = moviesJsonArray.getJSONObject(i);
//        String title = movieJson.getString(KEY_TITLE);
//        String posterUrl = POSTER_PATH + movieJson.getString(KEY_POSTER);
//        String coverUrl = COVER_PATH + movieJson.getString(KEY_COVER);
//        String synopsis = movieJson.getString(KEY_SYNOPSIS);
//        double rating = movieJson.getDouble(KEY_RATING);
//        String releaseDate = movieJson.getString(KEY_RELEASE_DATE);
//        movies[i] = new Movie(
//                title,
//                posterUrl,
//                coverUrl,
//                synopsis,
//                rating,
//                releaseDate
//        );
//    }
}
