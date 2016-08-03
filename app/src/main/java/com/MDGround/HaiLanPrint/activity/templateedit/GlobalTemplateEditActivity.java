package com.MDGround.HaiLanPrint.activity.templateedit;

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
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.adapter.TemplateImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityGlobalTemplateEditBinding;
import com.MDGround.HaiLanPrint.enumobject.ProductType;
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
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by yoghourt on 5/18/16.
 */
public class GlobalTemplateEditActivity extends ToolbarActivity<ActivityGlobalTemplateEditBinding>
        implements DrawingBoardView.OnDrawingBoardClickListener {

    private ProductionView mProductionView;

    private TemplateImageAdapter mTeplateImageAdapter;

    private int mCurrentSelectPageIndex = 0;

    private NotifyDialog mNotifyDialog;

    private AlertDialog mAlertDialog;

    private DrawingBoardView mCurrentSelectDrawingBoardView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_global_template_edit;
    }

    @Override
    protected void initData() {

        ProductType chooseProductType = MDGroundApplication.sInstance.getChoosedProductType();

        mProductionView = new ProductionView(this);
        mDataBinding.lltEdit.addView(mProductionView, 0);

        // 只有杂志册,艺术册,个性月历 这三个功能块有定位块, 否则背景图片显示在最前面
        if (!TemplateUtils.isTemplateHasModules()) {
            mProductionView.bringBackgroundToFront();
        }

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

        mTeplateImageAdapter.setOnSelectImageLisenter(new TemplateImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int pageIndex, MDImage mdImage) {

                if (mCurrentSelectPageIndex != pageIndex) {

                    saveCurrentPageEditStatus();

                    selectPageByIndexToEdit(pageIndex, mdImage);
                }
            }
        });

        mDataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mCurrentSelectDrawingBoardView == null) {
                    mProductionView.mDrawingBoardViewSparseArray.get(0).setBrightness(progress);
                } else {
                    mCurrentSelectDrawingBoardView.setBrightness(progress);
                }

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

            int moduleIndex = (int) mCurrentSelectDrawingBoardView.getTag();

            MDImage oldMDImage = SelectImageUtils.getMdImageByPageIndexAndModuleIndex(mCurrentSelectPageIndex, moduleIndex);

            oldMDImage.setPhotoID(newMDImage.getPhotoID());
            oldMDImage.setPhotoSID(newMDImage.getPhotoSID());
            oldMDImage.setImageLocalPath(newMDImage.getImageLocalPath());

            GlideUtil.loadImageAsBitmap(oldMDImage, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(final Bitmap newBitmap, GlideAnimation<? super
                        Bitmap> glideAnimation) {
                    mCurrentSelectDrawingBoardView.setPhotoBitmap(newBitmap, new Matrix(), 1.0f);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        mAlertDialog.show();
    }

    @Override
    public void onDrawingBoardTouch(DrawingBoardView drawingBoardView) {
        mCurrentSelectDrawingBoardView = drawingBoardView;
    }

    @Override
    public void onDrawingBoardClick(DrawingBoardView drawingBoardView) {
        mCurrentSelectDrawingBoardView = drawingBoardView;

        Intent intent = new Intent(GlobalTemplateEditActivity.this, SelectAlbumWhenEditActivity.class);
        startActivityForResult(intent, 0);
    }

    // 保存当前页的编辑状态
    private void saveCurrentPageEditStatus() {
        mCurrentSelectDrawingBoardView = null;

        if (TemplateUtils.isTemplateHasModules()) {
            MDImage templateImage = SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex);
            List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

            if (photoTemplateAttachFrameList != null) {
                for (int i = 0; i < photoTemplateAttachFrameList.size(); i++) {

                    DrawingBoardView drawingBoardView = mProductionView.mDrawingBoardViewSparseArray.get(i);

                    Matrix matrix = drawingBoardView.getMatrixOfEditPhoto();

                    float[] values = new float[9];

                    matrix.getValues(values);

                    float tx = values[Matrix.MTRANS_X];
                    float ty = values[Matrix.MTRANS_Y];

                    // calculate real scale
                    float scalex = values[Matrix.MSCALE_X];
                    float skewy = values[Matrix.MSKEW_Y];
                    float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);

                    // calculate the degree of rotation
                    float rAngle = Math.round(Math.atan2(values[Matrix.MSKEW_X], values[Matrix.MSCALE_X]) * (180 / Math.PI));

                    KLog.e("tx : " + tx);
                    KLog.e("ty : " + ty);
                    KLog.e("rScale : " + rScale);
                    KLog.e("rAngle : " + rAngle);

                    String matrixString = TemplateUtils.getStringByMatrix(matrix);

                    photoTemplateAttachFrameList.get(i).setMatrix(matrixString);
                }
            }
        } else {
            MDImage templateImage = SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex);

            DrawingBoardView drawingBoardView = mProductionView.mDrawingBoardViewSparseArray.get(0);

            Matrix matrix = drawingBoardView.getMatrixOfEditPhoto();

            String matrixString = TemplateUtils.getStringByMatrix(matrix);

            templateImage.getWorkPhoto().setMatrix(matrixString);
        }
    }

    private void selectPageByIndexToEdit(final int pageIndex, final MDImage mdImage) {
        mCurrentSelectPageIndex = pageIndex;

        mProductionView.clear();

        // 模板背景图片加载
        GlideUtil.loadImageAsBitmap(mdImage, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap backgroundBitmap, GlideAnimation<? super
                    Bitmap> glideAnimation) {
                mProductionView.backgroundLayer.setImageBitmap(backgroundBitmap);

                // 根据返回Bitmap的大小设置在android上对应的宽高
                final int width = ViewUtils.screenWidth();
                final float height = TemplateUtils.getEditHeightOnAndroid(backgroundBitmap);

                mProductionView.setWidthAndHeight(width, (int) height);

                // 杂志册,艺术册,个性月历 这三个功能块有定位块
                if (TemplateUtils.isTemplateHasModules()) {

                    // 各个编辑定位块加载
                    MDImage templateImage = SelectImageUtils.sTemplateImage.get(pageIndex);
                    List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

                    if (photoTemplateAttachFrameList != null) {
                        for (int moduleIndex = 0; moduleIndex < photoTemplateAttachFrameList.size(); moduleIndex++) {
                            final PhotoTemplateAttachFrame photoTemplateAttachFrame = photoTemplateAttachFrameList.get(moduleIndex);

                            final int finalI = moduleIndex;

                            MDImage moduleShowImage = SelectImageUtils
                                    .getMdImageByPageIndexAndModuleIndex(mCurrentSelectPageIndex, moduleIndex);
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
                } else {
                    MDImage image = SelectImageUtils.sAlreadySelectImage.get(mCurrentSelectPageIndex);
                    GlideUtil.loadImageAsBitmap(image, new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap moduleBitmap, GlideAnimation<? super
                                Bitmap> glideAnimation) {

                            moduleBitmap = moduleBitmap.copy(moduleBitmap.getConfig(), true); // safe copy

                            Matrix matrix = TemplateUtils.getMatrixByString(SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex)
                                    .getWorkPhoto().getMatrix());
                            addDrawBoard(0, 0, width, height,
                                    moduleBitmap, moduleBitmap, matrix, 1.0f, 0);
                        }
                    });
                }
            }
        });
    }

    private void saveToMyWork() {
        ViewUtils.loading(this);
        CreateImageUtil.createAllPageHasModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
            @Override
            public void onComplete(final List<String> allCompositeImageLocalPathList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 保存到我的作品中
                        MDGroundApplication.sOrderutUtils = new OrderUtils(GlobalTemplateEditActivity.this, true,
                                1, MDGroundApplication.sInstance.getChoosedTemplate().getPrice());
                        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(GlobalTemplateEditActivity.this,
                                allCompositeImageLocalPathList, 0);
                    }
                });
            }
        });
    }

    private void addDrawBoard(float dx, float dy, float w, float h,
                              Bitmap mouldBmp, Bitmap photoBmp, Matrix matrix, float rate, int position) {
        DrawingBoardView drawingBoardView = new DrawingBoardView(this, this,
                w, h, mouldBmp, photoBmp, matrix, rate);
        drawingBoardView.setTag(Integer.valueOf(position));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) w, (int) h);
        layoutParams.setMargins((int) dx, (int) dy, 0, 0);
        drawingBoardView.setLayoutParams(layoutParams);

        mProductionView.drawBoardLayer.addView(drawingBoardView);
        mProductionView.mDrawingBoardViewSparseArray.append(position, drawingBoardView);
    }

    private void generateOrder(List<String> allCompositeImageLocalPathList) {
        ViewUtils.loading(this);
        // 生成订单
        MDGroundApplication.sOrderutUtils = new OrderUtils(this, false,
                1, MDGroundApplication.sInstance.getChoosedTemplate().getPrice());
        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(this, allCompositeImageLocalPathList, 0);
    }

    //region ACTION
    public void nextStepAction(View view) {
        if (TemplateUtils.isTemplateHasModules()) {
            CreateImageUtil.createAllPageHasModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
                @Override
                public void onComplete(final List<String> allCompositeImageLocalPathList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            generateOrder(allCompositeImageLocalPathList);
                        }
                    });
                }
            });
        } else {
            CreateImageUtil.createAllPageWithoutModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
                @Override
                public void onComplete(final List<String> allCompositeImageLocalPathList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            generateOrder(allCompositeImageLocalPathList);
                        }
                    });
                }
            });

        }
    }
    //endregion
}
