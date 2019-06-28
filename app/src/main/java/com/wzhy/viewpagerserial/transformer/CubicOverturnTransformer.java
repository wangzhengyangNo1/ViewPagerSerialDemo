package com.wzhy.viewpagerserial.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CubicOverturnTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MAX_ROTATE = 60f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;


    public CubicOverturnTransformer() {
        this(DEFAULT_MAX_ROTATE);
    }

    public CubicOverturnTransformer(float maxRotate) {
        mMaxRotate = maxRotate;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setPivotY(page.getHeight() / 2f);

        if (position < -1) { // [-Infinity,-1)
            page.setRotationY(-mMaxRotate);
            page.setPivotX(page.getWidth());
        } else if (position <= 1) { // [-1,1]

            page.setRotationY(position * mMaxRotate);
            if (position < 0) {//[0,-1]
                page.setPivotX(page.getWidth());
            } else {//[1,0]
                page.setPivotX(0);
            }

        } else { // (1,+Infinity]
            page.setRotationY(mMaxRotate);
            page.setPivotX(0);
        }
    }
}

