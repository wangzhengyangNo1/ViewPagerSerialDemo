package com.wzhy.viewpagerserial.nav;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzhy.viewpagerserial.R;

/**
 * ViewPager 导航联动：公共导航页面
 * @see {@link NavActivity}
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
