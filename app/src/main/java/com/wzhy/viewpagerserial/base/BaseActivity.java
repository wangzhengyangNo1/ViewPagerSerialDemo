package com.wzhy.viewpagerserial.base;

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
