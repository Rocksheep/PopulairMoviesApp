package nl.codesheep.android.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mTitle;
    private String mPosterUrl;
    private String mCoverUrl;
    private String mSynopsis;
    private double mRating;
    private String mReleaseDate;

    public Movie(String title, String posterUrl, String coverUrl, String synopsis, double rating, String releaseDate) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mCoverUrl = coverUrl;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
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
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mCoverUrl);
        dest.writeString(mSynopsis);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
    }
}
