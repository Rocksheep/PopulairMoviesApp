package nl.codesheep.android.popularmoviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.codesheep.android.popularmoviesapp.views.SlidingTabLayout;

public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();
    private MovieViewPageAdapter mPageAdapter;

    public PosterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageAdapter = new MovieViewPageAdapter(getActivity(), getFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.movie_list_viewpager);
        viewPager.setAdapter(mPageAdapter);

        SlidingTabLayout slidingTabLayout =
                (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(
                ContextCompat.getColor(getActivity(), R.color.accent));
        slidingTabLayout.setViewPager(viewPager);

        return rootView;
    }
}
