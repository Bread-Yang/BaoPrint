package com.MDGround.HaiLanPrint.restfuls;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.OrderStatus;
import com.MDGround.HaiLanPrint.enumobject.ThirdPartyLoginType;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.OrderWorkPhoto;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.models.WorkInfo;
import com.MDGround.HaiLanPrint.models.WorkPhoto;
import com.MDGround.HaiLanPrint.restfuls.bean.Device;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by yoghourt on 5/6/16.
 */
public class GlobalRestful extends BaseRestful {

    private static GlobalRestful mIntance = new GlobalRestful();

    @Override
    protected BusinessType getBusinessType() {
        return BusinessType.Global;
    }

    @Override
    protected String getHost() {
        return Constants.HOST;
    }


    private GlobalRestful() {

    }

    public static GlobalRestful getInstance() {
        if (mIntance == null) {
            mIntance = new GlobalRestful();
        }
        return mIntance;
    }

    // 用户登录
    public void LoginUser(String loginID, String pwd, Callback<ResponseData> callback) {
        Device device = DeviceUtil.getDeviceInfo(MDGroundApplication.mInstance);
        device.setDeviceToken("abc");   // 信鸽的token, XGPushConfig.getToken(this);
        device.setDeviceID(DeviceUtil.getDeviceId());

        JsonObject obj = new JsonObject();
        obj.addProperty("LoginID", loginID);
        obj.addProperty("Pwd", pwd);
        obj.add("Device", new Gson().toJsonTree(device));

        asynchronousPost("LoginUser", obj, callback);
    }

    public void LoginUserByThirdParty(ThirdPartyLoginType loginType, String openID,
                                      String PhotoUrl, String UserNickName,
                                      String UserName, Callback<ResponseData> callback) {
        Device device = DeviceUtil.getDeviceInfo(MDGroundApplication.mInstance);
        device.setDeviceToken("abc");   // 信鸽的token, XGPushConfig.getToken(this);
        device.setDeviceID(DeviceUtil.getDeviceId());

        JsonObject obj = new JsonObject();
        switch (loginType) {
            case Wechat:
                obj.addProperty("WXOpenID", openID);
                break;
            case QQ:
                obj.addProperty("QQOpenID", openID);
                break;
            case Weibo:
                obj.addProperty("WBUID", openID);
                break;
        }
        obj.addProperty("PhotoUrl", PhotoUrl);
        obj.addProperty("UserNickName", UserNickName);
        obj.addProperty("UserName", UserName);
        obj.add("Device", new Gson().toJsonTree(device));

        asynchronousPost("LoginUserByThirdParty", obj, callback);
    }

    public void CheckUserPhone(String phone, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Phone", phone);

        asynchronousPost("CheckUserPhone", obj, callback);
    }

    // 用户注册
    public void RegisterUser(User user, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Phone", user.getPhone());
        obj.addProperty("Pwd", user.getPassword());
        obj.addProperty("UserName", user.getUserName());
        obj.addProperty("ChildDOB", user.getChildDOB());
        obj.addProperty("ChildName", user.getChildName());
        obj.addProperty("ChildSchool", user.getChildSchool());
        obj.addProperty("ChildClass", user.getChildClass());
        obj.addProperty("InvitationCode", user.getInvitationCode());

        asynchronousPost("RegisterUser", obj, callback);
    }

    // 找回密码
    public void ChangeUserPassword(String phone, String password, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Phone", phone);
        obj.addProperty("Password", password);

        asynchronousPost("ChangeUserPassword", obj, callback);
    }

    // 获取云相册统计接口
    public void GetCloudPhotoCount(Callback<ResponseData> callback) {
        asynchronousPost("GetCloudPhotoCount", null, callback);
    }

    // 获取云相册所有图片
    public void GetCloudPhoto(int PageIndex, boolean IsShared, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("PageIndex", PageIndex);
        obj.addProperty("IsShared", IsShared);

        asynchronousPost("GetCloudPhoto", obj, callback);
    }

    // 删除个人云相册接口
    public void DeleteCloudPhoto(ArrayList<Integer> AutoIDList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("AutoIDList", new Gson().toJsonTree(AutoIDList));

        asynchronousPost("DeleteCloudPhoto", obj, callback);
    }

    //转存到个人相册
    public void TransferCloudPhoto(boolean IsShared, ArrayList<Integer> AutoIDList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("IsShared", IsShared);
        obj.add("AutoIDList", new Gson().toJsonTree(AutoIDList));

        asynchronousPost("TransferCloudPhoto", obj, callback);
    }

    // 获取获取产品类型信息以及规格明细
    public void GetPhotoType(ProductType productType, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("ProductType", productType.value());

        asynchronousPost("GetPhotoType", obj, callback);
    }

    // 获取首页轮播图片列表
    public void GetBannerPhotoList(Callback<ResponseData> callback) {
        asynchronousPost("GetBannerPhotoList", null, callback);
    }

    // 用于获取所有类型图片说明/Banner图/介绍页
    public void GetPhotoTypeExplainList(Callback<ResponseData> callback) {
        asynchronousPost("GetPhotoTypeExplainList", null, callback);
    }

    // 保存订单接口
    public void SaveOrder(ProductType productType, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("TypeID", productType.value());

        asynchronousPost("SaveOrder", obj, callback);
    }

    public void SaveUserWork(WorkInfo workInfo, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("WorkInfo", new Gson().toJsonTree(workInfo));

        asynchronousPost("SaveUserWork", obj, callback);
    }

    public void SaveUserWorkPhotoList(List<WorkPhoto> workPhotoList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("WorkPhotoList", new Gson().toJsonTree(workPhotoList));

        asynchronousPost("SaveUserWorkPhotoList", obj, callback);
    }

    // 保存订单作品接口
    public void SaveOrderWork(OrderWork orderWork, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("OrderWork", new Gson().toJsonTree(orderWork));

        asynchronousPost("SaveOrderWork", obj, callback);
    }

    // 保存作品与相片关系接口
    public void SaveOrderPhotoList(List<OrderWorkPhoto> OrderWorkPhotoList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("OrderWorkPhotoList", new Gson().toJsonTree(OrderWorkPhotoList));

        asynchronousPost("SaveOrderPhotoList", obj, callback);
    }

    public void GetUserAddressList(Callback<ResponseData> callback) {
        asynchronousPost("GetUserAddressList", null, callback);
    }

    public void SaveUserAddress(DeliveryAddress deliveryAddress, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("UserAddress", new Gson().toJsonTree(deliveryAddress));

        asynchronousPost("SaveUserAddress", obj, callback);
    }

    public void GetPhotoTemplateList(int typeDescID, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("TypeDescID", typeDescID);

        asynchronousPost("GetPhotoTemplateList", obj, callback);
    }

    public void GetPhotoTemplateListByType(ProductType productType, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("ProductType", productType.value());

        asynchronousPost("GetPhotoTemplateListByType", obj, callback);
    }

    public void GetPhotoTemplateAttachList(int templateID, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("TemplateID", templateID);

        asynchronousPost("GetPhotoTemplateAttachList", obj, callback);
    }

    public void GetUserOrderList(OrderStatus orderStatus, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("OrderStatus", orderStatus.value());

        asynchronousPost("GetUserOrderList", obj, callback);
    }

    public void ActivatingCoupon(String activationCode, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("ActivationCode", activationCode);

        asynchronousPost("ActivatingCoupon", obj, callback);
    }

    public void GetUserCouponList(Callback<ResponseData> callback) {
        asynchronousPost("GetUserCouponList", null, callback);
    }

    public void GetSystemSetting(Callback<ResponseData> callback) {
        asynchronousPost("GetSystemSetting", null, callback);
    }

    // 确认支付调用接口（返回微信prepayid 给sdk调用）//支付宝暂时没做，后续做的话会改动
    public void UpdateOrderPrepay(OrderInfo orderInfo, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("OrderInfo", new Gson().toJsonTree(orderInfo));

        asynchronousPost("UpdateOrderPrepay", obj, callback);
    }

    // 确认收货接口
    public void UpdateOrderFinished(int orderID, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("OrderID", orderID);

        asynchronousPost("UpdateOrderFinished", obj, callback);
    }

    // 申请退款接口
    public void UpdateOrderRefunding(OrderInfo orderInfo, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("OrderInfo", new Gson().toJsonTree(orderInfo));

        asynchronousPost("UpdateOrderRefunding", obj, callback);
    }


    //获取用户积分查询接口
    public void GetUserIntegralInfo(Callback<ResponseData> callback) {
        asynchronousPost("GetUserIntegralInfo", null, callback);
    }

    //获取我的作品列表接口
    public void GetUserWorkList(Callback<ResponseData> callback) {
        asynchronousPost("GetUserWorkList", null, callback);
    }

    //修改用户信息
    public void SaveUserInfo(User user, Callback<ResponseData> callback) {
        JsonObject object = new JsonObject();
        object.add("UserInfo", new Gson().toJsonTree(user));

        asynchronousPost("SaveUserInfo", object, callback);
    }

    //删除收货地址
    public void DeleteUserAddress(int AutoID, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("AutoID", AutoID);
        asynchronousPost("DeleteUserAddress", obj, callback);
    }

    //意见反馈接口
    public void SaveUserSuggestion(String Phone, String Suggestion, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Phone", Phone);
        obj.addProperty("Suggestion", Suggestion);
        asynchronousPost("SaveUserSuggestion", obj, callback);
    }

    // 删除我的作品
    public void DeleteUserWork(List<Integer> WorkIDList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("WorkIDList", new Gson().toJsonTree(WorkIDList));
        asynchronousPost("DeleteUserWork", obj, callback);
    }

    // 购买我的作品
    public void SaveOrderByWork(List<Integer> workIDList, Callback<ResponseData> callback) {
        JsonObject obj = new JsonObject();
        obj.add("WorkIDList", new Gson().toJsonTree(workIDList));
        asynchronousPost("SaveOrderByWork", obj, callback);
    }
}
