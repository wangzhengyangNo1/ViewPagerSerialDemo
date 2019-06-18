package com.wzhy.viewpagerserial.nav;

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
