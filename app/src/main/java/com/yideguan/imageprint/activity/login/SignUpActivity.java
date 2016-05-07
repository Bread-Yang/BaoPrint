package com.yideguan.imageprint.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;
import com.yideguan.imageprint.constants.IntentConstants;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.utils.MD5Util;
import com.yideguan.imageprint.utils.StringUtil;
import com.yideguan.imageprint.views.ClearEditText;

public class SignUpActivity extends ToolbarActivity {

    private ClearEditText cet_account, cet_captcha, cet_password;

    @Override
    public int getContentLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void findViewById() {
        cet_account = (ClearEditText) findViewById(R.id.cet_account);
        cet_captcha = (ClearEditText) findViewById(R.id.cet_captcha);
        cet_password = (ClearEditText) findViewById(R.id.cet_password);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void nextStepAction(View view) {
        String phone = cet_account.getText().toString();

        if (StringUtil.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(SignUpActivity.this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String captcha = cet_captcha.getText().toString();
        if (StringUtil.isEmpty(captcha)) {
            Toast.makeText(SignUpActivity.this, R.string.input_captcha, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = cet_password.getText().toString();
        if (StringUtil.isEmpty(password) || password.length() < 6 || password.length() > 16) {
            Toast.makeText(SignUpActivity.this, R.string.input_corrent_password, Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User();

        newUser.setPhone(phone);
        newUser.setPassword(MD5Util.MD5(password));

        Intent intent = new Intent(this, ImproveInformationActivity.class);
        intent.putExtra(IntentConstants.NEW_USER, newUser);
        startActivity(intent);

        finish();
    }

    public void protocolAction(View view) {

    }

    public void loginAction(View view) {
        finish();
    }

}

