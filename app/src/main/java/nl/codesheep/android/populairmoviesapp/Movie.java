package nl.codesheep.android.populairmoviesapp;

/**
 * Created by Rien on 12-9-2015.
 */
public class Movie {

    private String mTitle;
    private String mPosterUrl;
    private String mSynopsis;
    private double mRating;
    private String mReleaseDate;

    public Movie(String title, String posterUrl, String synopsis, double rating, String releaseDate) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
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

    public String getSynopsis() {
        return mSynopsis;
    }

    public double getRating() {
        return mRating;
    }
}
