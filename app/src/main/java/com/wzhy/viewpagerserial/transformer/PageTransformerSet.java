package com.wzhy.viewpagerserial.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.Arrays;
import java.util.List;


/**
 * 页面切换集合
 */
public class PageTransformerSet implements ViewPager.PageTransformer {

    private List<ViewPager.PageTransformer> mPageTransformerList;

    public PageTransformerSet(ViewPager.PageTransformer... pageTransformers) {
        if (mPageTransformerList ==  null) {
            mPageTransformerList = Arrays.asList(pageTransformers);
        }

    }


    @Override
    public void transformPage(@NonNull View page, float position) {
        for (ViewPager.PageTransformer pageTransformer : mPageTransformerList) {
            pageTransformer.transformPage(page, position);
        }
    }
}
