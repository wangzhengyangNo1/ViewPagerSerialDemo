package com.wzhy.viewpagerserial.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CubicInnerOverturnTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MAX_ROTATION = 60f;
    private float mMaxRotation = DEFAULT_MAX_ROTATION;

    private static final float MIN_SCALE = 0.80f;


    public CubicInnerOverturnTransformer() {
        this(DEFAULT_MAX_ROTATION);
    }

    public CubicInnerOverturnTransformer(float maxRotate) {
        mMaxRotation = maxRotate;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setPivotY(page.getHeight() / 2f);

        if (position < -1) { // [-Infinity,-1)
            page.setRotationY(mMaxRotation);
            page.setPivotX(page.getWidth());
        } else if (position <= 1) { // [-1,1]

            page.setRotationY(-position * mMaxRotation);
            if (position < 0) {//[0,-1]
                page.setPivotX(page.getWidth());
                page.setScaleX(MIN_SCALE + 0.8f*(position +0.5f) * (position +0.5f));
                page.setScaleY(MIN_SCALE + 0.8f*(position +0.5f) * (position +0.5f));
            } else {//[1,0]
                page.setPivotX(0);
                page.setScaleX(MIN_SCALE + 0.8f*(position - 0.5f) * (position - 0.5f));
                page.setScaleY(MIN_SCALE + 0.8f*(position -0.5f) * (position -0.5f));
            }




        } else { // (1,+Infinity]
            page.setRotationY(-mMaxRotation);
            page.setPivotX(0);
        }
    }
}

