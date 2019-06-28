package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 淡入淡出
 */
public class FadeInOutTransformer implements ViewPager.PageTransformer {

    private static final float DEF_MIN_ALPHA =0.5f;
    private float mMinAlpha = DEF_MIN_ALPHA;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1f) {//[-Infinity, -1)
            page.setAlpha(mMinAlpha);
        } else if (position <= 1f) {//[-1, 1]
            if (position < 0f) {//[-1, 0)
                page.setAlpha(1f + (1f - mMinAlpha) * position);
            } else { //[0, 1]
                page.setAlpha(1f - (1f - mMinAlpha) * position);
            }
        } else {//(1, +Infinity]
            page.setAlpha(mMinAlpha);
        }
    }
}
