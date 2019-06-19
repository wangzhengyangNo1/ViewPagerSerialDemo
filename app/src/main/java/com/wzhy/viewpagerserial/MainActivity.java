package com.wzhy.viewpagerserial;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wzhy.viewpagerserial.banner.BannerActivity;
import com.wzhy.viewpagerserial.base.BaseActivity;
import com.wzhy.viewpagerserial.nav.NavActivity;

public class MainActivity extends BaseActivity {

    private TextView mTvBaseView;
    private TextView mTvNav;
    private TextView mTvBanner;
    private TextView mTvRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ViewPager 系列 Demo");

        initView();
        setListeners();
    }

    @Override
    public void initView() {
        mTvBaseView = (TextView) findViewById(R.id.tv_base_use);
        mTvNav = (TextView) findViewById(R.id.tv_nav);
        mTvBanner = (TextView) findViewById(R.id.tv_banner);
        mTvRefresh = (TextView) findViewById(R.id.tv_refresh);
    }

    @Override
    public void setListeners() {
        mTvBaseView.setOnClickListener(getOnClickListener());
        mTvNav.setOnClickListener(getOnClickListener());
        mTvBanner.setOnClickListener(getOnClickListener());
        mTvRefresh.setOnClickListener(getOnClickListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_base_use:
                //ViewPager 基本使用
                break;
            case R.id.tv_nav:
                //ViewPager 导航联动 带角标
                jump2Activity(NavActivity.class);
                break;
            case R.id.tv_banner:
                //ViewPager 轮播to
                jump2Activity(BannerActivity.class);
                break;
            case R.id.tv_refresh:
                //ViewPager 刷新
                break;

        }
    }


    public void jump2Activity(Class<? extends BaseActivity> destClass) {
        Intent intent = new Intent(MainActivity.this, destClass);
        startActivity(intent);
    }
}
