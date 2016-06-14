package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoGPUImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PictureFrameEditActivity extends ToolbarActivity<ActivityPictureFrameEditBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture_frame_edit;
    }

    @Override
    protected void initData() {
        MDGroundApplication.mChoosedTemplate.setPageCount(1);

        mDataBinding.setTemplate(MDGroundApplication.mChoosedTemplate);

        showImageToGPUImageView();

    }

    @Override
    protected void setListener() {
        mDataBinding.bgiImage.setOnSingleTouchListener(new BaoGPUImage.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                Intent intent = new Intent(PictureFrameEditActivity.this, SelectAlbumWhenEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void showImageToGPUImageView() {
        if (SelectImageUtil.mTemplateImage.size() > 0) {
            // 模版图片加载
            Glide.with(MDGroundApplication.mInstance)
                    .load(SelectImageUtil.mTemplateImage.get(0))
                    .dontAnimate()
                    .into(mDataBinding.ivTemplate);
        }

        // 用户选择的图片加载
        Glide.with(this)
                .load(SelectImageUtil.mAlreadySelectImage.get(0))
                .asBitmap()
                .thumbnail(0.1f)
                .into(new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                        mDataBinding.bgiImage.loadNewImage(bitmap);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage mdImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);

            SelectImageUtil.mAlreadySelectImage.set(0, mdImage);

            showImageToGPUImageView();
        }
    }

    //region ACTION
    public void minusNumAction(View view) {
        int photoCount = MDGroundApplication.mChoosedTemplate.getPageCount();

        if (photoCount == 1) {
            return;
        }

        MDGroundApplication.mChoosedTemplate.setPageCount(--photoCount);
    }

    public void addNumAction(View view) {
        int photoCount = MDGroundApplication.mChoosedTemplate.getPageCount();

        MDGroundApplication.mChoosedTemplate.setPageCount(++photoCount);
    }

    public void purchaseAction(View view) {
        ViewUtils.loading(this);
        MDGroundApplication.mOrderutUtils = new OrderUtils(this, MDGroundApplication.mChoosedTemplate.getPrice(), null);
        MDGroundApplication.mOrderutUtils.saveOrderRequest();
    }
    //endregion
}
