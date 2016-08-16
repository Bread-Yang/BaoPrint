package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumWhenEditActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameEditBinding;
import com.MDGround.HaiLanPrint.enumobject.MaterialType;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.OriginalSizeBitmap;
import com.MDGround.HaiLanPrint.models.Size;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.utils.CreateImageUtil;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.TemplateUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.DrawingBoardView;
import com.MDGround.HaiLanPrint.views.ProductionView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PictureFrameEditActivity extends ToolbarActivity<ActivityPictureFrameEditBinding>
        implements DrawingBoardView.OnDrawingBoardClickListener {

    private ProductionView mProductionView;

    private Template mChooseTemplate;

    private int mPrice;

    private String mWorkFormat, mWorkStyle;

    private Bitmap mTemplateBitmap;

    private Size mSize;

    private float mRateOfEditArea = 1.0f;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture_frame_edit;
    }

    @Override
    protected void initData() {
        initBackgroundBitmap();

        mChooseTemplate = MDGroundApplication.sInstance.getChoosedTemplate();
        mChooseTemplate.setPageCount(1);
        MDGroundApplication.sInstance.setChoosedTemplate(mChooseTemplate);

        mDataBinding.setTemplate(mChooseTemplate);

        mDataBinding.tvPrice.setText(getString(R.string.yuan_amount,
                StringUtil.toYuanWithoutUnit(mChooseTemplate.getPrice())));
        changeMaterialAvailable();
    }

    @Override
    protected void setListener() {
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
                        refreshProductionView(-90);
                        break;
                    case R.id.rbPortrait:
                        refreshProductionView(90);
                        break;
                }
            }
        });
    }

    @Override
    public void onDrawingBoardTouch(DrawingBoardView drawingBoardView) {

    }

    @Override
    public void onDrawingBoardClick(DrawingBoardView drawingBoardView) {
        Intent intent = new Intent(PictureFrameEditActivity.this, SelectAlbumWhenEditActivity.class);
        startActivityForResult(intent, 0);
    }

    private void initBackgroundBitmap() {
        mProductionView = new ProductionView(this);
        mDataBinding.lltEdit.addView(mProductionView, 0);
        mProductionView.bringBackgroundToFront();

        MDImage templateImage = SelectImageUtils.sTemplateImage.get(0);


        GlideUtil.getOriginalSizeBitmap(this, templateImage, new SimpleTarget<OriginalSizeBitmap>() {
            @Override
            public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                mTemplateBitmap = resource.bitmap;
                mSize = resource.size;

                refreshProductionView(0f);
            }
        });

    }

    private void refreshProductionView(float rotateAngle) {
        if (rotateAngle != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateAngle);
            mTemplateBitmap = Bitmap.createBitmap(mTemplateBitmap, 0, 0,
                    mTemplateBitmap.getWidth(), mTemplateBitmap.getHeight(), matrix, true);
        }

        mProductionView.clear();
        mProductionView.backgroundLayer.setImageBitmap(mTemplateBitmap);

        mRateOfEditArea = TemplateUtils.getRateOfEditAreaOnAndroid(mSize);

        // 根据返回Bitmap的大小设置在android上对应的宽高
        Point sizePoint = TemplateUtils.getEditAreaSizeOnAndroid(mSize);

        final int width = sizePoint.x;
        final float height = sizePoint.y;

        mProductionView.setWidthAndHeight(width, (int) height);

        MDImage image = SelectImageUtils.sAlreadySelectImage.get(0);

        GlideUtil.getOriginalSizeBitmap(PictureFrameEditActivity.this, image, new SimpleTarget<OriginalSizeBitmap>() {
            @Override
            public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                Bitmap copyBitmap = resource.bitmap.copy(resource.bitmap.getConfig(), true); // safe copy

                Matrix matrix = new Matrix();
                addDrawBoard(0, 0, width, height, resource.size.width, resource.size.height,
                        copyBitmap, copyBitmap, matrix, mRateOfEditArea, 0);
                ViewUtils.dismiss();
            }
        });

//        GlideUtil.loadImageAsBitmap(image, new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap moduleBitmap, GlideAnimation<? super
//                    Bitmap> glideAnimation) {
//
//                moduleBitmap = moduleBitmap.copy(moduleBitmap.getConfig(), true); // safe copy
//
//                Matrix matrix = new Matrix();
//                addDrawBoard(0, 0, width, height,
//                        moduleBitmap, moduleBitmap, matrix, 1.0f, 0);
//            }
//        });
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
        CreateImageUtil.createAllPageHasModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
            @Override
            public void onComplete(final List<String> allCompositeImageLocalPathList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 保存到我的作品中
                        MDGroundApplication.sOrderutUtils = new OrderUtils(PictureFrameEditActivity.this, true,
                                mChooseTemplate.getPageCount(),
                                mPrice, mWorkFormat, null, mWorkStyle);
                        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(PictureFrameEditActivity.this,
                                allCompositeImageLocalPathList, 0);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MDImage newMDImage = data.getParcelableExtra(Constants.KEY_SELECT_IMAGE);

            MDImage oldMDImage = SelectImageUtils.sAlreadySelectImage.get(0);

            oldMDImage.setPhotoID(newMDImage.getPhotoID());
            oldMDImage.setPhotoSID(newMDImage.getPhotoSID());
            oldMDImage.setImageLocalPath(newMDImage.getImageLocalPath());

            GlideUtil.getOriginalSizeBitmap(PictureFrameEditActivity.this, oldMDImage, new SimpleTarget<OriginalSizeBitmap>() {
                @Override
                public void onResourceReady(OriginalSizeBitmap resource, GlideAnimation<? super OriginalSizeBitmap> glideAnimation) {
                    Bitmap copyBitmap = resource.bitmap.copy(resource.bitmap.getConfig(), true); // safe copy

                    mProductionView.mDrawingBoardViewSparseArray.valueAt(0)
                            .setUserSelectBitmap(copyBitmap, new Matrix(), mRateOfEditArea,
                                    resource.size.width, resource.size.height);
                }
            });

//            GlideUtil.loadImageAsBitmap(oldMDImage, new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(final Bitmap newBitmap, GlideAnimation<? super
//                        Bitmap> glideAnimation) {
//                    mProductionView.mDrawingBoardViewSparseArray.valueAt(0).setUserSelectBitmap(newBitmap, new Matrix(), 1.0f);
//                }
//            });
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

        CreateImageUtil.createAllPageWithoutModules(new CreateImageUtil.onCreateAllComposteImageCompleteListner() {
            @Override
            public void onComplete(final List<String> allCompositeImageLocalPathList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MDGroundApplication.sOrderutUtils = new OrderUtils(PictureFrameEditActivity.this, false,
                                mChooseTemplate.getPageCount(),
                                mPrice, mWorkFormat, null, mWorkStyle);

                        MDGroundApplication.sOrderutUtils.uploadAllCompositeImageReuqest(PictureFrameEditActivity.this,
                                allCompositeImageLocalPathList, 0);
                    }
                });
            }
        });
    }
    //endregion
}
