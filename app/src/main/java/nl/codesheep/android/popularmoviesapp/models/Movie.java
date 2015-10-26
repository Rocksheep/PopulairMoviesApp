package nl.codesheep.android.popularmoviesapp.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

import nl.codesheep.android.popularmoviesapp.data.MovieColumns;

public class Movie implements Parcelable{

    private long mId;

    @SerializedName("id")
    private long mMovieId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterUrl;

    @SerializedName("backdrop_path")
    private String mCoverUrl;

    @SerializedName("overview")
    private String mSynopsis;

    @SerializedName("vote_average")
    private double mRating;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("popularity")
    private double mPopularity;

    private boolean mIsFavorite;

    public Movie(
            long movieId,
            String title,
            String posterUrl,
            String coverUrl,
            String synopsis,
            double rating,
            String releaseDate,
            double popularity) {
        mMovieId = movieId;
        mTitle = title;
        mPosterUrl = posterUrl;
        mCoverUrl = coverUrl;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
        mId = 0;
        mPopularity = popularity;
    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mMovieId = in.readLong();
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mCoverUrl = in.readString();
        mSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
        mPopularity = in.readDouble();
        mIsFavorite = in.readByte() == 1;
    }

    public static Movie fromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndexOrThrow(MovieColumns._ID);
        int movieIdIndex = cursor.getColumnIndexOrThrow(MovieColumns.MOVIE_ID);
        int titleIndex = cursor.getColumnIndexOrThrow(MovieColumns.TITLE);
        int posterUriIndex = cursor.getColumnIndexOrThrow(MovieColumns.POSTER_URI);
        int coverUriIndex = cursor.getColumnIndexOrThrow(MovieColumns.COVER_URI);
        int synopsisIndex = cursor.getColumnIndexOrThrow(MovieColumns.SYNOPSIS);
        int ratingIndex = cursor.getColumnIndexOrThrow(MovieColumns.RATING);
        int releaseDateIndex = cursor.getColumnIndexOrThrow(MovieColumns.RELEASE_DATE);
        int popularityIndex = cursor.getColumnIndexOrThrow(MovieColumns.POPULARITY);
        int favoriteIndex = cursor.getColumnIndex(MovieColumns.IS_FAVORITE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date(cursor.getLong(releaseDateIndex));

        Movie movie = new Movie(
                cursor.getLong(movieIdIndex),
                cursor.getString(titleIndex),
                cursor.getString(posterUriIndex),
                cursor.getString(coverUriIndex),
                cursor.getString(synopsisIndex),
                cursor.getDouble(ratingIndex),
                dateFormat.format(date),
                cursor.getDouble(popularityIndex)
        );

        if (favoriteIndex != -1) {
            movie.setIsFavorite(cursor.getInt(favoriteIndex) == 1);
        }

        movie.setId(cursor.getLong(idIndex));

        return movie;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public double getRating() {
        return mRating;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mCoverUrl);
        dest.writeString(mSynopsis);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mPopularity);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }

    public long getMovieId() {
        return mMovieId;
    }

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }
}
