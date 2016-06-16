package com.MDGround.HaiLanPrint.activity.magiccup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityMagicCupEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoCustomGPUImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class MagicCupPhotoEditActivity extends ToolbarActivity<ActivityMagicCupEditBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_magic_cup_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView();
    }

    @Override
    protected void setListener() {
        mDataBinding.bgiCustomImage.setOnSingleTouchListener(new BaoCustomGPUImage.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                Intent intent = new Intent(MagicCupPhotoEditActivity.this, SelectAlbumWhenEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mDataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDataBinding.tvPercent.setText(getString(R.string.percent, progress) + "%");

                mDataBinding.bgiCustomImage.mBrightnessFilter.setBrightness(progress / 100f);
                mDataBinding.bgiCustomImage.requestRender();

//                mDataBinding.bgiImage.setmBrightness(progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showImageToGPUImageView() {
        if (SelectImageUtil.mTemplateImage.size() > 0) {
            // 模板图片加载
            Glide.with(MDGroundApplication.mInstance)
                    .load(SelectImageUtil.mTemplateImage.get(0))
                    .dontAnimate()
                    .into(mDataBinding.ivTemplate);
        }

        // 用户选择的图片加载
        Glide.with(this)
                .load(SelectImageUtil.mAlreadySelectImage.get(0))
                .asBitmap()
//                .thumbnail(0.1f)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {

                        mDataBinding.bgiCustomImage.loadNewImage(bitmap);
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
    public void nextStepAction(View view) {
        ViewUtils.loading(this);

        MDGroundApplication.mOrderutUtils = new OrderUtils(this, MDGroundApplication.mChoosedMeasurement.getPrice(), null);
        MDGroundApplication.mOrderutUtils.saveOrderRequest();
    }
    //endregion

}
