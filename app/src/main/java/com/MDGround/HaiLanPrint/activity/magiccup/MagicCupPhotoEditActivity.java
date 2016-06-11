package com.MDGround.HaiLanPrint.activity.magiccup;

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
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.OrderWorkPhoto;
import com.MDGround.HaiLanPrint.models.WorkInfo;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/18/16.
 */
public class MagicCupPhotoEditActivity extends ToolbarActivity<ActivityPhotoEditBinding> {

    private TemplageImageAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void initData() {
        showImageToGPUImageView(0, SelectImageUtil.mAlreadySelectImage.get(0));

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
        saveUserWorkReqeust();
    }
    //endregion

    //region SERVER
    private void uploadImageRequest() {
        final MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(0);
        File file = new File(mdImage.getImageLocalPath());

        FileRestful.getInstance().UploadCloudPhoto(false, file, null, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                final MDImage responseImage = response.body().getContent(MDImage.class);
//                responseImage.setPhotoCount(mdImage.getPhotoCount());
//
//                if (mdImage.getSyntheticImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getSyntheticImageLocalPath())) { // 合成图片
//                    File syntheticFile = new File(mdImage.getSyntheticImageLocalPath());
//
//                    // 上传合成图片
//                    FileRestful.getInstance().UploadCloudPhoto(false, syntheticFile, null, new Callback<ResponseData>() {
//                        @Override
//                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                            MDImage responseSyntheticImage = response.body().getContent(MDImage.class);
//
//                            responseImage.setSyntheticPhotoID(responseSyntheticImage.getPhotoID());
//                            responseImage.setSyntheticPhotoSID(responseSyntheticImage.getPhotoSID());
//
//                            SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
//                            uploadImageRequest(nextUploadIndex);
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseData> call, Throwable t) {
//
//                        }
//                    });
//                } else {
//
//                }
                saveUserWorkReqeust();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
            }
        });
    }

    private void saveUserWorkReqeust() {
        WorkInfo workInfo = new WorkInfo();
        workInfo.setCreatedTime(DateUtils.getServerDateStringByDate(new Date()));
        workInfo.setPhotoCount(1);
        workInfo.setPrice(MDGroundApplication.mChoosedMeasurement.getPrice());
        workInfo.setTypeID(MDGroundApplication.mChoosedProductType.value());
        workInfo.setTemplateID(MDGroundApplication.mChoosedTemplate.getTemplateID());
        workInfo.setTypeName(ProductType.getProductName(MDGroundApplication.mChoosedProductType));
        workInfo.setUserID(MDGroundApplication.mLoginUser.getUserID());
        workInfo.setWorkDesc("");

        GlobalRestful.getInstance().SaveUserWork(workInfo, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                WorkInfo responseWorkInfo = response.body().getContent(WorkInfo.class);

                saveUserWorkPhotoListRequest(responseWorkInfo);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void saveUserWorkPhotoListRequest(WorkInfo responseWorkInfo) {
        List<WorkPhoto> workPhotoList = new ArrayList<>();

        for (int i = 0; i < SelectImageUtil.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(i);
            WorkPhoto workPhoto = new WorkPhoto();
            workPhoto.setPhoto1ID(mdImage.getPhotoID()); // 作者上传的图片
            workPhoto.setPhoto1SID(mdImage.getPhotoSID());
            workPhoto.setPhoto2ID(mdImage.getPhotoID()); // 合成图大图ID
            workPhoto.setPhoto2SID(mdImage.getPhotoSID()); // 合成缩略图ID
            int index = i + 1;
            workPhoto.setPhotoIndex(index);
            workPhoto.setWorkID(responseWorkInfo.getWorkID());
            workPhoto.setZoomSize(100);

            workPhotoList.add(workPhoto);
        }

        GlobalRestful.getInstance().SaveUserWorkPhotoList(workPhotoList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                saveOrderRequest();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void saveOrderRequest() {
        GlobalRestful.getInstance().SaveOrder(0, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                int orderID = 0;

                OrderInfo orderInfo = response.body().getContent(OrderInfo.class);
                orderID = orderInfo.getOrderID();

                saveOrderWorkRequest(orderID);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void saveOrderWorkRequest(int orderID) {
        OrderWork orderWork = new OrderWork();
        orderWork.setCreateTime(DateUtils.getServerDateStringByDate(new Date()));
        orderWork.setOrderCount(1);
        orderWork.setOrderID(orderID);
        orderWork.setPhotoCount(1);
        orderWork.setPhotoCover(SelectImageUtil.mAlreadySelectImage.get(0).getPhotoSID()); //封面，第一张照片的缩略图ID
        orderWork.setPrice(MDGroundApplication.mChoosedMeasurement.getPrice());
        orderWork.setTypeID(MDGroundApplication.mChoosedMeasurement.getTypeID()); //作品类型（getPhotoType接口返回的TypeID）
        orderWork.setTypeName(ProductType.getProductName(MDGroundApplication.mChoosedProductType)); //Title（getPhotoType接口返回的Title）

        GlobalRestful.getInstance().SaveOrderWork(orderWork, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                OrderWork responseOrderWork = response.body().getContent(OrderWork.class);

                saveOrderPhotoListAction(responseOrderWork);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void saveOrderPhotoListAction(final OrderWork orderWork) {
        List<OrderWorkPhoto> orderWorkPhotoList = new ArrayList<>();

        for (int i = 0; i < SelectImageUtil.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(i);
            OrderWorkPhoto orderWorkPhoto = new OrderWorkPhoto();
            orderWorkPhoto.setPhoto1ID(mdImage.getPhotoID());
            orderWorkPhoto.setPhoto1SID(mdImage.getPhotoSID());
            int index = i + 1;
            orderWorkPhoto.setPhotoIndex(index);
            orderWorkPhoto.setWorkOID(orderWork.getWorkOID());

            orderWorkPhotoList.add(orderWorkPhoto);
        }

        GlobalRestful.getInstance().SaveOrderPhotoList(orderWorkPhotoList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                NavUtils.toPaymentPreviewActivity(MagicCupPhotoEditActivity.this, orderWork);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }
    //endregion

}
