package com.MDGround.HaiLanPrint.activity.postcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.TemplateImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityGlobalTemplateEditBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.PhotoTemplateAttachFrame;
import com.MDGround.HaiLanPrint.utils.CreateImageUtil;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.TemplateUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.DrawingBoardView;
import com.MDGround.HaiLanPrint.views.ProductionView;
import com.MDGround.HaiLanPrint.views.dialog.NotifyDialog;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by yoghourt on 5/18/16.
 */
public class GlobalTemplateEditActivity extends ToolbarActivity<ActivityGlobalTemplateEditBinding> {

    private ProductionView mProductionView;

    private TemplateImageAdapter mTeplateImageAdapter;

    private int mCurrentSelectIndex = 0;

    private NotifyDialog mNotifyDialog;

    private AlertDialog mAlertDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_global_template_edit;
    }

    @Override
    protected void initData() {
        mProductionView = new ProductionView(this);
        mDataBinding.lltEdit.addView(mProductionView, 0);

        mAlertDialog = ViewUtils.createAlertDialog(this, getString(R.string.if_add_to_my_work),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.toMainActivity(GlobalTemplateEditActivity.this);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveToMyWork();
                    }
                });

        selectPageByIndexToEdit(0, SelectImageUtils.sTemplateImage.get(0));

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
                mAlertDialog.show();
            }
        });

//        mDataBinding.bgiImage.setOnSingleTouchListener(new BaoGPUImage.OnSingleTouchListener() {
//            @Override
//            public void onSingleTouch() {
//                Intent intent = new Intent(GlobalTemplateEditActivity.this, SelectAlbumWhenEditActivity.class);
//                startActivityForResult(intent, 0);
//            }
//        });

        mTeplateImageAdapter.setOnSelectImageLisenter(new TemplateImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int pageIndex, MDImage mdImage) {

                if (mCurrentSelectIndex != pageIndex) {

                    saveCurrentPageEditStatus();

                    selectPageByIndexToEdit(pageIndex, mdImage);
                }
            }
        });

        mDataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProductionView.mDrawingBoardViewSparseArray.valueAt(0).setBrightness(progress);

                mDataBinding.tvPercent.setText(getString(R.string.percent, progress) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage newMDImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);
            MDImage oldMDImage = SelectImageUtils.sAlreadySelectImage.get(mCurrentSelectIndex);

            newMDImage.setWorkPhoto(oldMDImage.getWorkPhoto());

            SelectImageUtils.sAlreadySelectImage.set(mCurrentSelectIndex, newMDImage);

            selectPageByIndexToEdit(mCurrentSelectIndex, SelectImageUtils.sTemplateImage.get(mCurrentSelectIndex));
        }
    }

    @Override
    public void onBackPressed() {
        mAlertDialog.show();
    }

    // 保存当前页的编辑状态
    private void saveCurrentPageEditStatus() {
        MDImage templateImage = SelectImageUtils.sTemplateImage.get(mCurrentSelectIndex);
        List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

        for (int i = 0; i < photoTemplateAttachFrameList.size(); i++) {

            Matrix matrix = mProductionView.mDrawingBoardViewSparseArray.get(i).getMatrixOfEditPhoto();

            String matrixString = TemplateUtils.getStringByMatrix(matrix);

            photoTemplateAttachFrameList.get(i).setMatrix(matrixString);
        }
    }

    private void selectPageByIndexToEdit(final int pageIndex, final MDImage mdImage) {
        mCurrentSelectIndex = pageIndex;

        mProductionView.clear();

        // 模板背景图片加载
        GlideUtil.loadImageAsBitmap(mdImage, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap backgroundBitmap, GlideAnimation<? super
                    Bitmap> glideAnimation) {
                mProductionView.backgroundLayer.setImageBitmap(backgroundBitmap);
                setAndroidWidthAndHeight(backgroundBitmap);

                // 各个编辑模块加载
                MDImage templateImage = SelectImageUtils.sTemplateImage.get(pageIndex);
                List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

                if (photoTemplateAttachFrameList != null) {
                    for (int moduleIndex = 0; moduleIndex < photoTemplateAttachFrameList.size(); moduleIndex++) {
                        final PhotoTemplateAttachFrame photoTemplateAttachFrame = photoTemplateAttachFrameList.get(moduleIndex);

                        final int finalI = moduleIndex;

                        MDImage moduleShowImage = photoTemplateAttachFrame.getUserSelectImage();
                        // 加载用户选择的图片
                        GlideUtil.loadImageAsBitmap(moduleShowImage, new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap moduleBitmap, GlideAnimation<? super
                                    Bitmap> glideAnimation) {

                                moduleBitmap = moduleBitmap.copy(moduleBitmap.getConfig(), true); // safe copy

                                int dx = photoTemplateAttachFrame.getPositionX();
                                int dy = photoTemplateAttachFrame.getPositionY();
                                int width = photoTemplateAttachFrame.getWidth();
                                int height = photoTemplateAttachFrame.getHeight();

                                float coefficient = TemplateUtils.getRateOfEditWidthOnAndroid(backgroundBitmap);
                                float androidDx = dx * coefficient;
                                float androidDy = dy * coefficient;
                                float androidWidth = width * coefficient;
                                float androidHeight = height * coefficient;

                                Matrix matrix = TemplateUtils.getMatrixByString(photoTemplateAttachFrame.getMatrix());
                                // 添加module编辑区域
                                addDrawBoard(androidDx, androidDy, androidWidth, androidHeight,
                                        moduleBitmap, moduleBitmap, matrix, 1.0f, finalI);
                            }
                        });

                    }
                }
            }
        });
    }

    // 根据返回Bitmap的大小设置在android上对应的宽高
    private void setAndroidWidthAndHeight(Bitmap bitmap) {
        float height = TemplateUtils.getEditHeightOnAndroid(bitmap);

        mProductionView.setWidthAndHeight(ViewUtils.screenWidth(), (int) height);
    }

    private void saveToMyWork() {
        ViewUtils.loading(this);
        // 保存到我的作品中
        MDGroundApplication.sOrderutUtils = new OrderUtils(this, true,
                1, MDGroundApplication.sInstance.getChoosedTemplate().getPrice());
        MDGroundApplication.sOrderutUtils.uploadImageRequest(this, 0);
    }

    private void generateOrder() {
        ViewUtils.loading(this);
        // 生成订单
        MDGroundApplication.sOrderutUtils = new OrderUtils(this, false,
                1, MDGroundApplication.sInstance.getChoosedTemplate().getPrice());
        MDGroundApplication.sOrderutUtils.uploadImageRequest(this, 0);
    }

    private void addDrawBoard(float dx, float dy, float w, float h,
                              Bitmap mouldBmp, Bitmap photoBmp, Matrix matrix, float rate, int position) {
        DrawingBoardView drawingBoardView = new DrawingBoardView(this, w, h, mouldBmp, photoBmp, matrix, rate);
        drawingBoardView.setTag(Integer.valueOf(position));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) w, (int) h);
        layoutParams.setMargins((int) dx, (int) dy, 0, 0);
        drawingBoardView.setLayoutParams(layoutParams);

        mProductionView.drawBoardLayer.addView(drawingBoardView);
        mProductionView.mDrawingBoardViewSparseArray.append(position, drawingBoardView);
    }

    //region ACTION
    public void nextStepAction(View view) {
//        for (int i = 0; i < SelectImageUtils.sAlreadySelectImage.size(); i++) {
//            MDImage selectImage = SelectImageUtils.sAlreadySelectImage.get(i);
//
//            if (selectImage.getPhotoSID() == 0 && selectImage.getImageLocalPath() == null) {
//                if (mNotifyDialog == null) {
//                    mNotifyDialog = new NotifyDialog(this);
//                    mNotifyDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {
//                        @Override
//                        public void onSureClick() {
//                            mNotifyDialog.dismiss();
//                            generateOrder();
//                        }
//                    });
//                }
//                mNotifyDialog.show();
//
//                mNotifyDialog.setTvContent(getString(R.string.not_add_image, i + 1));
//                return;
//            }
//        }
//
//        generateOrder();

        CreateImageUtil.createAll(SelectImageUtils.sTemplateImage);
    }
    //endregion
}
