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
        mChooseTemplate = MDGroundApplication.mInstance.getChoosedTemplate();
        mChooseTemplate.setPageCount(1);
        MDGroundApplication.mInstance.setChoosedTemplate(mChooseTemplate);

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
                        mPrice = MDGroundApplication.mInstance.getChoosedTemplate().getPrice();
                        mWorkFormat = frameSizeArray[0];
                        break;
                    case R.id.rb8Inch:
                        mPrice = MDGroundApplication.mInstance.getChoosedTemplate().getPrice2();
                        mWorkFormat = frameSizeArray[1];
                        break;
                    case R.id.rb10Inch:
                        mPrice = MDGroundApplication.mInstance.getChoosedTemplate().getPrice3();
                        mWorkFormat = frameSizeArray[2];
                        break;
                    case R.id.rb12Inch:
                        mPrice = MDGroundApplication.mInstance.getChoosedTemplate().getPrice4();
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
                        mWorkStyle = getString(R.string.landscape);
                        break;
                    case R.id.rbPortrait:
                        mWorkStyle = getString(R.string.portrait);
                        break;
                }
            }
        });
    }

    private void showImageToGPUImageView() {
        if (SelectImageUtils.mTemplateImage.size() > 0) {
            // 模板图片加载
            GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate, SelectImageUtils.mTemplateImage.get(0), false);
        }

        // 用户选择的图片加载
        GlideUtil.loadImageAsBitmap(SelectImageUtils.mAlreadySelectImage.get(0),
                new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        mDataBinding.bgiImage.loadNewImage(bitmap);
                    }
                });
    }

    private void changeMaterialAvailable() {
        mDataBinding.rgStyle.clearCheck();

        if ((MDGroundApplication.mInstance.getChoosedTemplate().getMaterialType() & MaterialType.Landscape.value()) != 0) {
            mDataBinding.rbLandscape.setEnabled(true);
            if (mDataBinding.rgStyle.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbLandscape.setChecked(true);
            }
        } else {
            mDataBinding.rbLandscape.setEnabled(false);
        }

        if ((MDGroundApplication.mInstance.getChoosedTemplate().getMaterialType() & MaterialType.Portrait.value()) != 0) {
            mDataBinding.rbPortrait.setEnabled(true);
            if (mDataBinding.rgStyle.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbPortrait.setChecked(true);
            }
        } else {
            mDataBinding.rbPortrait.setEnabled(false);
        }
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
    public void minusNumAction(View view) {
        int photoCount = mChooseTemplate.getPageCount();

        if (photoCount == 1) {
            return;
        }

        mChooseTemplate.setPageCount(--photoCount);
        MDGroundApplication.mInstance.setChoosedTemplate(mChooseTemplate);
    }

    public void addNumAction(View view) {
        int photoCount = mChooseTemplate.getPageCount();

        mChooseTemplate.setPageCount(++photoCount);
        MDGroundApplication.mInstance.setChoosedTemplate(mChooseTemplate);
    }

    public void purchaseAction(View view) {
        ViewUtils.loading(this);
        MDGroundApplication.mOrderutUtils = new OrderUtils(this,
                mChooseTemplate.getPageCount(),
                mPrice, mWorkFormat, null, mWorkStyle);
        MDGroundApplication.mOrderutUtils.saveOrderRequest();
    }
    //endregion
}
