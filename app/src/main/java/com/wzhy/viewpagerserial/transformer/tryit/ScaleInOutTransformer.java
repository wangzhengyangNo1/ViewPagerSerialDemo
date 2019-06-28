package com.wzhy.viewpagerserial.transformer.tryit;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ScaleInOutTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MIN_SCALE = 0.85f;
    private float mMinScale = DEFAULT_MIN_SCALE;

    public ScaleInOutTransformer() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        page.setPivotY(pageHeight / 2f);
//        page.setPivotX(pageWidth / 2f);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
            page.setPivotX(pageWidth);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {//1-2:1[0,-1] ;2-1:1[-1,0]

                float scaleFactor = 1f + (1f - mMinScale) * position;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * ((1 - position) * 0.5f));

            } else {//1-2:2[1,0] ;2-1:2[0,1]
                float scaleFactor = 1f - (1f - mMinScale) * position;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * ((1 - position) * 0.5f));
            }

        } else { // (1,+Infinity]
            page.setPivotX(0);
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
        }
    }
}