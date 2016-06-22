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
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoCustomGPUImage;
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.toMainActivity(MagicCupPhotoEditActivity.this);
            }
        });

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
        if (SelectImageUtils.mTemplateImage.size() > 0) {
            // 模板图片加载
            GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate,
                    SelectImageUtils.mTemplateImage.get(0), false);
        }

        // 用户选择的图片加载
        GlideUtil.loadImageAsBitmap(SelectImageUtils.mAlreadySelectImage.get(0),
                new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        mDataBinding.bgiCustomImage.loadNewImage(bitmap);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage mdImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);

            SelectImageUtils.mAlreadySelectImage.set(0, mdImage);

            showImageToGPUImageView();
        }
    }

    //region ACTION
    public void nextStepAction(View view) {
        ViewUtils.loading(this);

        MDGroundApplication.mOrderutUtils = new OrderUtils(this,
                1, MDGroundApplication.mInstance.getChoosedMeasurement().getPrice());
        MDGroundApplication.mOrderutUtils.saveOrderRequest();
    }
    //endregion

}
