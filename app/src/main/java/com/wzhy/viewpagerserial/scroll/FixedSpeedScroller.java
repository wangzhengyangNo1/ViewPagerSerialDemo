package com.wzhy.viewpagerserial.scroll;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 可设置duration的Scroller。
 * 用于设置ViewPager的页面切换速度。
 * 用法：
 * <code>
 *     try {
 *             Field field = ViewPager.class.getDeclaredField("mScroller");
 *             field.setAccessible(true);
 *             FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
 *             field.set(viewPager, scroller);
 *             scroller.setDuration(2000);
 *         } catch (NoSuchFieldException e) {
 *             e.printStackTrace();
 *         } catch (IllegalAccessException e) {
 *             e.printStackTrace();
 *         }
 * </code>
 */
public class FixedSpeedScroller extends Scroller {

    private static final int DURATION_DEF = 1500;
    private int mDuration = DURATION_DEF;
                                                                                                            
    public FixedSpeedScroller(Context context) {
        this(context, DURATION_DEF);
    }

    public FixedSpeedScroller(Context context, int duration) {
        this(context, null, duration);
    }
                                                                                                            
    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, DURATION_DEF);
    }
    public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        this.mDuration = duration;
    }
                                                                                                            
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
                                                                                                            
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        this.startScroll(startX, startY, dx, dy, mDuration);
    }
                                                                                                            
    public void setDuration(int duration) {
        mDuration = duration;
    }

}