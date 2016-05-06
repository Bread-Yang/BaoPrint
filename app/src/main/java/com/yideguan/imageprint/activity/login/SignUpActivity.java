package com.yideguan.imageprint.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends ToolbarActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void nextStepAction(View view) {
        Intent intent = new Intent(this, ImproveInformationActivity.class);
        startActivity(intent);
    }

    public void protocolAction(View view) {

    }

    public void loginAction(View view) {
        finish();
    }

}

