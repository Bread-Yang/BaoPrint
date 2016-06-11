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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", loginID);
            obj.put("Pwd", pwd);
            obj.put("Device", new JSONObject(convertObjectToString(device)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("LoginUser", obj.toString(), callback);
    }

    public void LoginUserByThirdParty(ThirdPartyLoginType loginType, String openID,
                                      String PhotoUrl, String UserNickName,
                                      String UserName, Callback<ResponseData> callback) {
        Device device = DeviceUtil.getDeviceInfo(MDGroundApplication.mInstance);
        device.setDeviceToken("abc");   // 信鸽的token, XGPushConfig.getToken(this);
        device.setDeviceID(DeviceUtil.getDeviceId());

        JSONObject obj = new JSONObject();
        try {
            switch (loginType) {
                case Wechat:
                    obj.put("WXOpenID", openID);
                    break;
                case QQ:
                    obj.put("QQOpenID", openID);
                    break;
                case Weibo:
                    obj.put("WBUID", openID);
                    break;
            }
            obj.put("PhotoUrl", PhotoUrl);
            obj.put("UserNickName", UserNickName);
            obj.put("UserName", UserName);
            obj.put("Device", new JSONObject(convertObjectToString(device)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("LoginUserByThirdParty", obj.toString(), callback);
    }

    public void CheckUserPhone(String phone, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("CheckUserPhone", obj.toString(), callback);
    }

    // 用户注册
    public void RegisterUser(User user, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Phone", user.getPhone());
            obj.put("Pwd", user.getPassword());
            obj.put("UserName", user.getUserName());
            obj.put("ChildDOB", user.getChildDOB());
            obj.put("ChildName", user.getChildName());
            obj.put("ChildSchool", user.getChildSchool());
            obj.put("ChildClass", user.getChildClass());
            obj.put("InvitationCode", user.getInvitationCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("RegisterUser", obj.toString(), callback);
    }

    // 找回密码
    public void ChangeUserPassword(String phone, String password, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Phone", phone);
            obj.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("ChangeUserPassword", obj.toString(), callback);
    }

    // 获取云相册统计接口
    public void GetCloudPhotoCount(Callback<ResponseData> callback) {
        asynchronousPost("GetCloudPhotoCount", null, callback);
    }

    // 获取云相册所有图片
    public void GetCloudPhoto(int PageIndex, boolean IsShared, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("PageIndex", PageIndex);
            obj.put("IsShared", IsShared);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetCloudPhoto", obj.toString(), callback);
    }

    // 删除个人云相册接口
    public void DeleteCloudPhoto(ArrayList<Integer> AutoIDList, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(AutoIDList);
            JSONArray array = new JSONArray(jsonString);
            obj.put("AutoIDList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("DeleteCloudPhoto", obj.toString(), callback);
    }

    //转存到个人相册
    public void TransferCloudPhoto(boolean IsShared, ArrayList<Integer> AutoIDList, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(AutoIDList);
            JSONArray array = new JSONArray(jsonString);
            obj.put("IsShared", IsShared);
            obj.put("AutoIDList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("TransferCloudPhoto", obj.toString(), callback);
    }

    // 获取获取产品类型信息以及规格明细
    public void GetPhotoType(ProductType productType, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ProductType", productType.value());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetPhotoType", obj.toString(), callback);
    }

    // 获取首页轮播图片列表
    public void GetBannerPhotoList(Callback<ResponseData> callback) {
        asynchronousPost("GetBannerPhotoList", null, callback);
    }

    // 用于获取所有类型图片说明/Banner图/介绍页
    public void GetPhotoTypeExplainList(Callback<ResponseData> callback) {
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("TypeID", productType.value());
//            obj.put("ExplainType", photoExplainType.value());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        asynchronousPost("GetPhotoTypeExplainList", null, callback);
    }

    // 保存订单接口
    public void SaveOrder(int OrderID, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderID", OrderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveOrder", obj.toString(), callback);
    }

    public void SaveUserWork(WorkInfo workInfo, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(workInfo);
            JSONObject object = new JSONObject(jsonString);
            obj.put("WorkInfo", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveUserWork", obj.toString(), callback);
    }

    public void SaveUserWorkPhotoList(List<WorkPhoto> workPhotoList, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(workPhotoList);
            JSONArray array = new JSONArray(jsonString);
            obj.put("WorkPhotoList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveUserWorkPhotoList", obj.toString(), callback);
    }

    // 保存订单作品接口
    public void SaveOrderWork(OrderWork orderWork, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(orderWork);
            JSONObject object = new JSONObject(jsonString);
            obj.put("OrderWork", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveOrderWork", obj.toString(), callback);
    }

    // 保存作品与相片关系接口
    public void SaveOrderPhotoList(List<OrderWorkPhoto> OrderWorkPhotoList, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(OrderWorkPhotoList);
            JSONArray array = new JSONArray(jsonString);
            obj.put("OrderWorkPhotoList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveOrderPhotoList", obj.toString(), callback);
    }

    public void GetUserAddressList(Callback<ResponseData> callback) {
        asynchronousPost("GetUserAddressList", null, callback);
    }

    public void SaveUserAddress(DeliveryAddress deliveryAddress, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(deliveryAddress);
            JSONObject object = new JSONObject(jsonString);
            obj.put("UserAddress", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("SaveUserAddress", obj.toString(), callback);
    }

    public void GetPhotoTemplateList(int typeDescID, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("TypeDescID", typeDescID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetPhotoTemplateList", obj.toString(), callback);
    }

    public void GetPhotoTemplateListByType(ProductType productType, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ProductType", productType.value());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetPhotoTemplateListByType", obj.toString(), callback);
    }

    public void GetPhotoTemplateAttachList(int templateID, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("TemplateID", templateID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetPhotoTemplateAttachList", obj.toString(), callback);
    }

    public void GetUserOrderList(OrderStatus orderStatus, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderStatus", orderStatus.value());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("GetUserOrderList", obj.toString(), callback);
    }

    public void ActivatingCoupon(String activationCode, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ActivationCode", activationCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("ActivatingCoupon", obj.toString(), callback);
    }

    public void GetUserCouponList(Callback<ResponseData> callback) {
        asynchronousPost("GetUserCouponList", null, callback);
    }

    // 确认支付调用接口（返回微信prepayid 给sdk调用）//支付宝暂时没做，后续做的话会改动
    public void UpdateOrderPrepay(OrderInfo orderInfo, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(orderInfo);
            JSONObject object = new JSONObject(jsonString);
            obj.put("OrderInfo", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("UpdateOrderRefunding", obj.toString(), callback);
    }

    // 确认收货接口
    public void UpdateOrderFinished(int orderID, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderID", orderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("UpdateOrderFinished", obj.toString(), callback);
    }

    // 申请退款接口
    public void UpdateOrderRefunding(OrderInfo orderInfo, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            String jsonString = convertObjectToString(orderInfo);
            JSONObject object = new JSONObject(jsonString);
            obj.put("OrderInfo", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        asynchronousPost("UpdateOrderRefunding", obj.toString(), callback);
    }


    //获取用户积分查询接口
    public void GetUserIntegralInfo(Callback<ResponseData> callback) {
        asynchronousPost("GetUserIntegralInfo", null, callback);
    }
    //获取我的作品列表接口
    public void GetUserWorkList(Callback<ResponseData> callback){
        asynchronousPost("GetUserWorkList",null,callback);
    }
}
