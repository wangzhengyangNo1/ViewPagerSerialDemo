package com.wzhy.viewpagerserial.transformer.tryit;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class BackgroundInTransformer implements ViewPager.PageTransformer {

    private static final float DEF_MAX_SCALE = 0.85f;
    private float mMaxScale = DEF_MAX_SCALE;

    @Override
    public void transformPage(@NonNull View page, float position) {
        final float height = page.getHeight();
        final float width = page.getWidth();


        float scale;
        if (position < -1) {
            scale = mMaxScale;
        } else if (position <= 0) {
            scale = 1f + (1f - mMaxScale) * position;
        } else if (position < 1) {
            page.bringToFront();
            scale = 1f - (1f - mMaxScale) * position;
        } else {
            scale = mMaxScale;
        }

//        final float scale = Math.min(position < 0 ? 1f : Math.abs(1f - position), 0.8f);

        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setPivotX(width * 0.5f);
        page.setPivotY(height * 0.5f);
//        page.setTranslationX(position<0?width * position :-width * position * 0.5f);
    }

}
