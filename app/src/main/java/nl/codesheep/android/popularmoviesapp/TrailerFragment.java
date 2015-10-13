package nl.codesheep.android.popularmoviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import nl.codesheep.android.popularmoviesapp.models.Video;

public class TrailerFragment extends Fragment {

    Video mVideo;

    public static TrailerFragment newInstance(Video video) {

        Bundle args = new Bundle();
        args.putString("url", video.getThumbnailUrl());

        TrailerFragment fragment = new TrailerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TrailerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_trailer, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.trailer_image_view);
        Bundle args = getArguments();
        if (args != null) {
            String thumbnailUrl = args.getString("url");
            Picasso.with(getActivity()).load(thumbnailUrl).into(imageView);
        }
        return rootView;
    }

}
