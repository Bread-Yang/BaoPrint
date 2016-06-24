package com.MDGround.HaiLanPrint.activity.artalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.adapter.TemplateImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityArtAlbumEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.BaoGPUImage;
import com.MDGround.HaiLanPrint.views.dialog.NotifyDialog;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by yoghourt on 5/18/16.
 */
public class ArtAlbumEditActivity extends ToolbarActivity<ActivityArtAlbumEditBinding> {

    private TemplateImageAdapter mTeplateImageAdapter;

    private int mCurrentSelectIndex = 0;

    private NotifyDialog mNotifyDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_art_album_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(0, SelectImageUtils.mTemplateImage.get(0));

        mDataBinding.templateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.templateRecyclerView.setLayoutManager(imageLayoutManager);
        mTeplateImageAdapter = new TemplateImageAdapter();
        mDataBinding.templateRecyclerView.setAdapter(mTeplateImageAdapter);
    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArtAlbumEditActivity.this);
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.if_add_to_my_work);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.toMainActivity(ArtAlbumEditActivity.this);
                    }
                });
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveToMyWork();
                    }
                });
                builder.show();
            }
        });

        mDataBinding.bgiImage.setOnSingleTouchListener(new BaoGPUImage.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                Intent intent = new Intent(ArtAlbumEditActivity.this, SelectAlbumWhenEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mTeplateImageAdapter.setOnSelectImageLisenter(new TemplateImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int position, MDImage mdImage) {

                if (mCurrentSelectIndex != position) {
                    float scaleFactor = mDataBinding.bgiImage.getmScaleFactor();
                    float rotateDegree = mDataBinding.bgiImage.getmRotationDegrees();

                    WorkPhoto workPhoto = SelectImageUtils.mAlreadySelectImage.get(mCurrentSelectIndex).getWorkPhoto();
                    workPhoto.setZoomSize((int) (scaleFactor * 100));
                    workPhoto.setRotate((int) rotateDegree);

                    showImageToGPUImageView(position, mdImage);
                }
            }
        });

        mDataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                WorkPhoto workPhoto = SelectImageUtils.mAlreadySelectImage.get(mCurrentSelectIndex).getWorkPhoto();
                workPhoto.setBrightLevel(progress);

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
        GlideUtil.loadImageByMDImage(mDataBinding.ivTemplate, mdImage, false);

        // 用户选择的图片加载
        MDImage selectImage = SelectImageUtils.mAlreadySelectImage.get(position);
        if (selectImage.getPhotoSID() != 0 || selectImage.getImageLocalPath() != null) {
            GlideUtil.loadImageAsBitmap(selectImage, new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                    WorkPhoto workPhoto = SelectImageUtils.mAlreadySelectImage.get(mCurrentSelectIndex).getWorkPhoto();

                    mDataBinding.bgiImage.loadNewImage(bitmap,
                            workPhoto.getZoomSize() / 100f,
                            workPhoto.getRotate(),
                            workPhoto.getBrightLevel() / 100f);
                }
            });
        } else {
            mDataBinding.bgiImage.loadNewImage(null);
        }
    }

    private void saveToMyWork() {
        ViewUtils.loading(this);
        // 保存到我的作品中
        MDGroundApplication.mOrderutUtils = new OrderUtils(this, true,
                1, MDGroundApplication.mInstance.getChoosedTemplate().getPrice());
        MDGroundApplication.mOrderutUtils.uploadImageRequest(this, 0);
    }

    private void generateOrder() {
        ViewUtils.loading(this);
        // 生成订单
        MDGroundApplication.mOrderutUtils = new OrderUtils(this, false,
                1, MDGroundApplication.mInstance.getChoosedTemplate().getPrice());
        MDGroundApplication.mOrderutUtils.uploadImageRequest(this, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage newMDImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);
            MDImage oldMDImage = SelectImageUtils.mAlreadySelectImage.get(mCurrentSelectIndex);

            newMDImage.setWorkPhoto(oldMDImage.getWorkPhoto());

            SelectImageUtils.mAlreadySelectImage.set(mCurrentSelectIndex, newMDImage);

            showImageToGPUImageView(mCurrentSelectIndex, SelectImageUtils.mTemplateImage.get(mCurrentSelectIndex));
        }
    }

    //region ACTION
    public void nextStepAction(View view) {
        for (int i = 0; i < SelectImageUtils.mAlreadySelectImage.size(); i++) {
            MDImage selectImage = SelectImageUtils.mAlreadySelectImage.get(i);

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
