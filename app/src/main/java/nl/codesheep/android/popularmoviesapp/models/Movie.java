package nl.codesheep.android.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable{

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

    public Movie(long movieId, String title, String posterUrl, String coverUrl, String synopsis, double rating, String releaseDate) {
        mMovieId = movieId;
        mTitle = title;
        mPosterUrl = posterUrl;
        mCoverUrl = coverUrl;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        mMovieId = in.readLong();
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mCoverUrl = in.readString();
        mSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mCoverUrl);
        dest.writeString(mSynopsis);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
    }

    public long getMovieId() {
        return mMovieId;
    }
}
