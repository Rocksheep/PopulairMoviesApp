package nl.codesheep.android.popularmoviesapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

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

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie"
        )
        public static final Uri MOVIES = buildUri(Path.MOVIES);

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
