package com.wzhy.viewpagerserial.base;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment implements IView {


    protected View.OnClickListener mOnClickListener;

    protected View.OnClickListener getOnClickListener() {
        if (mOnClickListener == null) {
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFragment.this.onClick(v);
                }
            };
        }

        return mOnClickListener;
    }
}
