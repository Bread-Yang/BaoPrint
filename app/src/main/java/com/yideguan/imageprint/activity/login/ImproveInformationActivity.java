package com.yideguan.imageprint.activity.login;

import android.os.Bundle;
import android.view.View;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;

/**
 * A login screen that offers login via email/password.
 */
public class ImproveInformationActivity extends ToolbarActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_improve_information;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void finishAction(View view) {
        finish();
    }

}

