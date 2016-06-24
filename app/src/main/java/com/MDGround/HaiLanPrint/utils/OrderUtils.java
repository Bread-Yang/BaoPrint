package com.MDGround.HaiLanPrint.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.activity.payment.PaymentSuccessActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.OrderStatus;
import com.MDGround.HaiLanPrint.enumobject.PayType;
import com.MDGround.HaiLanPrint.enumobject.UploadType;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.OrderWorkPhoto;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.models.WorkInfo;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 6/12/16.
 */
public class OrderUtils {

    private Activity mActivity;

    public int mOrderCount;

    public int mPrice;

    public OrderInfo mOrderInfo;

    public String mWorkFormat = "";

    public String mWorkMaterial = "";

    public String mWorkStyle = "";

    public ArrayList<OrderWork> mOrderWorkArrayList = new ArrayList<>();

    public OrderUtils(OrderInfo orderInfo, ArrayList<OrderWork> orderWorkArrayList) {
        mOrderInfo = orderInfo;
        mOrderWorkArrayList = orderWorkArrayList;
    }

    public OrderUtils(Activity activity, int orderCount, int price) {
        this.mActivity = activity;
        mOrderCount = orderCount;
        mPrice = price;
    }

    public OrderUtils(Activity activity, int orderCount, int price,
                      String workFormat, String workMaterial, String workStyle) {
        this.mActivity = activity;
        mOrderCount = orderCount;
        mPrice = price;

        if (workFormat != null) {
            this.mWorkFormat = workFormat;
        }

        if (workMaterial != null) {
            this.mWorkMaterial = workMaterial;
        }

        if (workStyle != null) {
            this.mWorkStyle = workStyle;
        }
    }

    // 生成合成图片到本地
    public void createSyntheticImage(final Context context) {
        for (int i = 0; i < SelectImageUtils.mAlreadySelectImage.size(); i++) {
            final MDImage selectImage = SelectImageUtils.mAlreadySelectImage.get(i);
            MDImage templateImage = SelectImageUtils.mTemplateImage.get(i);
            if (templateImage.getPhotoSID() != 0
                    && (selectImage.getImageLocalPath() != null || selectImage.getPhotoSID() != 0)) { //
                // 模版图片存在,并且用户选择的图片存在

            }
        }
        for (MDImage mdImage : SelectImageUtils.mAlreadySelectImage) {

        }
    }

    private Runnable uploadSyntheticImage = new Runnable() {
        @Override
        public void run() {

        }
    };

//    public void uploadImageRequest(final int upload_image_index) {
//        if (upload_image_index < SelectImageUtil.mAlreadySelectImage.size()) {
//            final MDImage selectImage = SelectImageUtil.mAlreadySelectImage.get(upload_image_index);
//
//            final int nextUploadIndex = upload_image_index + 1;
//
//            if (selectImage.getImageLocalPath() != null && !StringUtil.isEmpty(selectImage
// .getImageLocalPath())) { // 本地图片
//                File file = new File(selectImage.getImageLocalPath());
//
//                // 上传本地照片
//                FileRestful.getInstance().UploadPhoto(UploadType.Order, file, new Callback<ResponseData>() {
//                    @Override
//                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                        final MDImage responseImage = response.body().getContent(MDImage.class);
//
//                        if (responseImage != null) {
//                            responseImage.setPhotoCount(selectImage.getPhotoCount());
//
//                            if (selectImage.getSyntheticImageLocalPath() != null && !StringUtil.isEmpty
// (selectImage.getSyntheticImageLocalPath())) { // 合成图片
//                                File syntheticFile = new File(selectImage.getSyntheticImageLocalPath());
//
//                                // 上传合成图片
//                                FileRestful.getInstance().UploadCloudPhoto(false, syntheticFile, null,
// new Callback<ResponseData>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseData> call,
// Response<ResponseData> response) {
//                                        MDImage responseSyntheticImage = response.body().getContent
// (MDImage.class);
//
//                                        SelectImageUtil.mAlreadySelectImage.set(upload_image_index,
// responseImage);
//                                        uploadImageRequest(nextUploadIndex);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseData> call, Throwable t) {
//
//                                    }
//                                });
//                            } else {
//                                SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
//                                uploadImageRequest(nextUploadIndex);
//                            }
//                        } else {
//                            uploadImageRequest(nextUploadIndex);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseData> call, Throwable t) {
//                    }
//                });
//            } else {
//                uploadImageRequest(nextUploadIndex);
//            }
//        } else {
//            // 全部图片上传完之后,生成订单
//            saveOrderRequest();
//        }
//    }

    private GPUImageTransformFilter getTransformFilter(float scaleFactor, float rotationDegree) {
        float[] transform = new float[16];
        Matrix.setIdentityM(transform, 0);
        Matrix.setRotateM(transform, 0, rotationDegree, 0, 0, 1.0f);
        scaleFactor = scaleFactor;
        if (scaleFactor < 0) {
            scaleFactor = 1;
        }
        Matrix.scaleM(transform, 0, scaleFactor, scaleFactor, 1.0f);

        GPUImageTransformFilter transformFilter = new GPUImageTransformFilter();
        transformFilter.setTransform3D(transform);
        return transformFilter;
    }

    public void uploadImageRequest(final Context context, final int upload_image_index) {
        if (upload_image_index < SelectImageUtils.mAlreadySelectImage.size()) {
            final MDImage selectImage = SelectImageUtils.mAlreadySelectImage.get(upload_image_index);

            MDImage tempTemplateImage = null;
            if (upload_image_index < SelectImageUtils.mTemplateImage.size()) {
                tempTemplateImage = SelectImageUtils.mTemplateImage.get(upload_image_index);
            } else {
                tempTemplateImage = new MDImage();
            }
            final MDImage templateImage = tempTemplateImage;

            final int nextUploadIndex = upload_image_index + 1;

            // 本地图片
            if (selectImage.getImageLocalPath() != null && !StringUtil.isEmpty(selectImage
                    .getImageLocalPath())) {
                File file = new File(selectImage.getImageLocalPath());

                // 上传本地照片
                FileRestful.getInstance().UploadPhoto(UploadType.Order, file, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        KLog.e("上传本地照片成功");
                        final MDImage responseImage = response.body().getContent(MDImage.class);

                        if (responseImage != null) {
                            // 上传本地图片成功, 设置对应的PhotoID, PhotoSID
                            responseImage.setPhotoCount(selectImage.getPhotoCount());

                            final WorkPhoto workPhoto = selectImage.getWorkPhoto();
                            if (workPhoto != null) {
                                workPhoto.setPhoto1ID(responseImage.getPhotoID());
                                workPhoto.setPhoto1SID(responseImage.getPhotoSID());
                                responseImage.setWorkPhoto(workPhoto);
                            }

                            SelectImageUtils.mAlreadySelectImage.set(upload_image_index, responseImage);

                            // 如果有模板, 把模板和选择的照片合成一张图片
                            if (templateImage.getPhotoSID() != 0) {
                                GlideUtil.loadImageAsBitmap(templateImage, new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(final Bitmap templateBitmap,
                                                                GlideAnimation<? super Bitmap>
                                                                        glideAnimation) {
                                        KLog.e("加载模板bitmap成功");

                                        GlideUtil.loadImageAsBitmap(selectImage, new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap selectBitmap,
                                                                        GlideAnimation<? super Bitmap>
                                                                                glideAnimation) {
                                                KLog.e("加载本地bitmap成功");

                                                GPUImageTransformFilter transformFilter = getTransformFilter(workPhoto.getZoomSize() / 100f,
                                                        workPhoto.getRotate());         //  放大缩小旋转
                                                GPUImageBrightnessFilter brightnessFilter = new GPUImageBrightnessFilter();
                                                brightnessFilter.setBrightness(workPhoto.getBrightLevel() / 100f); // 亮度

                                                GPUImageNormalBlendFilter blendFilter = new GPUImageNormalBlendFilter();
                                                blendFilter.setBitmap(templateBitmap);

                                                GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup();
                                                gpuImageFilterGroup.addFilter(brightnessFilter);
                                                gpuImageFilterGroup.addFilter(transformFilter);
                                                gpuImageFilterGroup.addFilter(blendFilter);

                                                GPUImage blendImage = new GPUImage(context);
                                                blendImage.setImage(selectBitmap);
                                                blendImage.setFilter(gpuImageFilterGroup);

                                                Bitmap blendBitmap = blendImage.getBitmapWithFilterApplied();

                                                // 上传合成图片
                                                FileRestful.getInstance().UploadPhoto(UploadType.Order,
                                                        blendBitmap, new Callback<ResponseData>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseData> call,
                                                                                   Response<ResponseData> response) {
                                                                KLog.e("上传本地和模板合成照片成功");
                                                                // 上传合成图片成功, 设置对应的PhotoID, PhotoSID
                                                                MDImage responseSyntheticImage = response.body()
                                                                        .getContent(MDImage.class);

                                                                responseImage.getWorkPhoto().setPhoto2ID
                                                                        (responseSyntheticImage.getPhotoID());
                                                                responseImage.getWorkPhoto().setPhoto2SID
                                                                        (responseSyntheticImage.getPhotoSID());

                                                                // 继续上传
                                                                SelectImageUtils.mAlreadySelectImage.set
                                                                        (upload_image_index, responseImage);
                                                                uploadImageRequest(context, nextUploadIndex);
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseData> call,
                                                                                  Throwable t) {

                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                            } else {
                                uploadImageRequest(context, nextUploadIndex);
                            }
                        } else {
                            // 继续上传
                            uploadImageRequest(context, nextUploadIndex);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            } else {
                // 网络图片
                // 如果有模板, 把模板和选择的照片合成一张图片
                if (selectImage.getPhotoSID() != 0 && templateImage.getPhotoSID() != 0) {
                    GlideUtil.loadImageAsBitmap(templateImage, new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap templateBitmap, GlideAnimation<? super
                                Bitmap> glideAnimation) {

                            GlideUtil.loadImageAsBitmap(selectImage, new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap selectBitmap, GlideAnimation<? super
                                        Bitmap> glideAnimation) {
                                    KLog.e("加载网络bitmap成功");
                                    WorkPhoto workPhoto = selectImage.getWorkPhoto();

                                    GPUImageTransformFilter transformFilter = getTransformFilter(workPhoto.getZoomSize() / 100f,
                                            workPhoto.getRotate());         //  放大缩小旋转
                                    GPUImageBrightnessFilter brightnessFilter = new GPUImageBrightnessFilter();
                                    brightnessFilter.setBrightness(workPhoto.getBrightLevel() / 100f); // 亮度

                                    GPUImageNormalBlendFilter blendFilter = new GPUImageNormalBlendFilter();
                                    blendFilter.setBitmap(templateBitmap);

                                    GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup();
                                    gpuImageFilterGroup.addFilter(brightnessFilter);
                                    gpuImageFilterGroup.addFilter(transformFilter);
                                    gpuImageFilterGroup.addFilter(blendFilter);

                                    GPUImage blendImage = new GPUImage(context);
                                    blendImage.setImage(selectBitmap);
                                    blendImage.setFilter(gpuImageFilterGroup);

                                    Bitmap blendBitmap = blendImage.getBitmapWithFilterApplied();

                                    // 上传合成图片
                                    FileRestful.getInstance().UploadPhoto(UploadType.Order, blendBitmap,
                                            new Callback<ResponseData>() {
                                                @Override
                                                public void onResponse(Call<ResponseData> call,
                                                                       Response<ResponseData> response) {
                                                    KLog.e("上传网络和模板合成照片成功");
                                                    // 上传合成图片成功, 设置对应的PhotoID, PhotoSID
                                                    MDImage responseSyntheticImage = response.body().getContent
                                                            (MDImage.class);

                                                    selectImage.getWorkPhoto().setPhoto2ID(responseSyntheticImage.getPhotoID());
                                                    selectImage.getWorkPhoto().setPhoto2SID(responseSyntheticImage.getPhotoSID());
                                                    // 继续上传
                                                    uploadImageRequest(context, nextUploadIndex);
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseData> call, Throwable t) {

                                                }
                                            });
                                }
                            });
                        }
                    });
                } else {
                    // 继续上传
                    uploadImageRequest(context, nextUploadIndex);
                }
            }
        } else {
            // 全部图片上传完之后,生成订单
            switch (MDGroundApplication.mInstance.getChoosedProductType()) {
                case PrintPhoto:
                case Engraving:
                    saveOrderRequest();
                    break;
                default:
                    saveUserWorkReqeust();
                    break;
            }
        }
    }

    public void saveUserWorkReqeust() {
        WorkInfo workInfo = new WorkInfo();
        workInfo.setCreatedTime(DateUtils.getServerDateStringByDate(new Date()));
        workInfo.setPhotoCount(SelectImageUtils.mAlreadySelectImage.size());
        workInfo.setPrice(mPrice);
        workInfo.setTypeID(MDGroundApplication.mInstance.getChoosedProductType().value());
        workInfo.setTemplateID(MDGroundApplication.mInstance.getChoosedTemplate().getTemplateID());
        workInfo.setTypeName(ProductType.getProductName(MDGroundApplication.mInstance.getChoosedProductType()));
        workInfo.setUserID(MDGroundApplication.mInstance.getLoginUser().getUserID());
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

        for (int i = 0; i < SelectImageUtils.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtils.mAlreadySelectImage.get(i);
            WorkPhoto workPhoto = mdImage.getWorkPhoto();
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

    public void saveOrderRequest() {
        GlobalRestful.getInstance().SaveOrder(MDGroundApplication.mInstance.getChoosedProductType(), new
                Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                        mOrderInfo = response.body().getContent(OrderInfo.class);

                        OrderWork orderWork = createOrderWork(mOrderInfo);
                        mOrderWorkArrayList.add(orderWork);
                        saveOrderWorkRequest(0);
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        ViewUtils.dismiss();
                    }
                });
    }

    private OrderWork createOrderWork(OrderInfo orderInfo) {
        OrderWork orderWork = new OrderWork();
        orderWork.setCreateTime(DateUtils.getServerDateStringByDate(new Date()));
        orderWork.setOrderCount(mOrderCount);
        orderWork.setOrderID(orderInfo.getOrderID());
        if (MDGroundApplication.mInstance.getChoosedProductType() == ProductType.PrintPhoto
                || MDGroundApplication.mInstance.getChoosedProductType() == ProductType.Engraving) {
            orderWork.setPhotoCount(SelectImageUtils.getPrintPhotoOrEngravingOrderCount());
            orderWork.setPhotoCover(SelectImageUtils.mAlreadySelectImage.get(0).getPhotoSID()); //封面，第一张照片的缩略图ID
        } else {
            orderWork.setPhotoCount(SelectImageUtils.mAlreadySelectImage.size());
            orderWork.setPhotoCover(SelectImageUtils.mAlreadySelectImage.get(0).getWorkPhoto().getPhoto2SID()); //封面，第一张照片的合成照片
        }

        orderWork.setPrice(mPrice);
        orderWork.setTypeID(MDGroundApplication.mInstance.getChoosedProductType().value()); //作品类型（getPhotoType接口返回的TypeID）
        orderWork.setTypeName(ProductType.getProductName(MDGroundApplication.mInstance.getChoosedProductType()));
        Measurement measurement = MDGroundApplication.mInstance.getChoosedMeasurement();
        if (measurement != null) {
            orderWork.setTypeTitle(measurement.getTitle());
        }
        Template template = MDGroundApplication.mInstance.getChoosedTemplate();
        if (template != null) {
            orderWork.setTemplateID(template.getTemplateID());
            orderWork.setTemplateName(template.getTemplateName());
            orderWork.setWorkMaterial(template.getMaterialTypeString());
        }
        //Title（getPhotoType接口返回的Title）
        orderWork.setWorkFormat(mWorkFormat);
        orderWork.setWorkMaterial(mWorkMaterial);
        orderWork.setWorkStyle(mWorkStyle);

        return orderWork;
    }

    public void saveOrderWorkRequest(final int saveIndex) {
        GlobalRestful.getInstance().SaveOrderWork(mOrderWorkArrayList.get(saveIndex), new
                Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        OrderWork responseOrderWork = response.body().getContent(OrderWork.class);
                        mOrderWorkArrayList.set(saveIndex, responseOrderWork);

                        saveOrderPhotoListRequest(responseOrderWork);
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        ViewUtils.dismiss();
                    }
                });
    }

    private void saveOrderPhotoListRequest(final OrderWork orderWork) {
        List<OrderWorkPhoto> orderWorkPhotoList = new ArrayList<>();

        for (int i = 0; i < SelectImageUtils.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtils.mAlreadySelectImage.get(i);
            WorkPhoto workPhoto = mdImage.getWorkPhoto();

            OrderWorkPhoto orderWorkPhoto = new OrderWorkPhoto();

            if (MDGroundApplication.mInstance.getChoosedProductType() == ProductType.PrintPhoto
                    || MDGroundApplication.mInstance.getChoosedProductType() == ProductType.Engraving) {
                orderWorkPhoto.setAutoID(mdImage.getAutoID());
                orderWorkPhoto.setWorkOID(orderWork.getWorkOID());
                orderWorkPhoto.setPhoto1ID(mdImage.getPhotoID());
                orderWorkPhoto.setPhoto1SID(mdImage.getPhotoSID());
                int index = i + 1;
                orderWorkPhoto.setPhotoIndex(index);
            } else {
                orderWorkPhoto.setPhoto1ID(workPhoto.getPhoto1ID());
                orderWorkPhoto.setPhoto1SID(workPhoto.getPhoto1SID());
                orderWorkPhoto.setPhoto2ID(workPhoto.getPhoto2ID());
                orderWorkPhoto.setPhoto2SID(workPhoto.getPhoto2SID());
                orderWorkPhoto.setPhotoCount(mdImage.getPhotoCount());
                orderWorkPhoto.setPhotoIndex(workPhoto.getPhotoIndex());
                orderWorkPhoto.setWorkOID(orderWork.getWorkOID());
            }

            orderWorkPhotoList.add(orderWorkPhoto);
        }

        GlobalRestful.getInstance().SaveOrderPhotoList(orderWorkPhotoList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                NavUtils.toPaymentPreviewActivity(mActivity);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    public void updateOrderPrepayRequest(final Activity activity, DeliveryAddress deliveryAddress, PayType
            payType, int amountFee, int receivableFee) {
        ViewUtils.loading(activity);
        mOrderInfo.setAddressID(deliveryAddress.getAutoID());
        mOrderInfo.setAddressReceipt(StringUtil.getCompleteAddress(deliveryAddress));
        mOrderInfo.setCreatedTime(DateUtils.getServerDateStringByDate(new Date()));
        mOrderInfo.setOrderStatus(OrderStatus.Paid.value());
        mOrderInfo.setPayType(payType.value());
        mOrderInfo.setPhone(deliveryAddress.getPhone());
        mOrderInfo.setReceiver(deliveryAddress.getReceiver());
        mOrderInfo.setTotalFee(amountFee);
        mOrderInfo.setTotalFeeReal(receivableFee);

        GlobalRestful.getInstance().UpdateOrderPrepay(mOrderInfo, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                Intent intent = new Intent(activity, PaymentSuccessActivity.class);
                intent.putExtra(Constants.KEY_ORDER_INFO, mOrderInfo);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
}
