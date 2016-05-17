package com.MDGround.HaiLanPrint.restfuls;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.google.gson.Gson;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.bean.Device;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            obj.put("Device", new JSONObject(new Gson().toJson(device)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        asynchronousPost("LoginUser", obj.toString(), callback);
    }

    public void CheckUserPhone(String phone, Callback<ResponseData> callback) {

    }

    // 用户注册
    public void RegisterUser(User user, Callback<ResponseData> callback) {
        asynchronousPost("RegisterUser", new Gson().toJson(user), callback);
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
            String jsonString = new Gson().toJson(AutoIDList);
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
            String jsonString = new Gson().toJson(AutoIDList);
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

}
