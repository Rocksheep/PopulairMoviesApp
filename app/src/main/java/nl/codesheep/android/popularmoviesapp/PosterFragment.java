package nl.codesheep.android.popularmoviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.codesheep.android.popularmoviesapp.views.SlidingTabLayout;

public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private MovieViewPageAdapter mPageAdapter;

    public PosterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageAdapter = new MovieViewPageAdapter(getFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.movie_list_viewpager);
        mViewPager.setAdapter(mPageAdapter);

        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tab_text_view);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.accent));
        mSlidingTabLayout.setViewPager(mViewPager);
        


        return rootView;
    }
}
