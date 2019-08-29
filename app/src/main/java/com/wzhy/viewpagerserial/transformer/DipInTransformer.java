package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * 下沉效果
 */
public class DipInTransformer implements ViewPager.PageTransformer {


    private static final float MIN_SCALE = 0.85f;
    private float mMinScale = MIN_SCALE;
    private static final float MIN_ALPHA = 0.5f;
    private float mMinAlpha = MIN_ALPHA;

    @Override
    public void transformPage(@NonNull View page, float position) {
        Log.i("DipInTransformer", "transformPage: id = " + page.getId() + ", position = " + position);
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
//        page.setPivotX(pageWidth * 0.5f);
        page.setPivotY(pageHeight * 0.5f);
        if (position < -1f) {//(-Infinity, -1]
            page.setAlpha(mMinAlpha);
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
            page.setPivotX(pageWidth*(0.5f + 0.5f * mMinScale));
        } else if (position <= 1f) {//(-1, 1)
            float scaleFactor = Math.max(mMinScale, 1 - Math.abs(position));

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            if (position < 0) {
                page.setPivotX(pageWidth * (0.5f + 0.5f * scaleFactor));

            } else {
                page.setPivotX(pageWidth * (0.5f - 0.5f * scaleFactor));
            }

//
//            float horzMargin = pageWidth * (1f - scaleFactor) / 2f;
//            float vertMargin = pageHeight * (1f - scaleFactor) / 2f;
//            if (position < 0) {
//                page.setTranslationX(horzMargin - vertMargin / 2f);
//            } else {
//                page.setTranslationX(-horzMargin + vertMargin / 2f);
//
//            }

            page.setAlpha(mMinAlpha + (scaleFactor - mMinScale) / (1f - mMinScale) * (1f - mMinAlpha));
        } else {//(1, +Infinity)
            page.setAlpha(mMinAlpha);
            page.setScaleX(mMinScale);
            page.setScaleY(mMinScale);
            page.setPivotX(pageWidth * (0.5f - 0.5f * mMinScale));
        }
    }
}
