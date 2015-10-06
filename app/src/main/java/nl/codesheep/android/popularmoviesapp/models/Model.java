package nl.codesheep.android.popularmoviesapp.models;

import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;

abstract class Model {

    public static final String LOG_TAG = Model.class.getSimpleName();

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        for (Field f : getClass().getDeclaredFields()) {
            Log.d(LOG_TAG, f.getName());
        }
        return contentValues;
    }

}
