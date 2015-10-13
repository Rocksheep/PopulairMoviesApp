package nl.codesheep.android.popularmoviesapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MovieViewPageAdapter extends FragmentPagerAdapter {

    public MovieViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Popular";
            case 1:
                return "Highly rated";
            case 2:
                return "Favorites";
        }
        return "Item " + (position + 1);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return MoviePosterFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
