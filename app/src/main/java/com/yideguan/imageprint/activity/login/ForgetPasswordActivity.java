package com.yideguan.imageprint.activity.login;

import android.os.Bundle;
import android.view.View;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;

/**
 * A login screen that offers login via email/password.
 */
public class ForgetPasswordActivity extends ToolbarActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void finishAction(View view) {
        finish();
    }

}

