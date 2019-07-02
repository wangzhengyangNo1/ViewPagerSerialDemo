package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;

import com.wzhy.viewpagerserial.VpsApplication;

/**
 * 水平翻转效果
 */
public class FlipHorizontalTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {

        page.setCameraDistance(getCameraDistance());
        page.setTranslationX(-page.getWidth() * position);

        float rotation = 180f * position;
        page.setAlpha(rotation > 90f || rotation < -90f ? 0f : 1f);
        page.setPivotX(page.getWidth() * 0.5f);
        page.setPivotY(page.getHeight() * 0.5f);
        page.setRotationY(rotation);

        if (position > -0.5f && position < 0.5f) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }


    private float getCameraDistance() {
        DisplayMetrics displayMetrics = VpsApplication.getAppContext().getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        return 1.5f * Math.max(widthPixels, heightPixels) * density;
    }
}


