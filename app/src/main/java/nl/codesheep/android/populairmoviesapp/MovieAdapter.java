package nl.codesheep.android.populairmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster_image_view);
        String posterUrl = getItem(position).getPosterUrl();
        Picasso.with(getContext()).load(posterUrl).into(imageView);
        return convertView;
    }

}
