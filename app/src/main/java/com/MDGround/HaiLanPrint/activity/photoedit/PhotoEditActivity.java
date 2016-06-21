package com.MDGround.HaiLanPrint.activity.photoedit;

import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.TemplageImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPhotoEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PhotoEditActivity extends ToolbarActivity<ActivityPhotoEditBinding> {

    private TemplageImageAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(0, SelectImageUtils.mAlreadySelectImage.get(0));

        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.selectedImageRecyclerView.setLayoutManager(imageLayoutManager);
        mAdapter = new TemplageImageAdapter();
        mDataBinding.selectedImageRecyclerView.setAdapter(mAdapter);
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
            if (position < SelectImageUtils.mTemplateImage.size()) {
                MDImage templateImage = SelectImageUtils.mTemplateImage.get(position);

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
//                .thumbnail(0.1f)
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
        if(SelectImageUtils.mTemplateImage.size() > 0) {
            mDataBinding.ivTest.setImageBitmap(mDataBinding.bgiImage.addTemplate(this, ((GlideBitmapDrawable)mDataBinding.ivTemplate.getDrawable()).getBitmap()));
        }
//        mDataBinding.bgiImage.saveToPictures("海拍", "海拍.jpg", new GPUImageView.OnPictureSavedListener() {
//            @Override
//            public void onPictureSaved(Uri uri) {
//                KLog.e("保存图片成功 :" + uri);
//            }
//        });
    }

}
