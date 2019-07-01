package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 手风琴效果（水平方向缩放）
 */
public class AccordionTransformer implements ViewPager.PageTransformer {


    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < 0f) {
            page.setPivotX(page.getWidth());
            page.setScaleX(1f + position * 0.5f);
        } else if (position < 1f) {
            page.setPivotX(0f);
            page.setScaleX(1f - position * 0.5f);
        }
    }
}
