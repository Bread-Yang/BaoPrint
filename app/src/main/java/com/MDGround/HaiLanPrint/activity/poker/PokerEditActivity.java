package com.MDGround.HaiLanPrint.activity.poker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.adapter.TemplageImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPokerEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoGPUImage;
import com.MDGround.HaiLanPrint.views.dialog.NotifyDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PokerEditActivity extends ToolbarActivity<ActivityPokerEditBinding> {

    private TemplageImageAdapter mTeplateImageAdapter;

    private ArrayList<WorkPhoto> mWorkPhotoArrayList = new ArrayList<>();

    private int mCurrentSelectIndex = 0;

    private NotifyDialog mNotifyDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_poker_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(0, SelectImageUtil.mTemplateImage.get(0));

        for (int i = 0; i < SelectImageUtil.mTemplateImage.size(); i++) {
            mWorkPhotoArrayList.add(new WorkPhoto());
        }

        mDataBinding.templateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.templateRecyclerView.setLayoutManager(imageLayoutManager);
        mTeplateImageAdapter = new TemplageImageAdapter();
        mDataBinding.templateRecyclerView.setAdapter(mTeplateImageAdapter);
    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.toMainActivity(PokerEditActivity.this);
            }
        });

        mDataBinding.bgiImage.setOnSingleTouchListener(new BaoGPUImage.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                Intent intent = new Intent(PokerEditActivity.this, SelectAlbumWhenEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mTeplateImageAdapter.setOnSelectImageLisenter(new TemplageImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int position, MDImage mdImage) {

                if (mCurrentSelectIndex != position) {
                    float scaleFactor = mDataBinding.bgiImage.getmScaleFactor();
                    float rotateDegree = mDataBinding.bgiImage.getmRotationDegrees();

                    WorkPhoto workPhoto = mWorkPhotoArrayList.get(mCurrentSelectIndex);
                    workPhoto.setZoomSize(scaleFactor);
                    workPhoto.setRotate(rotateDegree);

                    showImageToGPUImageView(position, mdImage);
                }
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

    private void showImageToGPUImageView(final int position, MDImage mdImage) {
        mCurrentSelectIndex = position;

        // 模板图片加载
        GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate, mdImage);

        // 用户选择的图片加载
        MDImage selectImage = SelectImageUtil.mAlreadySelectImage.get(position);
        if (selectImage.getPhotoSID() != 0 || selectImage.getImageLocalPath() != null) {
            Glide.with(this)
                    .load(selectImage)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            // do something with the bitmap
                            // for demonstration purposes, let's just set it to an ImageView
                            WorkPhoto workPhoto = mWorkPhotoArrayList.get(position);

                            mDataBinding.bgiImage.loadNewImage(bitmap, workPhoto.getZoomSize(), workPhoto.getRotate());
                        }
                    });
        } else {
            mDataBinding.bgiImage.loadNewImage(null);
        }
    }

    private void generateOrder() {
        ViewUtils.loading(this);
        // 生成订单
        MDGroundApplication.mOrderutUtils = new OrderUtils(this, MDGroundApplication.mChoosedTemplate.getPrice(), null);
        MDGroundApplication.mOrderutUtils.saveOrderRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage mdImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);

            SelectImageUtil.mAlreadySelectImage.set(mCurrentSelectIndex, mdImage);

            mWorkPhotoArrayList.set(mCurrentSelectIndex, new WorkPhoto());

            showImageToGPUImageView(mCurrentSelectIndex, SelectImageUtil.mTemplateImage.get(mCurrentSelectIndex));
        }
    }

    //region ACTION
    public void nextStepAction(View view) {
        for (int i = 0; i < SelectImageUtil.mAlreadySelectImage.size(); i++) {
            MDImage selectImage = SelectImageUtil.mAlreadySelectImage.get(i);

            if (selectImage.getPhotoSID() == 0 && selectImage.getImageLocalPath() == null) {
                if (mNotifyDialog == null) {
                    mNotifyDialog = new NotifyDialog(this);
                    mNotifyDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {
                        @Override
                        public void onSureClick() {
                            mNotifyDialog.dismiss();
                            generateOrder();
                        }
                    });
                }
                mNotifyDialog.show();

                mNotifyDialog.setTvContent(getString(R.string.not_add_image, i + 1));
                return;
            }
        }

        generateOrder();
    }
    //endregion

}
