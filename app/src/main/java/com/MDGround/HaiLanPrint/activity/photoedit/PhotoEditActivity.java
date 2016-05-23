package com.MDGround.HaiLanPrint.activity.photoedit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.PhotoEditImageAdapter;
import com.MDGround.HaiLanPrint.databinding.ActivityPhotoEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.socks.library.KLog;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PhotoEditActivity extends ToolbarActivity<ActivityPhotoEditBinding> {

    private PhotoEditImageAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(SelectImageUtil.mAlreadySelectImage.get(0));

        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.selectedImageRecyclerView.setLayoutManager(imageLayoutManager);
        mAdapter = new PhotoEditImageAdapter();
        mDataBinding.selectedImageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnSelectImageLisenter(new PhotoEditImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(MDImage mdImage) {
                showImageToGPUImageView(mdImage);
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

    private void showImageToGPUImageView(MDImage mdImage) {
        Glide.with(this)
                .load(mdImage)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        mDataBinding.bgiImage.setImage(bitmap);
                    }
                });
    }

    public void saveImageAction(View view) {
        mDataBinding.bgiImage.saveToPictures("海拍", "海拍.jpg", new GPUImageView.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {
                KLog.e("保存图片成功 :" + uri);
            }
        });
    }

}
