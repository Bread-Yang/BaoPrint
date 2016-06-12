package com.MDGround.HaiLanPrint.activity.postcard;

import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.TemplageImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPostcardEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PostcardEditActivity extends ToolbarActivity<ActivityPostcardEditBinding> {

    private TemplageImageAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_postcard_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(0, SelectImageUtil.mAlreadySelectImage.get(0));

        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.templateRecyclerView.setLayoutManager(imageLayoutManager);
        mAdapter = new TemplageImageAdapter();
        mDataBinding.templateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnSelectImageLisenter(new TemplageImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int position, MDImage mdImage) {
                showImageToGPUImageView(position, mdImage);
            }
        });

        mDataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDataBinding.tvPercent.setText(getString(R.string.percent, progress) + "%");

                mDataBinding.bgiImage.mBrightnessFilter.setBrightness(progress / 100f);
                mDataBinding.bgiImage.requestRender();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    //region ACTION
    public void saveImageAction(View view) {
        ViewUtils.loading(this);

        OrderUtils orderUtils = new OrderUtils(this, null);
        orderUtils.saveUserWorkReqeust();
    }
    //endregion

}
