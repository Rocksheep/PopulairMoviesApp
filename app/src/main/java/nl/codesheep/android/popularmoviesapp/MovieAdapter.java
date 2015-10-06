package nl.codesheep.android.popularmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.models.Movie;
import nl.codesheep.android.popularmoviesapp.rest.MovieService;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mMovies;
    private Context mContext;
    private Cursor mCursor;

    private static final int POSTER_VIEW = 0;
    private static final int POSTER_DETAIL_VIEW = 1;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3 == 2 ? POSTER_DETAIL_VIEW : POSTER_VIEW;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == POSTER_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_poster, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_poster_big, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        String posterUrl = getItem(position).getPosterUrl();
        String title = getItem(position).getTitle();
        holder.titleTextView.setText(title);
        double rating = getItem(position).getRating() / 2;
        holder.ratingTextView.setText(mContext.getString(R.string.format_rating, rating));
        Picasso.with(mContext)
                .load(MovieService.POSTER_URL + posterUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        if (getItemViewType(position) == POSTER_DETAIL_VIEW) {
            holder.descriptionTextView.setText(mMovies.get(position).getSynopsis());
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public int position;

        public ImageView imageView;
        public TextView titleTextView;
        public TextView ratingTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
            titleTextView = (TextView) itemView.findViewById(R.id.movie_poster_title_text_view);
            ratingTextView = (TextView) itemView.findViewById(R.id.movie_poster_rating_text_view);
            descriptionTextView = (TextView) itemView.
                    findViewById(R.id.movie_poster_overview_text_view);
        }

        @Override
        public void onClick(View v) {
            try {
                Callback listener = (Callback) mContext;
                listener.onItemSelected(getItem(position));
            } catch (ClassCastException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

        }
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

}
