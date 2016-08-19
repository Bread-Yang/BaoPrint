package com.MDGround.HaiLanPrint.activity.templateedit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.MDGround.HaiLanPrint.models.OriginalSizeBitmap;
import com.MDGround.HaiLanPrint.models.PhotoTemplateAttachFrame;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.models.WorkPhotoEdit;
import com.MDGround.HaiLanPrint.utils.CreateImageUtil;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
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

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;

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

    private float mRateOfEditArea = 1.0f;

    private ProductType mProductType;

    private DateTime mDateTime = new DateTime();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_global_template_edit;
    }

    @Override
    protected void initData() {
        initOrder();

        mProductionView = new ProductionView(this);
//        mDataBinding.lltEdit.addView(mProductionView, 0);
        mDataBinding.fltEdit.addView(mProductionView, 0);

        mProductType = MDGroundApplication.sInstance.getChoosedProductType();

        // 明信片和Lomo卡可以输入文字
        if (mProductType == ProductType.Postcard || mProductType == ProductType.LOMOCard) {
            mDataBinding.cetInput.setVisibility(View.VISIBLE);
        }

        // 手机壳,拼图,魔术杯没有模版选择
        if (mProductType == ProductType.PhoneShell
                || mProductType == ProductType.Puzzle
                || mProductType == ProductType.MagicCup) {
            mDataBinding.templateRecyclerView.setVisibility(View.GONE);
        }

        // 只有杂志册,艺术册,个性月历 这三个功能块有定位块, 否则背景图片显示在最前面
        if (!TemplateUtils.isTemplateHasModules()) {
            mProductionView.bringBackgroundToFront();
        }

        mAlertDialog = ViewUtils.createAlertDialog(this, getString(R.string.if_add_to_my_work),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        NavUtils.toMainActivity(GlobalTemplateEditActivity.this);
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

        mDataBinding.cetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                WorkPhoto workPhoto = SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex).getWorkPhoto();

                workPhoto.setDescription(s.toString());
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

        mTeplateImageAdapter.setOnSelectImageLisenter(new TemplateImageAdapter.onSelectImageLisenter() {
            @Override
            public void selectImage(int pageIndex, MDImage mdImage) {

                if (mCurrentSelectPageIndex != pageIndex) {

                    saveCurrentPageEditStatus();

                    selectPageByIndexToEdit(pageIndex, mdImage);

                    // 如果是日历模块,则显示日历
                    if (mProductType == ProductType.Calendar) {
                        if (pageIndex == 0) {
                            mDataBinding.calendarCard.setVisibility(View.GONE);
                        } else {
                            mDataBinding.calendarCard.setVisibility(View.VISIBLE);

                            DateTime newDateTime = mDateTime.plusMonths(pageIndex - 1);

                            mDataBinding.calendarCard.setTime(newDateTime.getYear(), newDateTime.getMonthOfYear());
                        }
                    }
                }
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

//            SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex)
//                    .getWorkPhoto().setDescription("");
            // 切换模板图片后，则输入栏清空显示
            mDataBinding.cetInput.setText("");

//            GlideUtil.loadImageAsBitmap(oldMDImage, new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(final Bitmap newBitmap, GlideAnimation<? super
//                        Bitmap> glideAnimation) {
//                    Bitmap copyBitmap = newBitmap.copy(newBitmap.getConfig(), true); // safe copy
//
//                    mCurrentSelectDrawingBoardView.setUserSelectBitmap(copyBitmap, new Matrix(), 1.0f);
//                }
//            });

            GlideUtil.getOriginalSizeBitmap(GlobalTemplateEditActivity.this, oldMDImage, new SimpleTarget<OriginalSizeBitmap>() {
                @Override
                public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                    Bitmap copyBitmap = resource.bitmap.copy(resource.bitmap.getConfig(), true); // safe copy

                    mCurrentSelectDrawingBoardView.setUserSelectBitmap(copyBitmap,
                            new Matrix(), mRateOfEditArea, resource.size.width, resource.size.height);
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

    private void initOrder() {
        ProductType productType = MDGroundApplication.sInstance.getChoosedProductType();

        if (productType == ProductType.PhoneShell) {
            MDGroundApplication.sOrderutUtils = new OrderUtils(this, false,
                    1, MDGroundApplication.sInstance.getChoosedTemplate().getPrice(),
                    null,
                    MDGroundApplication.sInstance.getChoosedTemplate().getSelectMaterial(),
                    null);
        } else {
            int price = 0;
            if (productType == ProductType.MagicCup) {
                price = MDGroundApplication.sInstance.getChoosedMeasurement().getPrice();
            } else {
                price = MDGroundApplication.sInstance.getChoosedTemplate().getPrice();
            }

            MDGroundApplication.sOrderutUtils = new OrderUtils(this, false,
                    1, price);
        }
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

                    WorkPhotoEdit workPhotoEdit = SelectImageUtils.getMdImageByPageIndexAndModuleIndex(mCurrentSelectPageIndex, i).getWorkPhotoEdit();

                    // calculate the degree of rotation
                    float rAngle = Math.round(Math.atan2(values[Matrix.MSKEW_X], values[Matrix.MSCALE_X]) * (180 / Math.PI));

                    KLog.e("tx : " + tx);
                    KLog.e("ty : " + ty);
                    KLog.e("rScale : " + rScale);
                    KLog.e("rAngle : " + rAngle);

                    workPhotoEdit.setPositionX((int) tx);
                    workPhotoEdit.setPositionY((int) ty);
                    workPhotoEdit.setRotate(rAngle);
                    workPhotoEdit.setZoomSize(rScale);

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

        WorkPhoto workPhoto = mdImage.getWorkPhoto();

        String description = workPhoto.getDescription();

        mDataBinding.cetInput.setText("");
        if (description != null) {
            mDataBinding.cetInput.append(description);
        }
        // 模板背景图片加载
        GlideUtil.getOriginalSizeBitmap(GlobalTemplateEditActivity.this, mdImage, new SimpleTarget<OriginalSizeBitmap>() {
            @Override
            public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                mProductionView.backgroundLayer.setImageBitmap(resource.bitmap);

                // 根据返回Bitmap的大小设置在android上对应的宽高
                Point sizePoint = TemplateUtils.getEditAreaSizeOnAndroid(resource.size);
                final int width = sizePoint.x;
                final float height = sizePoint.y;

                mProductionView.setWidthAndHeight(width, (int) height);

                ViewUtils.loading(GlobalTemplateEditActivity.this);

                mRateOfEditArea = TemplateUtils.getRateOfEditAreaOnAndroid(resource.size);

                // 杂志册,艺术册,个性月历 这三个功能块有定位块
                if (TemplateUtils.isTemplateHasModules()) {
                    // 各个编辑定位块加载
                    MDImage templateImage = SelectImageUtils.sTemplateImage.get(pageIndex);
                    final List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

                    if (photoTemplateAttachFrameList != null && photoTemplateAttachFrameList.size() > 0) {
                        for (int moduleIndex = 0; moduleIndex < photoTemplateAttachFrameList.size(); moduleIndex++) {
                            final PhotoTemplateAttachFrame photoTemplateAttachFrame = photoTemplateAttachFrameList.get(moduleIndex);

                            final int finalI = moduleIndex;

                            MDImage moduleShowImage = SelectImageUtils
                                    .getMdImageByPageIndexAndModuleIndex(mCurrentSelectPageIndex, moduleIndex);

                            // 加载用户选择的图片
                            GlideUtil.getOriginalSizeBitmap(GlobalTemplateEditActivity.this, moduleShowImage, new SimpleTarget<OriginalSizeBitmap>() {
                                @Override
                                public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                                    KLog.e("图片的original size是: ", String.format(Locale.ROOT, "%dx%d", resource.size.width, resource.size.height));
                                    KLog.e("图片的压缩的 size是: ", String.format(Locale.ROOT, "%dx%d", resource.bitmap.getWidth(), resource.bitmap.getHeight()));

                                    Bitmap copyBitmap = resource.bitmap.copy(resource.bitmap.getConfig(), true); // safe copy

                                    int dx = photoTemplateAttachFrame.getPositionX();
                                    int dy = photoTemplateAttachFrame.getPositionY();
                                    int width = photoTemplateAttachFrame.getWidth();
                                    int height = photoTemplateAttachFrame.getHeight();

                                    float androidDx = dx * mRateOfEditArea;
                                    float androidDy = dy * mRateOfEditArea;
                                    float androidWidth = width * mRateOfEditArea;
                                    float androidHeight = height * mRateOfEditArea;

                                    Matrix matrix = TemplateUtils.getMatrixByString(photoTemplateAttachFrame.getMatrix());
                                    // 添加module编辑区域
                                    addDrawBoard(androidDx, androidDy, androidWidth, androidHeight, resource.size.width, resource.size.height,
                                            copyBitmap, copyBitmap, matrix, mRateOfEditArea, finalI);

                                    if (mProductionView.mDrawingBoardViewSparseArray.size() == photoTemplateAttachFrameList.size()) {
                                        ViewUtils.dismiss();
                                    }
                                }
                            });

                            // 加载用户选择的图片
//                            GlideUtil.loadImageAsBitmap(moduleShowImage, new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap moduleBitmap, GlideAnimation<? super
//                                        Bitmap> glideAnimation) {
//
//                                    Bitmap copyBitmap = moduleBitmap.copy(moduleBitmap.getConfig(), true); // safe copy
//
//                                    int dx = photoTemplateAttachFrame.getPositionX();
//                                    int dy = photoTemplateAttachFrame.getPositionY();
//                                    int width = photoTemplateAttachFrame.getWidth();
//                                    int height = photoTemplateAttachFrame.getHeight();
//
//                                    float rate = TemplateUtils.getRateOfEditAreaOnAndroid(backgroundBitmap);
//                                    float androidDx = dx * rate;
//                                    float androidDy = dy * rate;
//                                    float androidWidth = width * rate;
//                                    float androidHeight = height * rate;
//
//                                    Matrix matrix = TemplateUtils.getMatrixByString(photoTemplateAttachFrame.getMatrix());
//                                    // 添加module编辑区域
//                                    addDrawBoard(androidDx, androidDy, androidWidth, androidHeight,
//                                            copyBitmap, copyBitmap, matrix, rate, finalI);
//
//                                    if (mProductionView.mDrawingBoardViewSparseArray.size() == photoTemplateAttachFrameList.size()) {
//                                        ViewUtils.dismiss();
//                                    }
//                                }
//                            });
                        }
                    } else {
                        ViewUtils.dismiss();
                    }
                } else {
                    MDImage image = SelectImageUtils.sAlreadySelectImage.get(mCurrentSelectPageIndex);

                    GlideUtil.getOriginalSizeBitmap(GlobalTemplateEditActivity.this, image, new SimpleTarget<OriginalSizeBitmap>() {
                        @Override
                        public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                            Bitmap copyBitmap = resource.bitmap.copy(resource.bitmap.getConfig(), true); // safe copy

                            Matrix matrix = TemplateUtils.getMatrixByString(SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex)
                                    .getWorkPhoto().getMatrix());
                            addDrawBoard(0, 0, width, height, resource.size.width, resource.size.height,
                                    copyBitmap, copyBitmap, matrix, mRateOfEditArea, 0);
                            ViewUtils.dismiss();
                        }
                    });

//                    GlideUtil.loadImageAsBitmap(image, new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap moduleBitmap, GlideAnimation<? super
//                                Bitmap> glideAnimation) {
//
//                            moduleBitmap = moduleBitmap.copy(moduleBitmap.getConfig(), true); // safe copy
//
//                            Matrix matrix = TemplateUtils.getMatrixByString(SelectImageUtils.sTemplateImage.get(mCurrentSelectPageIndex)
//                                    .getWorkPhoto().getMatrix());
//                            addDrawBoard(0, 0, width, height,
//                                    moduleBitmap, moduleBitmap, matrix, 1.0f, 0);
//                            ViewUtils.dismiss();
//                        }
//                    });
                }
            }
        });
    }

    private void saveToMyWork() {
        saveCurrentPageEditStatus();
        ViewUtils.loading(this);
        CreateImageUtil.createAllPageHasModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
            @Override
            public void onComplete(final List<String> allCompositeImageLocalPathList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 保存到我的作品中
                        MDGroundApplication.sOrderutUtils.mIsJustSaveUserWork = true;
                        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(GlobalTemplateEditActivity.this,
                                allCompositeImageLocalPathList, 0);
                    }
                });
            }
        });
    }

    private void addDrawBoard(float androidDx, float androidDy, float androidWidth, float androidHeight,
                              int userSelectBitmapOriginalWidth, int UserSelectBitmaporiginalHeight,
                              Bitmap mouldBmp, Bitmap userSelectBitmap, Matrix matrix, float rate, int position) {
        DrawingBoardView drawingBoardView = new DrawingBoardView(this, this,
                androidWidth, androidHeight, mouldBmp, userSelectBitmap, matrix, rate,
                userSelectBitmapOriginalWidth, UserSelectBitmaporiginalHeight);
        drawingBoardView.setTag(Integer.valueOf(position));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) androidWidth, (int) androidHeight);
        layoutParams.setMargins((int) androidDx, (int) androidDy, 0, 0);
        drawingBoardView.setLayoutParams(layoutParams);

        mProductionView.drawBoardLayer.addView(drawingBoardView);
        mProductionView.mDrawingBoardViewSparseArray.append(position, drawingBoardView);
    }

    private void generateOrder(List<String> allCompositeImageLocalPathList) {
        // 生成订单
        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(this, allCompositeImageLocalPathList, 0);
    }

    //region ACTION
    public void nextStepAction(View view) {
        saveCurrentPageEditStatus();
        ViewUtils.loading(this);
        if (TemplateUtils.isTemplateHasModules()) {
            CreateImageUtil.createAllPageHasModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
                @Override
                public void onComplete(final List<String> allCompositeImageLocalPathList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ViewUtils.dismiss();
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
