package com.wzhy.viewpagerserial.transformer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wzhy.viewpagerserial.R;
import com.wzhy.viewpagerserial.banner.BannerEntity;
import com.wzhy.viewpagerserial.base.BaseActivity;
import com.wzhy.viewpagerserial.transformer.tryit.ParallaxTransformer;
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
//        mVpImgs.setOffscreenPageLimit(3);
//        mVpImgs.setPageMargin(30);
//        mVpImgs.setPageTransformer(false, new FadeInOutTransformer());
        int transformerId = getIntent().getIntExtra("transformerId", 0);
        String transformerName = getIntent().getStringExtra("transformerName");
        getSupportActionBar().setTitle(transformerName);
        setPageTransformer(transformerId);

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


    private ViewPager.PageTransformer mPageTransformer;

    private void setPageTransformer(int transformerId) {

        boolean reverseDrawingOrder = true;
        switch (transformerId) {
            case R.id.btn_accordion:
                mPageTransformer = new AccordionTransformer();
                break;
            case R.id.btn_arc_down:
                mPageTransformer = new ArcDownTransformer();
                break;
            case R.id.btn_arc_up:
                mPageTransformer = new ArcUpTransformer();
                break;
            case R.id.btn_cubicOverturn_outer:
                mPageTransformer = new CubicOverturnTransformer(90f, 0.6f);
                break;
            case R.id.btn_cubicOverturn_inner:
                mPageTransformer = new CubicOverturnTransformer(-90f, 0.6f);
                break;
            case R.id.btn_dip_in:
                mPageTransformer = new DipInTransformer();
                break;
            case R.id.btn_fade_in_out:
                mPageTransformer = new FadeInOutTransformer();
                break;
            case R.id.btn_flip_horizontal:
                mPageTransformer = new FlipHorizontalTransformer();
                break;
            case R.id.btn_flip_vertical:
                mPageTransformer = new FlipVerticalTransformer();
                break;
            case R.id.btn_rise_in:
                mPageTransformer = new RiseInTransformer();
                break;
            case R.id.btn_dive_out:
                mPageTransformer = new DiveOutTransformer();
                reverseDrawingOrder = false;
                break;
            case R.id.btn_stack:
                mPageTransformer = new StackTransformer();
                break;
            case R.id.btn_zoom_in_out:
                mPageTransformer = new ZoomInOutTransformer();
                break;
            case R.id.btn_parallax:
                mPageTransformer = new ParallaxTransformer();
                break;
            default:
                mPageTransformer = new ScaleInOutTransformer();
                break;
        }

        mVpImgs.setPageTransformer(reverseDrawingOrder, mPageTransformer);

    }
}
