package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ArcDownTransformer implements ViewPager.PageTransformer {

    private static final float DEF_MAX_ROTATE = 12.0f;
    private float mMaxRotate = DEF_MAX_ROTATE;



    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setPivotY( page.getHeight());
        if (position < -1f) {//[-Infinity, -1)
            page.setRotation(-mMaxRotate);
            page.setPivotX(page.getWidth());
        } else if (position <= 1f) {//[-1, 1]
            if (position < 0f) {//[-1, 0)
                page.setRotation(mMaxRotate * position);
                page.setPivotX(page.getWidth() * (0.5f - 0.5f * position));
            } else { //[0, 1]
                page.setRotation(mMaxRotate * position);
                page.setPivotX(page.getWidth() * (0.5f - 0.5f * position));
            }
        } else {//(1, +Infinity]
            page.setRotation(mMaxRotate);
            page.setPivotX(0f);
        }
    }
}
