package nl.codesheep.android.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
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
    private static final String THUMB_URL = "thumbnail_url";
    private static final String VIDEO_URL = "video_url";

    public static TrailerFragment newInstance(Video video) {

        Bundle args = new Bundle();
        args.putString(THUMB_URL, video.getThumbnailUrl());
        args.putString(VIDEO_URL, video.getVideoUrl());

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
        if (args == null) {
            return rootView;
        }
        String thumbnailUrl = args.getString(THUMB_URL);
        final String videoUrl = args.getString(VIDEO_URL);
        Picasso.with(getActivity()).load(thumbnailUrl).into(imageView);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(videoUrl)
                );

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

}
