package com.wzhy.viewpagerserial.banner;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzhy.viewpagerserial.R;
import com.wzhy.viewpagerserial.base.BaseActivity;

import java.util.ArrayList;

public class BannerActivity extends BaseActivity {

    private ViewPager mVpBanner;
    private ArrayList<BannerEntity> mBannerList;
    private BannerPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initData();
        initView();
        setListeners();
    }

    /**
     * 初始化数据时，如果数据量大于2， `mBannerList.size() >  2` ，无需特殊处理；
     * 如果等于2，因为 ViewPager 有离屏缓存，需要将2条数据再次添加一遍到四条数据（id 分别为：0, 1, 2, 3）；
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
    }


    @Override
    public void initView() {
        mVpBanner = ((ViewPager) findViewById(R.id.vp_banner));
        mPagerAdapter = new BannerPagerAdapter();
        mVpBanner.setAdapter(mPagerAdapter);

        if (mBannerList.size() > 1) {
            mVpBanner.setCurrentItem(1);
        } else if (mBannerList.size() == 1) {
            mVpBanner.setCurrentItem(0);
        } else {
            mVpBanner.setVisibility(View.GONE);
        }
    }

    class BannerPagerAdapter extends PagerAdapter {

        private ArrayList<BannerEntity> mDataList = new ArrayList<>();

        public BannerPagerAdapter() {
            resetData(0);
        }

        public void resetData(int id) {
            if (mBannerList.size() > 1) {
                int leftId = (mBannerList.size() + id - 1) % mBannerList.size();
                int rightId = (id + 1) % mBannerList.size();

                Log.i("Banner", "resetData: leftId = " + leftId + ", id = " + id + ", rightId = " + rightId);
                mDataList.clear();
                mDataList.add(mBannerList.get(leftId));
                mDataList.add(mBannerList.get(id));
                mDataList.add(mBannerList.get(rightId));
            } else if (mBannerList.size() == 1){
                mDataList.add(mBannerList.get(id));
            }

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
            Log.i("destroyItem", "destroyItem: " + ((BannerEntity) object).getId() + ", position = " + position);
        }

        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            super.finishUpdate(container);
            resetData(mDataList.get(mVpBanner.getCurrentItem()).getId());
            notifyDataSetChanged();
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


    @Override
    public void setListeners() {
        mVpBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
