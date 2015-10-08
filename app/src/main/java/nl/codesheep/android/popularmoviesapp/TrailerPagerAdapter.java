package nl.codesheep.android.popularmoviesapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TrailerPagerAdapter extends FragmentPagerAdapter {

    public TrailerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TrailerFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
