# ViewPagerSerialDemo
@[toc]
# 1. 简介
常常羡慕于别人分享的自定义ViewPager联动效果。近期项目中用到联动效果，找来一个不错的框架用到了项目中。发现有几个问题：一是难以适应自己项目的设计风格；二是适应自己项目的交互比较麻烦；三是不知所以然导致维护和修改困难。于是决定自己实现。

下图是我们的实现目标：
![ViewPager导航联动带角标](https://img-blog.csdnimg.cn/20190618142725502.gif)
# 2. 要点
## 2.1 角标如何布局
因为需要在每一个导航的 tab 右上角加上角标。所以需要写好角标的布局。因为需要让角标始终在文字的右上角，于是我就想到了 RelativeLayout 相对布局，它可以设置角标的相对位置，非常适合现在这个场景（当然 ConstraintLauout 也可以）。布局代码如下示例：
`layout/layout_tab_cell_num.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/rl_tab_cell"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingLeft="2dp"
	android:paddingRight="2dp">

<TextView
    android:id="@+id/tv_tab_name"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true"
    android:text="报送"
    android:textColor="#ffffff"
    android:textSize="16dp"
    android:alpha="0.6"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp"
    />


<TextView
    android:id="@+id/tv_msg_num"
    android:layout_width="wrap_content"
    android:layout_height="16dp"
    android:minWidth="16dp"
    android:background="@drawable/shape_tab_msg_bg_red"
    android:layout_gravity="center"
    android:visibility="gone"
    tools:visibility="visible"
    android:paddingLeft="4dp"
    android:paddingRight="4dp"
    android:paddingBottom="0.5dp"
    android:gravity="center"
    android:lines="1"
    android:maxLines="1"
    android:textStyle="bold"
    android:text="99"
    android:textSize="10dp"
    android:textColor="@color/color_white"
    android:layout_toRightOf="@id/tv_tab_name"
    android:layout_above="@id/tv_tab_name"
    android:layout_marginLeft="-14dp"
    android:layout_marginBottom="-12dp"
    />

    <TextView
        android:id="@+id/tv_red_dot"
        android:layout_width="8dp"
        android:layout_height="8dp"
        android:background="@drawable/shape_tab_msg_bg_red"
        android:layout_gravity="center"
        android:visibility="gone"
        tools:visibility="gone"
        android:layout_toRightOf="@id/tv_tab_name"
        android:layout_above="@id/tv_tab_name"
        android:layout_marginLeft="-12dp"
        android:layout_marginBottom="-8dp"
        />

</RelativeLayout>

```

![ViewPager 角标](https://img-blog.csdnimg.cn/20190617151450487.jpg)
上面布局代码中，通过

```xml
android:layout_toRightOf="@id/tv_tab_name"
android:layout_above="@id/tv_tab_name"
```
控制角标数字或角标红点的位置，通过

```xml
android:layout_marginLeft="-12dp"
android:layout_marginBottom="-8dp"
```
控制角标向 tab 文字内部缩进（相对于文字向左向下缩进）。


`drawable/shape_tab_msg_bg_red.xml` 的布局如下，common：
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 内部颜色 -->
    <solid android:color="@color/color_red" />
    <!-- 边缘线条颜色 -->
    <stroke
        android:width="1dp"
        android:color="@color/common_color" />
    <!-- 圆角的幅度 -->
    <corners android:radius="100dp" />

</shape>
```
**注意：** 边缘线条颜色 最好 **与背景色一致** 。
## 2.2 如何与Indicator实现联动
Tab 切换需要一个指示器，用于明确指示我们处在那个页面，我们需要让 ViewPager 的页面和 Tab样式和指示器一一对应。
我们可以使用动画，但是有麻烦，如果在页面切换时使用动画去实现，我们很难保证指示器的动画能与 ViewPager 的页面切换节奏保持一致。节奏不一致，就会不流畅。
怎么办呢？我注意到 ViewPager 可以添加一个监听：`addOnPageChangeListener(OnPageChangeListener)`。
onPageChangeListener 的源码如下：

```java
    /**
     * Callback interface for responding to changing state of the selected page.
     */
    public interface OnPageChangeListener {

        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position Position index of the first page currently being displayed.
         *                 Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         */
        void onPageScrollStateChanged(int state);
    }
```

`onPageSelected` 和 `onPageScrollStateChanged` 我们应该很熟悉了，
`onPageSelected` 在新页面被选择示会被调用，动画未必完成；
`onPageScrollStateChanged` 在当滚动状态改版时调用，有三种状态。
还有一个方法比较少用到：
`onPageScrolled` 在当前页面被滚动时调用，有三个参数：
- `position`
 翻译：当前正在显示的第一页的位置索引。如果 positionOffset 不为 0，position+1 页会显示。
 事实上：当页面正在滚动时，即滚动未完成，postion 表示 **左边页面的下标**，无论是左滑还是右滑，postion+1 即是右边页面的下标。
- `positionOffset`
 从[0, 1)的值，表示页面的偏移量。
- `positionOffsetPixels`
 偏移量的 pixel 值。

通过 `onPageScrolled` 方法的 position 和 `positionOffset` 我们可以算出 指示器（Indicator View）
的位置。如下所示：
![indicatorCenterX](https://img-blog.csdnimg.cn/20190617170129629.jpg)
图为指示器中心点位置计算：
```java
int dist = (leftCell.getWidth() + rightCell.getWidth()) / 2;
float indicatorCenterX = leftCell.getLeft() + leftCell.getWidth() / 2 + dist * positionOffset;
int translationX = (int) (indicatorCenterX - mIndicatorView.getWidth() / 2);
mIndicatorView.setTranslationX(translationX);
```
positionOffset 是随着页面的切换而不断变化的，因此可以，通过setTranslationX 设置 indicatorView 的位移量 translationX 来控制 indicatorView 的移动。

# 3. 实现
## 3.1 布局
导航栏 tab 的布局上面已经给出，下面是导航、指示器、ViewPager的布局：
`layout/activity_nav.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".nav.NavActivity">

    <HorizontalScrollView
        android:id="@+id/hsv_nav_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/common_color"
        android:scrollbars="none">


        <FrameLayout
            android:id="@+id/fl_nav_frame"
            android:layout_width="wrap_content"
            android:layout_height="44dp">

            <LinearLayout
                android:id="@+id/ll_tab_group"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:orientation="horizontal">

            </LinearLayout>

            <View
                android:id="@+id/indicator_view"
                android:layout_width="18dp"
                android:layout_height="2dp"
                android:background="@color/color_white"
                android:layout_gravity="bottom"
                android:layout_marginBottom="4dp"/>

        </FrameLayout>

    </HorizontalScrollView>

    <android.support.v4.view.ViewPager
        android:id="@+id/vp_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />


</LinearLayout>
```
布局很简单，如果tab数量固定且有限，不需要水平滚动条，可以把水平滚动条去掉，在`@+id/ll_tab_group` 中，根据上面角标布局，写自己的 tab 组合。

## 3.2 导航栏
**第一步：准备数据**

```java
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
}
```
数据类如下
```java
import java.io.Serializable;

public class FuncEntity implements Serializable {


    /**
     * funName : 关注
     * id : 2
     */

    private String funName;
    private int id;


    public FuncEntity() {
    }

    public FuncEntity(String funName, int id) {
        this.funName = funName;
        this.id = id;
    }


    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
```

**第二步：初始化导航栏，为 ViewPager 设置适配器**

```java
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

		//请求角标数量
        for (FuncEntity funcEntity : mFuncEntityList) {
            requestMsgNum(funcEntity);
        }

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
     *  请求角标数量
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
            tvMsgNum.setVisibility(tabNum == 0 ? View.GONE:View.VISIBLE);
        }
    }

    /**
     * 清除角标
     * @param tabId
     */
    public void clearTabNum(int tabId) {
        updateTabNum(tabId, 0);
    }
```

## 3.3 导航栏与ViewPager联动
需要为每一个 tab 设置点击监听：

```java
for (int i = 0; i < mLlTabGroup.getChildCount(); i++) {
    final int pos = i;
    mLlTabGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mVpContent.setCurrentItem(pos);
        }
    });
}
```

## 3.4 tab切换效果

```java

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
```

# 4. 代码
完整代码：
`NavActivity.java`

```java
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

```

`NavActivity.java`

```java
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity implements IView {


    protected View.OnClickListener mOnClickListener;

    protected View.OnClickListener getOnClickListener() {
        if (mOnClickListener == null) {
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.onClick(v);
                }
            };
        }

        return mOnClickListener;
    }
}
```
`IView.java`
```java

import android.view.View;

public interface IView {

    void initView();

    void setListeners();

    void onClick(View v);
}

```

`BaseFuncFragment.java`

```java
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wzhy.viewpagerserial.base.IView;

public abstract class BaseFuncFragment extends Fragment implements IView {

    protected FuncEntity mFuncEntity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.containsKey("funcEntity")) {
            mFuncEntity = (FuncEntity) args.getSerializable("funcEntity");
        }
    }
}
```

`CommFuncFragment.java`

```java
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzhy.viewpagerserial.R;

/**
 *
 */
public class CommFuncFragment extends BaseFuncFragment {


    private View mView;

    public CommFuncFragment() {
    }

    public static CommFuncFragment newInstance(FuncEntity funcEntity) {
        Bundle args = new Bundle();
        args.putSerializable("funcEntity", funcEntity);
        CommFuncFragment fragment = new CommFuncFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_comm_func, container, false);
        initView();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    @Override
    public void initView() {
        ((TextView) mView.findViewById(R.id.tv_page_name)).setText(mFuncEntity.getFunName());
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}
```
`fragment_comm_func.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
tools:context=".nav.CommFuncFragment">

<TextView
    android:id="@+id/tv_page_name"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textSize="48dp"
    tools:text="tabName"
    android:textColor="@color/common_color"
    android:layout_gravity="center"/>

</FrameLayout>
```

# 5. 参考
