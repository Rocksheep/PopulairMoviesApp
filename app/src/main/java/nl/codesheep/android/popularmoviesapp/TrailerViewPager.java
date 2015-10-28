package nl.codesheep.android.popularmoviesapp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class TrailerViewPager extends ViewPager {

    private static final String LOG_TAG = TrailerViewPager.class.getSimpleName();

    public TrailerViewPager(Context context) {
        super(context);
    }

    public TrailerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // http://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
