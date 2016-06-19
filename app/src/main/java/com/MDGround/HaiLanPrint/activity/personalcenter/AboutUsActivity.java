package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityAboutUsBinding;

/**
 * Created by PC on 2016-06-15.
 */

public class AboutUsActivity extends ToolbarActivity<ActivityAboutUsBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    //region ACTION
    //拨打电话
    public void toCallPhone(View view) {
        Uri uri = Uri.parse("tel:123456");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    //打开网站
    public void toUsWeb(View view) {
        String realm = mDataBinding.tvRealm.getText().toString();
        Uri uri = Uri.parse("http://" + realm);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //服务条款
    public void toRegisterProtocol(View view) {
        Intent intent = new Intent(this, TermServerActivity.class);
        startActivity(intent);
    }

    //意见反馈
    public void toFeedBack(View view) {
        Intent intent = new Intent(this, FeedBackActivity.class);
        startActivity(intent);
    }
    //endregion
}
