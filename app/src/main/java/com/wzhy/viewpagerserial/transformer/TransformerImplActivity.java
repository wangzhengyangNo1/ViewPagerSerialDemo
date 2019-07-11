package com.wzhy.viewpagerserial.transformer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wzhy.viewpagerserial.R;
import com.wzhy.viewpagerserial.base.BaseActivity;

public class TransformerImplActivity extends BaseActivity {

    private LinearLayout mLlBtnGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfromer_impl);

        initView();
        setListeners();

    }

    @Override
    public void initView() {
        mLlBtnGroup = (LinearLayout) findViewById(R.id.ll_btn_group);
    }

    @Override
    public void setListeners() {
        for (int i = 0; i < mLlBtnGroup.getChildCount(); i++) {
            mLlBtnGroup.getChildAt(i).setOnClickListener(getOnClickListener());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TransformerUsageActivity.class);
        intent.putExtra("transformerId", v.getId());
        intent.putExtra("transformerName", ((Button) v).getText().toString());
        startActivity(intent);
    }
}
