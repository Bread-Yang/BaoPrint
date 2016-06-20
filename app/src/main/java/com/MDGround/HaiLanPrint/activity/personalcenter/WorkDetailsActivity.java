package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityWorksDetailsBinding;
import com.MDGround.HaiLanPrint.models.WorkInfo;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.EncryptUtil;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.views.dialog.ShareDialog;
import com.socks.library.KLog;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PC on 2016-06-19.
 */

public class WorkDetailsActivity extends ToolbarActivity<ActivityWorksDetailsBinding> {
    private WorkInfo mWorkInfo;
    private ShareDialog mShareDialog;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_works_details;
    }

    @Override
    protected void initData() {
        Intent intent = this.getIntent();
        mWorkInfo = (WorkInfo) intent.getSerializableExtra(Constants.KEY_WORKS_DETAILS);
        KLog.e("mWorkInfoID" + mWorkInfo.getWorkID());
        GlideUtil.loadImageByPhotoSID(mDataBinding.ivImage, mWorkInfo.getPhotoCover(),true);
        mDataBinding.tvWorksname.setText(String.valueOf(mWorkInfo.getTypeName()));
        mDataBinding.tvWorksPice.setText(getString(R.string.rmb) + String.valueOf(StringUtil.toYuanWithoutUnit(mWorkInfo.getPrice())));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(mWorkInfo.getCreatedTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String recentlyDate = DateUtils.getDateStringBySpecificFormat(date, new SimpleDateFormat("yyyy-MM-dd"));
        mDataBinding.tvRecentlyEdited.setText(getString(R.string.recently_edit) + " " + recentlyDate);
        mDataBinding.tvPage.setText(getString(R.string.page_num_) + " " + mWorkInfo.getPhotoCount() + getString(R.string.letter_P));
        mDataBinding.tvTemplate.setText(getString(R.string.template_name_)+" "+mWorkInfo.getTypeName());
    }

    @Override
    protected void setListener() {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextSize(0);
        tvRight.setBackgroundResource(R.drawable.icon_share_mywork);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 int workId=mWorkInfo.getWorkID();
                 int userId=mWorkInfo.getUserID();
                String shareUrl=shareURL(String.valueOf(workId),String.valueOf(userId));
                if (mShareDialog == null) {
                    mShareDialog = new ShareDialog(WorkDetailsActivity.this);
                }
               mShareDialog.initShareUri(shareUrl);
                mShareDialog.show();
            }
        });
    }

    //region ACTION
    //编辑作品
    public void onEditorWorks(View view) {

    }

    //购买作品
    public void onShoppingWorks(View view) {

    }
    //分享链接
    public String shareURL(String workId,String userId){
        String shareUrl=null;
        String workID= null;
        String userID=null;
        try {
            workID = EncryptUtil.encrypt(workId);
            userID=EncryptUtil.encrypt(userId);
            workID= URLEncoder.encode(workID, "UTF-8");
            userID=URLEncoder.encode(userID,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!"".equals(workID)&&!"".equals(userID)){
            shareUrl="http://psuat.yideguan.com/ShareWorkPhoto.aspx?workId="+workID+"&userId="+userID;
        }
        return  shareUrl;
    }
    //endregion


}
