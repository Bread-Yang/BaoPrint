package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.graphics.Bitmap;
import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.TemplageImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameTemplateDetailBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PictureFrameTempalteDetailActivity extends ToolbarActivity<ActivityPictureFrameTemplateDetailBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture_frame_template_detail;
    }

    @Override
    protected void initData() {
        mDataBinding.setTemplate(MDGroundApplication.mChoosedTemplate);

        showImageToGPUImageView(0, SelectImageUtil.mAlreadySelectImage.get(0));

    }

    @Override
    protected void setListener() {

    }

    private void showImageToGPUImageView(int position, MDImage mdImage) {
        if (MDGroundApplication.mChoosedProductType == ProductType.Postcard) {
            if (position < SelectImageUtil.mTemplateImage.size()) {
                MDImage templateImage = SelectImageUtil.mTemplateImage.get(position);

                Glide.with(MDGroundApplication.mInstance)
                        .load(templateImage)
                        .dontAnimate()
                        .into(mDataBinding.ivTemplate);
            } else {
                mDataBinding.ivTemplate.setImageBitmap(null);
            }
        }

        Glide.with(this)
                .load(mdImage)
                .asBitmap()
                .thumbnail(0.1f)
                .into(new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        mDataBinding.bgiImage.loadNewImage(bitmap);
                    }
                });
    }

    public void saveImageAction(View view) {

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

    }
    //endregion
}
