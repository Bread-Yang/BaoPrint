package com.yideguan.imageprint.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;
import com.yideguan.imageprint.constants.Constants;
import com.yideguan.imageprint.databinding.ActivitySignUpBinding;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.utils.MD5Util;
import com.yideguan.imageprint.utils.StringUtil;

public class SignUpActivity extends ToolbarActivity<ActivitySignUpBinding> {

    @Override
    public int getContentLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {

    }

    public void nextStepAction(View view) {
        String phone = mDataBinding.cetAccount.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;

        }

        if (phone.length() != 11) {
            Toast.makeText(SignUpActivity.this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String captcha = mDataBinding.cetCaptcha.getText().toString();
        if (StringUtil.isEmpty(captcha)) {
            Toast.makeText(SignUpActivity.this, R.string.input_captcha, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mDataBinding.cetPassword.getText().toString();

        if (StringUtil.isEmpty(password)) {
            Toast.makeText(this, R.string.input_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            Toast.makeText(SignUpActivity.this, R.string.input_corrent_password, Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User();

        newUser.setPhone(phone);
        newUser.setPassword(MD5Util.MD5(password));

        Intent intent = new Intent(this, ImproveInformationActivity.class);
        intent.putExtra(Constants.KEY_NEW_USER, newUser);
        startActivity(intent);

        finish();
    }

    public void protocolAction(View view) {
        Intent intent = new Intent(this, ProtocolActivity.class);
        startActivity(intent);
    }

    public void loginAction(View view) {
        finish();
    }

}

