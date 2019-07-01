package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 缩放效果
 */
public class ZoomInOutTransformer implements ViewPager.PageTransformer {

    private static final float DEF_MIN_SCALE = 0.9f;
    private float mMinScale = DEF_MIN_SCALE;


    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1f) {//[-Infinity, -1)
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
        } else if (position <= 1f) {//[-1, 1]
            if (position < 0f) {//[-1, 0)
                page.setScaleX(1f + (1f - mMinScale) * position);
                page.setScaleY(1f + (1f - mMinScale) * position);
            } else { //[0, 1]
                page.setScaleX(1f - (1f - mMinScale) * position);
                page.setScaleY(1f - (1f - mMinScale) * position);
            }
        } else {//(1, +Infinity]
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
        }
    }
}
