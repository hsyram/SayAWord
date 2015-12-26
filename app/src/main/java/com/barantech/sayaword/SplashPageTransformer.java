package com.barantech.sayaword;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Transformer for view pager in splashActivity
 * <br/>
 * Created by mary on 12/22/15.
 */
public class SplashPageTransformer implements ViewPager.PageTransformer {
    View v1;
    View v2;
    View v3;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
//            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
//            view.setAlpha(1);
//            view.setTranslationX(0);
//            view.setScaleX(1);
//            view.setScaleY(1);
            v1.setTranslationX((position) * (pageWidth / 4));
            v2.setTranslationX((position) * (pageWidth / 2));
            v3.setTranslationX((position) * (pageWidth));

        } else if (position <= 1) { // (0,1]
//            view.setAlpha(1 - position);
//            view.setTranslationX(pageWidth * -position);
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);

            v1.setTranslationX((position) * (pageWidth / 4));
            v2.setTranslationX((position) * (pageWidth / 2));
            v3.setTranslationX((position) * (pageWidth));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
//            view.setAlpha(0);
        }
    }
}
