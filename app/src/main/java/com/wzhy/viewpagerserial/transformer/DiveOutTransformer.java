package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 下潜效果
 */
public class DiveOutTransformer implements ViewPager.PageTransformer {

    private static final float DEF_MIN_SCALE = 0.72f;

    private float mMinScale = DEF_MIN_SCALE;

    private static final float DEF_MIN_ALPHA = 0.5f;

    public DiveOutTransformer() {
    }

    public DiveOutTransformer(float minScale) {
        this.mMinScale = minScale;
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        this.mMinScale = minScale;
    }

    @Override

    public void transformPage(@NonNull View page, float position) {

        if (position < -1f) {
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
            page.setAlpha(DEF_MIN_ALPHA);
            page.setTranslationX(-position * page.getWidth());
        } else if (position <= 0) {
            page.setTranslationX(-position * page.getWidth());
            page.setScaleX(1f + (1f - mMinScale) * position);
            page.setScaleY(1f + (1f - mMinScale) * position);
            page.setAlpha(1f + (1f - DEF_MIN_ALPHA) * position);
        } else {
            page.setTranslationX(0f);
        }

    }


}
