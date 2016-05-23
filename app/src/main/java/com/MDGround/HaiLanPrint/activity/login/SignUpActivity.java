package com.MDGround.HaiLanPrint.activity.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySignUpBinding;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.utils.MD5Util;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.socks.library.KLog;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class SignUpActivity extends ToolbarActivity<ActivitySignUpBinding> {

    @Override
    public int getContentLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initData() {
        SMSSDK.initSDK(this, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRECT);
        SMSSDK.registerEventHandler(mEventHandler);
    }

    @Override
    protected void setListener() {
        mDataBinding.ivShowPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.cetPassword.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        mDataBinding.ivHidePsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.cetPassword.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    private EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 提交验证码成功
                    registerRequest();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功,开始倒计时
                    KLog.e("获取验证码成功,开始倒计时");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtils.toast("验证码出错");
                    }
                });
            }
        }
    };

    private void registerRequest() {
        String phone = mDataBinding.cetAccount.getText().toString();
        String password = mDataBinding.cetPassword.getText().toString();

        User newUser = new User();

        newUser.setPhone(phone);
        newUser.setPassword(MD5Util.MD5(password));

        Intent intent = new Intent(this, ImproveInformationActivity.class);
        intent.putExtra(Constants.KEY_NEW_USER, newUser);
        startActivity(intent);

        finish();
    }

    //region ACTION
    public void getCaptchaAction(View view) {
        String phone = mDataBinding.cetAccount.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            ViewUtils.toast(R.string.input_phone_number);
            return;
        }

        if (phone.length() != 11) {
            ViewUtils.toast(R.string.input_corrent_phone);
            return;
        }

        mDataBinding.tvAcquireCaptcha.setClickable(false);

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDataBinding.tvAcquireCaptcha.setText(getString(R.string.after_second_acquire_again, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mDataBinding.tvAcquireCaptcha.setClickable(true);
                mDataBinding.tvAcquireCaptcha.setText(R.string.acquire_again);
            }
        };
        countDownTimer.start();

        SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
            /**
             * 此方法在发送验证短信前被调用，传入参数为接收者号码返回true表示此号码无须实际接收短信
             */
            @Override
            public boolean onSendMessage(String country, String phone) {
                // 可添加测试白名单
                /*
                 * if (phone.equals("18576627750")) { return true; }
				 */
                return false;
            }
        });
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

        SMSSDK.submitVerificationCode("86", phone, captcha);
    }

    public void protocolAction(View view) {
        Intent intent = new Intent(this, ProtocolActivity.class);
        startActivity(intent);
    }

    public void loginAction(View view) {
        finish();
    }
    //endregion
}

