package com.yideguan.imageprint.activity.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.yideguan.imageprint.R;

public abstract class ToolbarActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected TextView tv_right;
    protected T mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // 左边返回键
        mToolbar.setNavigationIcon(android.R.drawable.arrow_up_float);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 标题
        String title = (String) getSupportActionBar().getTitle();
        ((TextView) mToolbar.findViewById(R.id.tv_title)).setText(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 右边按钮
        tv_right = (TextView) mToolbar.findViewById(R.id.tv_right);

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarRightButtonClick(v);
            }
        });

        // data binding
        ViewStub contentStub = (ViewStub) findViewById(R.id.content_stub);
        if (getContentLayout() != 0) {
            contentStub.setOnInflateListener(new ViewStub.OnInflateListener() {
                @Override
                public void onInflate(ViewStub stub, View inflated) {
                    mDataBinding = DataBindingUtil.bind(inflated);
                }
            });

            contentStub.setLayoutResource(getContentLayout());
            contentStub.inflate();
        }

        initData();
        setListener();
    }

    protected abstract int getContentLayout();

    protected abstract void initData();

    protected abstract void setListener();

    protected void setToolbarRightButtonText(int resID) {
        tv_right.setText(resID);
    }

    protected void onToolbarRightButtonClick(View v) {

    }
}
