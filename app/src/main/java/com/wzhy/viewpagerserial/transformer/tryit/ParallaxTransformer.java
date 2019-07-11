package com.wzhy.viewpagerserial.transformer.tryit;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ParallaxTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        int width = page.getWidth();
        if (position <= -1f) {
            page.setScrollX(0);
        } else if (position < 1f) {

            if (position < 0f) {
                page.setScrollX((int) (width * 0.75f * position));
            } else {
                page.setScrollX((int) (width * 0.75f * position));
            }

        } else {
            page.setScrollX(0);
        }
    }
}
