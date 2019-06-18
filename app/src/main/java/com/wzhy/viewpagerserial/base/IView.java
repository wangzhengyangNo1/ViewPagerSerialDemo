package com.wzhy.viewpagerserial.base;

import android.view.View;

public interface IView {

    void initView();

    void setListeners();

    void onClick(View v);
}
