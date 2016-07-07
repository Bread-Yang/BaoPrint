package com.MDGround.HaiLanPrint.activity.messagecenter;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityMessageDetailBinding;
import com.MDGround.HaiLanPrint.models.Message;

public class MessageDetailActivity extends ToolbarActivity<ActivityMessageDetailBinding> {

    @Override
    public int getContentLayout() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initData() {
        Message message = getIntent().getParcelableExtra(Constants.KEY_MESSAGE);

        mDataBinding.tvTitle.setText(message.getTitle());
        mDataBinding.tvMessage.setText(message.getMessage());
    }

    @Override
    protected void setListener() {

    }
}

