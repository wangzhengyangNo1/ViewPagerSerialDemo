package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 堆叠效果
 */
public class StackTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setTranslationX(position < 0 ? 0f : -page.getWidth() * position);
    }
}
