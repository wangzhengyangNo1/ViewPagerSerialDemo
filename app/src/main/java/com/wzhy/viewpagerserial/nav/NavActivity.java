package com.wzhy.viewpagerserial.nav;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzhy.viewpagerserial.base.BaseActivity;
import com.wzhy.viewpagerserial.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ViewPager 导航联动
 * @see {@link com.wzhy.viewpagerserial.MainActivity}
 * @see {@link CommFuncFragment}
 */
public class NavActivity extends BaseActivity {

    private HorizontalScrollView mHsvNavBar;
    private FrameLayout mFlNavFrame;
    private LinearLayout mLlTabGroup;
    private View mIndicatorView;
    private ViewPager mVpContent;

    private ArrayList<FuncEntity> mFuncEntityList = new ArrayList<>();
    private FuncEntity mCurrFuncEntity;

    private FragmentManager mFragmentManager;
    private FragmentPagerAdapter mPagerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        initData();
        initView();
        setListeners();

        //请求角标数量
        for (FuncEntity funcEntity : mFuncEntityList) {
            requestMsgNum(funcEntity);
        }


    }

    private void initData() {
        mFuncEntityList.add(new FuncEntity("关注", 0));
        mFuncEntityList.add(new FuncEntity("推荐", 1));
        mFuncEntityList.add(new FuncEntity("视频", 2));
        mFuncEntityList.add(new FuncEntity("热点", 3));
        mFuncEntityList.add(new FuncEntity("直播", 4));
        mFuncEntityList.add(new FuncEntity("精品课", 5));
        mFuncEntityList.add(new FuncEntity("娱乐", 6));
        mFuncEntityList.add(new FuncEntity("科技", 7));
        mFuncEntityList.add(new FuncEntity("懂车帝", 8));
        mFuncEntityList.add(new FuncEntity("财经", 9));
        mFuncEntityList.add(new FuncEntity("国际", 10));
        mFuncEntityList.add(new FuncEntity("体育", 11));
        mFuncEntityList.add(new FuncEntity("问答", 12));


        mCurrFuncEntity = mFuncEntityList.get(0);
    }

    @Override
    public void initView() {
        mFragmentManager = getSupportFragmentManager();
        //find views
        mHsvNavBar = (HorizontalScrollView) findViewById(R.id.hsv_nav_bar);
        mFlNavFrame = (FrameLayout) findViewById(R.id.fl_nav_frame);
        mLlTabGroup = (LinearLayout) findViewById(R.id.ll_tab_group);
        mIndicatorView = findViewById(R.id.indicator_view);
        mVpContent = (ViewPager) findViewById(R.id.vp_content);


        //初始化导航栏
        for (int i = 0; i < mFuncEntityList.size(); i++) {
            FuncEntity funcEntity = mFuncEntityList.get(i);
            RelativeLayout rlTabCell = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_tab_cell_num, mLlTabGroup, false);
            TextView tvTabName = (TextView) rlTabCell.findViewById(R.id.tv_tab_name);
            TextView tvMsgNum = (TextView) rlTabCell.findViewById(R.id.tv_msg_num);
            TextView tvRedDot = (TextView) rlTabCell.findViewById(R.id.tv_red_dot);
            tvMsgNum.setVisibility(View.GONE);
            tvRedDot.setVisibility(View.GONE);
            tvTabName.setText(funcEntity.getFunName());
            rlTabCell.setTag(funcEntity.getId());
            mLlTabGroup.addView(rlTabCell);
        }

        mPagerAdapter = new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return getFuncFragment(mFuncEntityList.get(position));
            }

            @Override
            public int getCount() {
                return mFuncEntityList.size();
            }
        };
        mVpContent.setAdapter(mPagerAdapter);

    }

    /**
     * Map集合，关联FuncEntity和Fragment，保存二者键值对，使二者具有一一对应关系。
     */
    private Map<FuncEntity, BaseFuncFragment> mFuncFragmentMap = new HashMap<>();

    private BaseFuncFragment getFuncFragment(FuncEntity funcEntity) {
        BaseFuncFragment funcFragment = mFuncFragmentMap.get(funcEntity);
        if (funcFragment == null) {
            funcFragment = CommFuncFragment.newInstance(funcEntity);
            if (funcFragment != null) {
                mFuncFragmentMap.put(funcEntity, funcFragment);
            }
        }

        return funcFragment;

    }

    Random random = new Random();

    private int getMsgNum() {
        int num = random.nextInt(99) + 1;
//        Log.i("NavActivity", "getMsgNum: " + num);
        return num;
    }

    /**
     * 请求角标数量
     */
    private void requestMsgNum(FuncEntity funcEntity) {
        //请求角标数量
        //......

        //更新角标
        updateTabNum(funcEntity.getId(), getMsgNum());
    }

    /**
     * 更新数字
     *
     * @param tabId
     * @param tabNum
     */
    public void updateTabNum(int tabId, int tabNum) {
        View tabView = mLlTabGroup.findViewWithTag(tabId);
        if (tabView != null) {
            TextView tvMsgNum = tabView.findViewById(R.id.tv_msg_num);
            String tabNumStr = tabNum > 99 ? String.valueOf(99).concat("+") : String.valueOf(tabNum);
            tvMsgNum.setText(tabNumStr);
            tvMsgNum.setVisibility(tabNum == 0 ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 清除角标
     *
     * @param tabId
     */
    public void clearTabNum(int tabId) {
        updateTabNum(tabId, 0);
    }

    @Override
    public void setListeners() {

        for (int i = 0; i < mLlTabGroup.getChildCount(); i++) {
            final int pos = i;
            mLlTabGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVpContent.setCurrentItem(pos);
                }
            });
        }

        mVpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int lastPost = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (Math.abs(position - lastPost) > 0) {
                    int llPos = lastPost;
                    int lrPos = lastPost == mFuncEntityList.size() - 1 ? lastPost : lastPost + 1;
                    int clPos = position;
                    int crPos = position == mFuncEntityList.size() - 1 ? position : position + 1;
                    if (llPos != clPos && llPos != crPos) {
                        mLlTabGroup.getChildAt(llPos).findViewById(R.id.tv_tab_name).setAlpha(0.6f);
                    }
                    if (lrPos != clPos && lrPos != crPos) {
                        mLlTabGroup.getChildAt(lrPos).findViewById(R.id.tv_tab_name).setAlpha(0.6f);
                    }
                }

                //设置指示器位移
                RelativeLayout leftCell = (RelativeLayout) mLlTabGroup.getChildAt(position);
                int rightPos = position == mFuncEntityList.size() - 1 ? position : position + 1;
                RelativeLayout rightCell = (RelativeLayout) mLlTabGroup.getChildAt(rightPos);
                float dist = (leftCell.getWidth() + rightCell.getWidth()) / 2f;
                float indicatorCenterX = leftCell.getLeft() + leftCell.getWidth() / 2f + dist * positionOffset;
                int translationX = (int) (indicatorCenterX - mIndicatorView.getWidth() / 2f);
                mIndicatorView.setTranslationX(translationX);

                //水平滚动条自动滚动
                mHsvNavBar.scrollTo((int) (indicatorCenterX - mHsvNavBar.getWidth() / 2f), 0);

                //文字透明度
                if (position != rightPos) {
                    rightCell.findViewById(R.id.tv_tab_name).setAlpha(0.6f + 0.4f * positionOffset);
                    leftCell.findViewById(R.id.tv_tab_name).setAlpha(1f - 0.4f * positionOffset);
                } else {
                    leftCell.findViewById(R.id.tv_tab_name).setAlpha(1f - 0.4f * positionOffset);
                }

                lastPost = position;
            }
        });
    }

    private FuncEntity getCurrFuncEntity() {
        return mFuncEntityList.get(mVpContent.getCurrentItem());
    }

    @Override
    public void onClick(View v) {

    }

}
