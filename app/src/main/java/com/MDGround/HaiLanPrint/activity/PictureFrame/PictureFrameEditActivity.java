package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RadioGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameEditBinding;
import com.MDGround.HaiLanPrint.enumobject.MaterialType;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoGPUImage;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PictureFrameEditActivity extends ToolbarActivity<ActivityPictureFrameEditBinding> {

    private Template mChooseTemplate;

    private int mPrice;

    private String mWorkFormat, mWorkStyle;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture_frame_edit;
    }

    @Override
    protected void initData() {
        mChooseTemplate = MDGroundApplication.sInstance.getChoosedTemplate();
        mChooseTemplate.setPageCount(1);
        MDGroundApplication.sInstance.setChoosedTemplate(mChooseTemplate);

        mDataBinding.setTemplate(mChooseTemplate);

        showImageToGPUImageView();

        mDataBinding.tvPrice.setText(getString(R.string.yuan_amount,
                StringUtil.toYuanWithoutUnit(mChooseTemplate.getPrice())));
        changeMaterialAvailable();
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

        mDataBinding.rgSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String[] frameSizeArray = getResources().getStringArray(R.array.frame_size_array);

                switch (checkedId) {
                    case R.id.rb6Inch:
                        mPrice = MDGroundApplication.sInstance.getChoosedTemplate().getPrice();
                        mWorkFormat = frameSizeArray[0];
                        break;
                    case R.id.rb8Inch:
                        mPrice = MDGroundApplication.sInstance.getChoosedTemplate().getPrice2();
                        mWorkFormat = frameSizeArray[1];
                        break;
                    case R.id.rb10Inch:
                        mPrice = MDGroundApplication.sInstance.getChoosedTemplate().getPrice3();
                        mWorkFormat = frameSizeArray[2];
                        break;
                    case R.id.rb12Inch:
                        mPrice = MDGroundApplication.sInstance.getChoosedTemplate().getPrice4();
                        mWorkFormat = frameSizeArray[3];
                        break;
                }

                mDataBinding.tvPrice.setText(getString(R.string.yuan_amount,
                        StringUtil.toYuanWithoutUnit(mPrice)));
            }
        });

        mDataBinding.rgStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLandscape:
                        GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate, SelectImageUtils.sTemplateImage.get(0), false);
                        break;
                    case R.id.rbPortrait:
                        GlideUtil.loadImageRotated(mDataBinding.ivTemplate, SelectImageUtils.sTemplateImage.get(0), 90);
                        break;
                }
            }
        });
    }

    private void showImageToGPUImageView() {
        if (SelectImageUtils.sTemplateImage.size() > 0) {
            // 模板图片加载
            GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate, SelectImageUtils.sTemplateImage.get(0), false);
        }

        final MDImage mdImage = SelectImageUtils.sAlreadySelectImage.get(0);

        // 用户选择的图片加载
        GlideUtil.loadImageAsBitmap(mdImage,
                new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        WorkPhoto workPhoto = mdImage.getWorkPhoto();

                        mDataBinding.bgiImage.loadNewImage(bitmap,
                                workPhoto.getZoomSize() / 100f,
                                workPhoto.getRotate(),
                                workPhoto.getBrightLevel() / 100f);
                    }
                });
    }

    private void changeMaterialAvailable() {
        mDataBinding.rgStyle.clearCheck();

        if ((MDGroundApplication.sInstance.getChoosedTemplate().getMaterialType() & MaterialType.Landscape.value()) != 0) {
            mDataBinding.rbLandscape.setEnabled(true);
            if (mDataBinding.rgStyle.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbLandscape.setChecked(true);

                mWorkStyle = getString(R.string.landscape);
            }
        } else {
            mDataBinding.rbLandscape.setEnabled(false);
        }

        if ((MDGroundApplication.sInstance.getChoosedTemplate().getMaterialType() & MaterialType.Portrait.value()) != 0) {
            mDataBinding.rbPortrait.setEnabled(true);
            if (mDataBinding.rgStyle.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbPortrait.setChecked(true);

                mWorkStyle = getString(R.string.portrait);
            }
        } else {
            mDataBinding.rbPortrait.setEnabled(false);
        }
    }

    private void saveToMyWork() {
        ViewUtils.loading(this);
        // 保存到我的作品中
        MDGroundApplication.sOrderutUtils = new OrderUtils(this, true,
                mChooseTemplate.getPageCount(),
                mPrice, mWorkFormat, null, mWorkStyle);
        MDGroundApplication.sOrderutUtils.uploadImageRequest(this, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage newMdImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);

            MDImage oldMdImage = SelectImageUtils.sAlreadySelectImage.get(0);

            WorkPhoto workPhoto = oldMdImage.getWorkPhoto();
            workPhoto.setZoomSize(100);
            workPhoto.setBrightLevel(0);
            workPhoto.setRotate(0);
            newMdImage.setWorkPhoto(workPhoto);

            SelectImageUtils.sAlreadySelectImage.set(0, newMdImage);

            showImageToGPUImageView();
        }
    }

    //region ACTION
    public void minusNumAction(View view) {
        int photoCount = mChooseTemplate.getPageCount();

        if (photoCount == 1) {
            return;
        }

        mChooseTemplate.setPageCount(--photoCount);
        MDGroundApplication.sInstance.setChoosedTemplate(mChooseTemplate);
    }

    public void addNumAction(View view) {
        int photoCount = mChooseTemplate.getPageCount();

        mChooseTemplate.setPageCount(++photoCount);
        MDGroundApplication.sInstance.setChoosedTemplate(mChooseTemplate);
    }

    public void purchaseAction(View view) {
        ViewUtils.loading(this);
        MDGroundApplication.sOrderutUtils = new OrderUtils(this, false,
                mChooseTemplate.getPageCount(),
                mPrice, mWorkFormat, null, mWorkStyle);
        MDGroundApplication.sOrderutUtils.uploadImageRequest(this, 0);
    }
    //endregion
}
