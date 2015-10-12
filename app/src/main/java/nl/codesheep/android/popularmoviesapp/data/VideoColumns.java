package nl.codesheep.android.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface VideoColumns {

    @DataType(DataType.Type.INTEGER) @AutoIncrement @PrimaryKey String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull String MOVIE_KEY = "movie_id";
    @DataType(DataType.Type.TEXT) @NotNull String YOUTUBE_KEY = "youtube_key";

}
