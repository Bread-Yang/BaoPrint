package com.MDGround.HaiLanPrint.utils;

import android.app.Activity;
import android.content.Intent;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.activity.payment.PaymentSuccessActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.OrderStatus;
import com.MDGround.HaiLanPrint.enumobject.PayType;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.OrderWorkPhoto;
import com.MDGround.HaiLanPrint.models.WorkInfo;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.MDGround.HaiLanPrint.utils.SelectImageUtil.mAlreadySelectImage;

/**
 * Created by yoghourt on 6/12/16.
 */

public class OrderUtils {

    private Activity mActivity;

    public int mOrderCount;

    public int mPrice;

    public String mWorkMaterial = "";

    public OrderInfo mOrderInfo;

    public String workFormat = "";

    public String workStyle = "";

    public ArrayList<OrderWork> mOrderWorkArrayList = new ArrayList<>();

    public OrderUtils(Activity activity, int orderCount, int price, String workMaterial) {
        this.mActivity = activity;
        mOrderCount = orderCount;
        mPrice = price;
        if (workMaterial != null) {
            this.mWorkMaterial = workMaterial;
        }
    }

    private int getPrintPhotoOrEngravingOrderCount() {
        int count = 0;
        for (MDImage mdImage : mAlreadySelectImage) {
            count += mdImage.getPhotoCount();
        }
        return count;
    }

    public void uploadImageRequest(final int upload_image_index) {
        if (upload_image_index < SelectImageUtil.mAlreadySelectImage.size()) {
            final MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(upload_image_index);

            final int nextUploadIndex = upload_image_index + 1;

            if (mdImage.getImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getImageLocalPath())) { // 本地图片
                File file = new File(mdImage.getImageLocalPath());

                // 上传本地照片
                FileRestful.getInstance().UploadCloudPhoto(false, file, null, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        final MDImage responseImage = response.body().getContent(MDImage.class);
                        responseImage.setPhotoCount(mdImage.getPhotoCount());

                        if (mdImage.getSyntheticImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getSyntheticImageLocalPath())) { // 合成图片
                            File syntheticFile = new File(mdImage.getSyntheticImageLocalPath());

                            // 上传合成图片
                            FileRestful.getInstance().UploadCloudPhoto(false, syntheticFile, null, new Callback<ResponseData>() {
                                @Override
                                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                    MDImage responseSyntheticImage = response.body().getContent(MDImage.class);

                                    responseImage.setSyntheticPhotoID(responseSyntheticImage.getPhotoID());
                                    responseImage.setSyntheticPhotoSID(responseSyntheticImage.getPhotoSID());

                                    SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
                                    uploadImageRequest(nextUploadIndex);
                                }

                                @Override
                                public void onFailure(Call<ResponseData> call, Throwable t) {

                                }
                            });
                        } else {
                            SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
                            uploadImageRequest(nextUploadIndex);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            } else {
                uploadImageRequest(nextUploadIndex);
            }
        } else {
            // 全部图片上传完之后,生成订单
            saveOrderRequest();
        }
    }

    public void saveUserWorkReqeust() {
        WorkInfo workInfo = new WorkInfo();
        workInfo.setCreatedTime(DateUtils.getServerDateStringByDate(new Date()));
        workInfo.setPhotoCount(SelectImageUtil.mAlreadySelectImage.size());
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

    public void saveOrderRequest() {
        GlobalRestful.getInstance().SaveOrder(MDGroundApplication.mChoosedProductType, new Callback<ResponseData>() {
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
        if (MDGroundApplication.mChoosedProductType == ProductType.PrintPhoto
                || MDGroundApplication.mChoosedProductType == ProductType.Engraving) {
            orderWork.setPhotoCount(getPrintPhotoOrEngravingOrderCount());
        } else {
            orderWork.setPhotoCount(SelectImageUtil.mAlreadySelectImage.size());
        }
        orderWork.setPhotoCover(SelectImageUtil.mAlreadySelectImage.get(0).getPhotoSID()); //封面，第一张照片的缩略图ID
        orderWork.setPrice(mPrice);
        orderWork.setTypeID(MDGroundApplication.mChoosedProductType.value()); //作品类型（getPhotoType接口返回的TypeID）
        orderWork.setTypeName(ProductType.getProductName(MDGroundApplication.mChoosedProductType)); //Title（getPhotoType接口返回的Title）
        orderWork.setWorkFormat(workFormat);
        orderWork.setWorkMaterial(mWorkMaterial);
        orderWork.setWorkStyle(workStyle);

        return orderWork;
    }

    public void saveOrderWorkRequest(final int saveIndex) {
        GlobalRestful.getInstance().SaveOrderWork(mOrderWorkArrayList.get(saveIndex), new Callback<ResponseData>() {
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

        for (int i = 0; i < SelectImageUtil.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(i);
            OrderWorkPhoto orderWorkPhoto = new OrderWorkPhoto();
            orderWorkPhoto.setAutoID(mdImage.getAutoID());
            orderWorkPhoto.setWorkOID(orderWork.getWorkOID());
            orderWorkPhoto.setPhoto1ID(mdImage.getPhotoID());
            orderWorkPhoto.setPhoto1SID(mdImage.getPhotoSID());
            int index = i + 1;
            orderWorkPhoto.setPhotoIndex(index);

            orderWorkPhotoList.add(orderWorkPhoto);
        }

        GlobalRestful.getInstance().SaveOrderPhotoList(orderWorkPhotoList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                NavUtils.toPaymentPreviewActivity(mActivity, orderWork);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    public void updateOrderPrepayRequest(final Activity activity, DeliveryAddress deliveryAddress, PayType payType, int amountFee, int receivableFee) {
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
