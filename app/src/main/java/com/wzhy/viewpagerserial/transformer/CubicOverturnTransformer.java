package com.wzhy.viewpagerserial.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;

import com.wzhy.viewpagerserial.VpsApplication;

/**
 * 立方体翻转效果
 */
public class CubicOverturnTransformer implements ViewPager.PageTransformer {

    public static final float DEFAULT_MAX_ROTATION = 60f;
    public static final float DEF_MIN_SCALE = 0.86f;

    private float mMaxRotation = DEFAULT_MAX_ROTATION;
    private float mMinScale = DEF_MIN_SCALE;


    public CubicOverturnTransformer() {
        this(DEFAULT_MAX_ROTATION);
    }

    public CubicOverturnTransformer(float maxRotation) {
        this(maxRotation, DEF_MIN_SCALE);
    }

    public CubicOverturnTransformer(float maxRotation, float minScale) {
        mMaxRotation = maxRotation;
        this.mMinScale = minScale;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setPivotY(page.getHeight() / 2f);

        float distance = getCameraDistance();
        page.setCameraDistance(distance);//设置 View 的镜头距离，可以防止旋转大角度时出现图像失真或不显示。
        if (position < -1) { // [-Infinity,-1)
            page.setRotationY(-mMaxRotation);
            page.setPivotX(page.getWidth());
        } else if (position <= 1) { // [-1,1]

            page.setRotationY(position * mMaxRotation);
            if (position < 0) {//[0,-1]
                page.setPivotX(page.getWidth());
                float scale = DEF_MIN_SCALE + 4f * (1f - DEF_MIN_SCALE) * (position + 0.5f) * (position + 0.5f);
                page.setScaleX(scale);
                page.setScaleY(scale);
            } else {//[1,0]
                page.setPivotX(0);
                float scale = DEF_MIN_SCALE + 4f * (1f - DEF_MIN_SCALE) * (position - 0.5f) * (position - 0.5f);
                page.setScaleX(scale);
                page.setScaleY(scale);
            }
        } else { // (1,+Infinity]
            page.setRotationY(mMaxRotation);
            page.setPivotX(0);
        }
    }

    /**
     * 获得镜头距离（图像与屏幕距离）。参考{@link View#setCameraDistance(float)}，小距离表示小视角，
     * 大距离表示大视角。这个距离较小时，在 3D 变换（如围绕X和Y轴的旋转）时，会导致更大的失真。
     * 如果改变 rotationX 或 rotationY 属性，使得此 View 很大 （超过屏幕尺寸的一半），则建议始终使用
     * 大于此时图高度 （X 轴旋转）或 宽度（Y 轴旋转）的镜头距离。
     * @return  镜头距离 distance
     *
     * @see {@link View#setCameraDistance(float)}
     */
    private float getCameraDistance() {
        DisplayMetrics displayMetrics = VpsApplication.getAppContext().getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        return 1.5f*Math.max(widthPixels, heightPixels)*density;
    }
}

