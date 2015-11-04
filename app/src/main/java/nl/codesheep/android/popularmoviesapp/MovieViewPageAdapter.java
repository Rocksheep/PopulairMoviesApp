package nl.codesheep.android.popularmoviesapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.codesheep.android.popularmoviesapp.data.MovieColumns;

public class MovieViewPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MovieViewPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] pageTitles = mContext.getResources().getStringArray(R.array.main_tab_menu_values);
        if (position < pageTitles.length) {
            return pageTitles[position];
        }
        return null;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        String orderBy;
        boolean showFavorites = false;
        switch (position) {
            default:
            case 0:
                orderBy = MovieColumns.POPULARITY;
                break;
            case 1:
                orderBy = MovieColumns.RATING;
                break;
            case 2:
                orderBy = MovieColumns.POPULARITY;
                showFavorites = true;
                break;

        }
        return MoviePosterFragment.newInstance(orderBy, showFavorites);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
