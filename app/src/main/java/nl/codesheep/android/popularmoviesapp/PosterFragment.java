package nl.codesheep.android.popularmoviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.codesheep.android.popularmoviesapp.views.SlidingTabLayout;

public class PosterFragment extends Fragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    public PosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.movie_list_viewpager);
        mViewPager.setAdapter(new MovieViewPageAdapter(getFragmentManager()));
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tab_text_view);
        mSlidingTabLayout.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.accent));
        mSlidingTabLayout.setDividerColors(getActivity().getResources().getColor(R.color.accent));
        mSlidingTabLayout.setViewPager(mViewPager);

        return rootView;
    }
}
