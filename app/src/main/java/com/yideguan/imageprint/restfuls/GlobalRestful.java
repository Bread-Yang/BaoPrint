package com.yideguan.imageprint.restfuls;

import com.google.gson.Gson;
import com.yideguan.imageprint.enumobject.restfuls.BusinessType;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.restfuls.bean.Device;
import com.yideguan.imageprint.restfuls.bean.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Callback;

/**
 * Created by yoghourt on 5/6/16.
 */
public class GlobalRestful extends BaseRestful {

    private static GlobalRestful mIntance = new GlobalRestful();

    @Override
    protected int getBusinessCode() {
        return BusinessType.Global.getType();
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
    public void LoginUser(String loginID, String pwd, Device device, Callback<ResponseData> callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", loginID);
            obj.put("Pwd", pwd);
            obj.put("Device", new JSONObject(new Gson().toJson(device)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        initRequestDateAndPost("LoginUser", obj.toString(), callback);
    }

    // 用户注册
    public void RegisterUser(User user, Callback<ResponseData> callback) {
        initRequestDateAndPost("RegisterUser", new Gson().toJson(user), callback);
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

        initRequestDateAndPost("ChangeUserPassword", obj.toString(), callback);
    }
}
