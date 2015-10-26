package nl.codesheep.android.popularmoviesapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.models.Video;

public class TrailerPagerAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = TrailerPagerAdapter.class.getSimpleName();
    private ArrayList<Video> videos;

    public TrailerPagerAdapter(FragmentManager fm) {
        super(fm);
        videos = new ArrayList<>();
        Log.d(LOG_TAG, "I am a new adapter");
    }

    @Override
    public Fragment getItem(int position) {

        return TrailerFragment.newInstance(videos.get(position));
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    public void add(Video video) {
        videos.add(video);
        notifyDataSetChanged();
    }

    public void clear() {
        videos.clear();
    }
}
