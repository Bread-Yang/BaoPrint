package com.MDGround.HaiLanPrint.activity.calendar;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityCalendarTemplateDetailBinding;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/11/16.
 */
public class CalendarTemplateDetailActivity extends ToolbarActivity<ActivityCalendarTemplateDetailBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_calendar_template_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
        mDataBinding.setTemplate(MDGroundApplication.mChoosedTemplate);
    }

    @Override
    protected void setListener() {
    }

    //region ACTION

    public void nextStepAction(View view) {
        NavUtils.toSelectAlbumActivity(this);
    }
    //endregion

    //region SERVER
    //endregion

}
