package com.wzhy.viewpagerserial.transformer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wzhy.viewpagerserial.R;
import com.wzhy.viewpagerserial.banner.BannerEntity;
import com.wzhy.viewpagerserial.base.BaseActivity;
import com.wzhy.viewpagerserial.transformer.tryit.BackgroundInTransformer;
import com.wzhy.viewpagerserial.transformer.tryit.ScaleInOutTransformer;

import java.util.ArrayList;

public class TransformerUsageActivity extends BaseActivity {

    private ViewPager mVpImgs;
    private ArrayList<BannerEntity> mBannerList;
    private PagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformer_usage);
        initData();
        initView();
        setListeners();
    }

    private void initData() {
        mBannerList = new ArrayList<BannerEntity>();
        int id = 0;
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/cb8be21b1f7ce2256ffd6c7b8b737a74.png", "为什么说 5G 是物联网的时代？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/075ff654f74cf80660112b03e48c2896.jpg", "新技术“红”不过十年，半监督学习为什么是个例外？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/354fc1a74a651de1e0291b4e9261d77c.jpg", "阿里达摩院SIGIR 2019：AI判案1秒钟，人工2小时"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190522/0e36975c84e6e3fb0e576556a1168330.png", "独家！天才少年 Vitalik：“中国开发者应多关注以太坊！”"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/8aee33b2a4ef11b0fb70bf371484c2ee.jpg", "不是码农，不会敲代码的她，却最懂程序员！| 人物志"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/cb8be21b1f7ce2256ffd6c7b8b737a74.png", "为什么说 5G 是物联网的时代？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190617/075ff654f74cf80660112b03e48c2896.jpg", "新技术“红”不过十年，半监督学习为什么是个例外？"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/354fc1a74a651de1e0291b4e9261d77c.jpg", "阿里达摩院SIGIR 2019：AI判案1秒钟，人工2小时"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190522/0e36975c84e6e3fb0e576556a1168330.png", "独家！天才少年 Vitalik：“中国开发者应多关注以太坊！”"));
        mBannerList.add(new BannerEntity(id++, "https://csdnimg.cn/feed/20190618/8aee33b2a4ef11b0fb70bf371484c2ee.jpg", "不是码农，不会敲代码的她，却最懂程序员！| 人物志"));

    }

    @Override
    public void initView() {
        mVpImgs = (ViewPager) findViewById(R.id.vp_imgs);
        mVpImgs.setOffscreenPageLimit(3);
//        mVpImgs.setPageMargin(30);
//        mVpImgs.setPageTransformer(false, new FadeInOutTransformer());
        mVpImgs.setPageTransformer(false, new CubicInnerOverturnTransformer());
        mPageAdapter = new PagerAdapter() {

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                BannerEntity bannerEntity = mBannerList.get(position);

                View view = getLayoutInflater().inflate(R.layout.layout_transformer_item, container, false);
                ImageView ivPageImg = (ImageView) view.findViewById(R.id.iv_page_img);
                Glide.with(TransformerUsageActivity.this).load(bannerEntity.getImgUrl()).centerCrop().into(ivPageImg);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }


            @Override
            public int getCount() {
                return mBannerList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        };
        mVpImgs.setAdapter(mPageAdapter);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(100, 101, 1, "Accordion");
        menu.add(100, 102, 2, "ArcDown");
        menu.add(100, 103, 3, "ArcUp");
        menu.add(100, 104, 4, "FadeInOut");
        menu.add(100, 105, 5, "ZoomInOut");
        menu.add(100, 106, 6, "ScaleInOut");
        menu.add(100, 107, 7, "Depth");
        menu.add(100, 108, 8, "RotateY");
        menu.add(100, 109, 9, "BackgroundIn");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 101:
                mVpImgs.setPageTransformer(false, new AccordionTransformer());
                break;
            case 102:
                mVpImgs.setPageTransformer(false, new ArcDownTransformer());
                break;
            case 103:
                mVpImgs.setPageTransformer(false, new ArcUpTransformer());
                break;
            case 104:
                mVpImgs.setPageTransformer(false, new FadeInOutTransformer());
                break;
            case 105:
                mVpImgs.setPageTransformer(false, new ZoomInOutTransformer());
                break;
            case 106:
                mVpImgs.setPageTransformer(false, new ScaleInOutTransformer());
                break;
            case 107:
                mVpImgs.setPageTransformer(false, new DipInTransformer());
                break;
            case 108:
                mVpImgs.setPageTransformer(false, new CubicOverturnTransformer());
                break;
            case 109:
                mVpImgs.setPageTransformer(false, new BackgroundInTransformer());
                break;
        }
        mPageAdapter.notifyDataSetChanged();
        return true;
    }
}
