package com.wzhy.viewpagerserial.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wzhy.viewpagerserial.R;
import com.wzhy.viewpagerserial.base.BaseActivity;

import java.util.ArrayList;

public class BannerActivity extends BaseActivity {

    private ViewPager mVpBanner;
    private ArrayList<BannerEntity> mBannerList;
    private BannerPagerAdapter mPagerAdapter;
    private int mOriSize;
    private LinearLayout mLlBannerDotGroup;
    private FrameLayout mFlBannerFrame;
    private MHandler mHandler;
    private static final int WHAT_AUTO_SCROLL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initData();
        initView();
        setListeners();

    }


    private class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_SCROLL) {
                int currentItem = mVpBanner.getCurrentItem();
                mVpBanner.setCurrentItem(currentItem + 1);
                startAutoScroll();
            }
        }
    }


    /**
     * 初始化数据时，如果数据量大于2， `mBannerList.size() >  2` ，无需特殊处理；
     * 如果等于2，因为 ViewPager 有离屏缓存，需要将两条数据复制一份添加到 mBannerList 中；
     * 如果等于1，不需要滚动，不作无限轮播处理。
     */
    private void initData() {
        mBannerList = new ArrayList<BannerEntity>();
        int id = 0;
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/cb8be21b1f7ce2256ffd6c7b8b737a74.png", "为什么说 5G 是物联网的时代？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/075ff654f74cf80660112b03e48c2896.jpg", "新技术“红”不过十年，半监督学习为什么是个例外？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/354fc1a74a651de1e0291b4e9261d77c.jpg", "阿里达摩院SIGIR 2019：AI判案1秒钟，人工2小时"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190522/0e36975c84e6e3fb0e576556a1168330.png", "独家！天才少年 Vitalik：“中国开发者应多关注以太坊！”"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/8aee33b2a4ef11b0fb70bf371484c2ee.jpg", "不是码农，不会敲代码的她，却最懂程序员！| 人物志"));

        //记录原始数据大小，表示指示圆点数量。在下面特殊情况处理之前。
        mOriSize = mBannerList.size();

        //处理等于 2 的情况。
        if (mBannerList.size() == 2) {
            BannerEntity clone0 = mBannerList.get(0).clone();
            BannerEntity clone1 = mBannerList.get(1).clone();
            mBannerList.add(clone0);
            mBannerList.add(clone1);
        }
    }


    @Override
    public void initView() {

        mHandler = new MHandler();

        mFlBannerFrame = (FrameLayout) findViewById(R.id.fl_banner_frame);
        mLlBannerDotGroup = (LinearLayout) findViewById(R.id.ll_banner_dot_group);
        mVpBanner = ((ViewPager) findViewById(R.id.vp_banner));
        mPagerAdapter = new BannerPagerAdapter();
        mVpBanner.setAdapter(mPagerAdapter);

        initBanner();
    }

    /**
     * 初始化轮播图。
     */
    private void initBanner() {
        mFlBannerFrame.setVisibility(View.VISIBLE);
        mLlBannerDotGroup.setVisibility(View.VISIBLE);
        if (mBannerList.size() > 1) {
            //根据原始数据大小创建指示圆点
            for (int i = 0; i < mOriSize; i++) {
                CheckedTextView rbDot = (CheckedTextView) getLayoutInflater().inflate(R.layout.view_banner_dot_round, mLlBannerDotGroup, false);
                mLlBannerDotGroup.addView(rbDot);
                if (i == 0) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rbDot.getLayoutParams();
                    lp.leftMargin = 0;
                }
            }
            //设置当前页为 1 下标页
            mVpBanner.setCurrentItem(1);
            //第一个圆点高亮
            ((CheckedTextView) mLlBannerDotGroup.getChildAt(0)).setChecked(true);

            //开始轮播
            startAutoScroll();
        } else if (mBannerList.size() == 1) {
            mVpBanner.setCurrentItem(0);
            mLlBannerDotGroup.setVisibility(View.GONE);
        } else {
            mFlBannerFrame.setVisibility(View.GONE);
        }
    }

    class BannerPagerAdapter extends PagerAdapter {

        private ArrayList<BannerEntity> mDataList = new ArrayList<>();

        public BannerPagerAdapter() {
            resetData(0);
        }

        public void resetData(int position) {
            if (mBannerList.size() > 1) {
                int leftPos = (mBannerList.size() + position - 1) % mBannerList.size();
                int rightPos = (position + 1) % mBannerList.size();
                mDataList.clear();
                mDataList.add(mBannerList.get(leftPos));
                mDataList.add(mBannerList.get(position));
                mDataList.add(mBannerList.get(rightPos));
            } else if (mBannerList.size() == 1) {
                mDataList.add(mBannerList.get(position));
            }

        }

        public BannerEntity getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.getTag() == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BannerEntity bannerEntity = mDataList.get(position);
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_banner_item, container, false);
            ImageView ivBannerImg = view.findViewById(R.id.iv_banner_img);
            TextView tvBannerDesc = view.findViewById(R.id.tv_banner_desc);
            Glide.with(BannerActivity.this).load(bannerEntity.getImgUrl()).centerCrop().into(ivBannerImg);
            tvBannerDesc.setText(bannerEntity.getDesc());
            view.setTag(bannerEntity);
            container.addView(view);
            return bannerEntity;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(container.findViewWithTag(object));
        }

        public void updateData() {
            if (mDataList.size() > 1) {
                //传入 mBannerList 中的位置
                resetData(mBannerList.indexOf(mDataList.get(mVpBanner.getCurrentItem())));
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (mDataList.contains(object)) {
                return mDataList.indexOf(object);
            } else {
                return POSITION_NONE;
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setListeners() {
        mVpBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int mCheckedId = 0;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                ((CheckedTextView) mLlBannerDotGroup.getChildAt(mCheckedId)).setChecked(false);
                //设置圆点高亮
                int id = mPagerAdapter.getItem(position).getId();
                ((CheckedTextView) mLlBannerDotGroup.getChildAt(id)).setChecked(true);

                mCheckedId = id;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffset == 0) {
                    mPagerAdapter.updateData();
                }
            }
        });

        mVpBanner.setOnTouchListener(new View.OnTouchListener() {

            float currX = 0;
            float dx = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currX = event.getX();
                        stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        dx += Math.abs(x - currX);
                        currX = x;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (dx < 20) {
                            int currentItem = mVpBanner.getCurrentItem();
                            BannerEntity item = mPagerAdapter.getItem(currentItem);
                            Toast.makeText(BannerActivity.this, item.getDesc(), Toast.LENGTH_SHORT).show();
                        }
                        startAutoScroll();
                        dx = 0;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        dx = 0;
                        startAutoScroll();
                        break;
                }
                return false;
            }
        });


    }

    private void startAutoScroll() {
        mHandler.sendEmptyMessageDelayed(WHAT_AUTO_SCROLL, 2000);
    }

    private void stopAutoScroll() {
        mHandler.removeMessages(WHAT_AUTO_SCROLL);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoScroll();
    }
}
