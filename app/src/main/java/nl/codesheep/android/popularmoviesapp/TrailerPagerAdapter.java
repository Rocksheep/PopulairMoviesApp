package nl.codesheep.android.popularmoviesapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import nl.codesheep.android.popularmoviesapp.models.Video;

public class TrailerPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Video> videos;

    public TrailerPagerAdapter(FragmentManager fm) {
        super(fm);
        videos = new ArrayList<>();
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
}
