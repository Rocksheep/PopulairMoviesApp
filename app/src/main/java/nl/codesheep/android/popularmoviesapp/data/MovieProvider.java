package nl.codesheep.android.popularmoviesapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
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

    private abstract class Path {
        public static final String MOVIES = "movies";
    }

    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/list"
        )
        public static final Uri MOVIES = buildUri(Path.MOVIES);

    }
}
