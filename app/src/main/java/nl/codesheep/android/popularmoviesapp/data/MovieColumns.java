package nl.codesheep.android.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @Unique(onConflict = ConflictResolutionType.IGNORE) @NotNull
            String MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT) @NotNull String TITLE = "title";
    @DataType(DataType.Type.TEXT) @NotNull String POSTER_URI = "poster_uri";
    @DataType(DataType.Type.TEXT) @NotNull String COVER_URI = "cover_uri";
    @DataType(DataType.Type.TEXT) @NotNull String SYNOPSIS = "synopsis";
    @DataType(DataType.Type.REAL) @NotNull String RATING = "rating";
    @DataType(DataType.Type.INTEGER) @NotNull String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.REAL) @NotNull String POPULARITY = "popularity";

    String IS_FAVORITE = "is_favorite";

    String[] PROJECTION = new String[] {
            MovieColumns._ID, MovieColumns.MOVIE_ID, MovieColumns.COVER_URI,
            MovieColumns.IS_FAVORITE, MovieColumns.POPULARITY, MovieColumns.POSTER_URI,
            MovieColumns.RATING, MovieColumns.RELEASE_DATE, MovieColumns.SYNOPSIS,
            MovieColumns.TITLE
    };

}
