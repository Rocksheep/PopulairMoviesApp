package nl.codesheep.android.popularmoviesapp.models;

import android.content.ContentValues;
import android.database.Cursor;

import nl.codesheep.android.popularmoviesapp.data.ReviewColumns;

public class Review {

    public long movieId;
    public String author;
    public String content;
    public String url;

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReviewColumns.MOVIE_KEY, movieId);
        contentValues.put(ReviewColumns.AUTHOR, author);
        contentValues.put(ReviewColumns.CONTENT, content);

        return contentValues;
    }

    public static Review fromCursor(Cursor cursor) {
        int movieIdIndex = cursor.getColumnIndexOrThrow(ReviewColumns.MOVIE_KEY);
        int authorIndex = cursor.getColumnIndexOrThrow(ReviewColumns.AUTHOR);
        int contentIndex = cursor.getColumnIndexOrThrow(ReviewColumns.CONTENT);

        Review review = new Review();
        review.movieId = cursor.getLong(movieIdIndex);
        review.author = cursor.getString(authorIndex);
        review.content = cursor.getString(contentIndex);
        return review;
    }
}
