package com.barantech.sayaword;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private View vIndicator0;
    private View vIndicator1;
    private View vIndicator2;
    private View mPreActive;
    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        vIndicator0 = findViewById(R.id.v_indicator_0_shadow);
        vIndicator1 = findViewById(R.id.v_indicator_1_shadow);
        vIndicator2 = findViewById(R.id.v_indicator_2_shadow);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new SplashPageTransformer());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View view = getIndicator(position);
                if (view != null)
                    if (positionOffset <= 0) { // [-1,0]
                        mPreActive = view;
                        view.setAlpha(1);
                        view.setScaleX(1);
                        view.setScaleY(1);

                    } else if (positionOffset <= 1) { // (0,1]
                        if(positionOffset >= 0.5)
                            mPreActive.setAlpha(0);
                        // Fade the page out.
                        view.setAlpha(1 - positionOffset);

                        // Scale the page down (between MIN_SCALE and 1)
                        float scaleFactor = MIN_SCALE
                                + (1 - MIN_SCALE) * (1 - Math.abs(positionOffset));
                        view.setScaleX(scaleFactor);
                        view.setScaleY(scaleFactor);

                    }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private View getIndicator(int position) {
        switch (position) {
            case 0:
                return vIndicator0;
            case 1:
                return vIndicator1;
            case 2:
                return vIndicator2;
            default:
                return null;
        }

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new WizardOneFragment();
                case 1:
                    return new WizardTwoFragment();
                case 2:
                    return new WizardThreeFragment();
            }
            return new WizardOneFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public static class WizardOneFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(
                    R.layout.wizard_one, container, false);

        }
    }
    public static class WizardTwoFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(
                    R.layout.wizard_two, container, false);

        }
    }
    public static class WizardThreeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(
                    R.layout.wizard_three, container, false);

        }
    }
}
