package nl.codesheep.android.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull String MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT) @NotNull String TITLE = "title";
    @DataType(DataType.Type.TEXT) @NotNull String POSTER_URI = "poster_uri";
    @DataType(DataType.Type.TEXT) @NotNull String COVER_URI = "cover_uri";
    @DataType(DataType.Type.TEXT) @NotNull String SYNOPSIS = "synopsis";
    @DataType(DataType.Type.REAL) @NotNull String RATING = "rating";
    @DataType(DataType.Type.INTEGER) @NotNull String RELEASE_DATE = "release_date";

}
