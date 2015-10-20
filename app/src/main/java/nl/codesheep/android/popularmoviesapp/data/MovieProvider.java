package nl.codesheep.android.popularmoviesapp.data;

import android.net.Uri;
import android.util.Log;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    private MovieProvider() {

    }

    public static final String AUTHORITY =
            "nl.codesheep.android.popularmoviesapp.data";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        for(String path : paths) {
            uriBuilder.appendPath(path);
        }
        return uriBuilder.build();
    }

    interface Path {
        String MOVIES = "movies";
        String REVIEWS = "reviews";
        String VIDEOS = "videos";
        String FAVORITES = "favorites";
    }

    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies {

        @MapColumns public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();
            map.put(MovieColumns.IS_FAVORITE, IS_FAVORITE);
            Log.d(MovieProvider.class.getSimpleName(), IS_FAVORITE);
            return map;
        }

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie"

        )
        public static final Uri MOVIES = buildUri(Path.MOVIES);

        static final String IS_FAVORITE = "(SELECT COUNT(*) FROM "
                + MovieDatabase.FAVORITES
                + " WHERE "
                + MovieDatabase.FAVORITES
                + "."
                + FavoriteColumns.MOVIE_KEY
                + "="
                + MovieDatabase.MOVIES
                + "."
                + MovieColumns.MOVIE_ID
                + ")";

    }

    @TableEndpoint(table = MovieDatabase.REVIEWS) public static class Reviews {
        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/review"
        )
        public static final Uri REVIEWS = buildUri(Path.REVIEWS);

        @InexactContentUri(
                path = Path.MOVIES + "/#/" + Path.REVIEWS,
                name = "REVIEWS_OF_MOVIE",
                type = "vnd.android.cursor.dir/review",
                whereColumn = ReviewColumns.MOVIE_KEY,
                pathSegment = 1
        )
        public static Uri fromMovie(long movieId) {
            return buildUri(Path.MOVIES, String.valueOf(movieId), Path.REVIEWS);
        }
    }

    @TableEndpoint(table = MovieDatabase.VIDEOS) public static class Videos {
        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/video"
        )
        public static final Uri VIDEOS = buildUri(Path.VIDEOS);

        @InexactContentUri(
                path = Path.MOVIES + "/#/" + Path.VIDEOS,
                name = "VIDEOS_OF_MOVIE",
                type = "vnd.android.cursor.dir/video",
                whereColumn = VideoColumns.MOVIE_KEY,
                pathSegment = 1
        )
        public static Uri fromMovie(long movieId) {
            return buildUri(Path.MOVIES, String.valueOf(movieId), Path.VIDEOS);
        }
    }

    @TableEndpoint(table = MovieDatabase.FAVORITES) public static class Favorites {

        @ContentUri(
                path = Path.FAVORITES,
                type = "vnd.android.cursor.dir/favorite",
                join = "LEFT JOIN " + MovieDatabase.MOVIES + " on " +
                        MovieDatabase.FAVORITES + "." + FavoriteColumns.MOVIE_KEY + " = " +
                        MovieDatabase.MOVIES + "." + MovieColumns.MOVIE_ID
        )
        public static Uri FAVORITES = buildUri(Path.FAVORITES);

    }
}
