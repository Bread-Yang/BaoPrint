package com.yideguan.imageprint.restfuls;

import android.content.Context;

import com.google.gson.Gson;
import com.yideguan.imageprint.application.YiDeGuanApplication;
import com.yideguan.imageprint.constants.Constants;
import com.yideguan.imageprint.enumobject.BusinessType;
import com.yideguan.imageprint.enumobject.PlatformType;
import com.yideguan.imageprint.enumobject.User;
import com.yideguan.imageprint.restfuls.Interceptor.DecryptedPayloadInterceptor;
import com.yideguan.imageprint.restfuls.bean.Device;
import com.yideguan.imageprint.restfuls.bean.RequestData;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.DeviceUtils;
import com.yideguan.imageprint.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yoghourt on 5/5/16.
 */
public class GlobalRestful {

    public static GlobalRestful INSTANCE = new GlobalRestful();

    private Context mContext;

    private GlobalService globalService;

    // 请求参数
    private RequestData requestData;

    // service接口
    public interface GlobalService {
        @POST("api/RpcService.ashx/")
        Call<ResponseData> requestSever(@Body RequestBody requestBody);
    }

    protected int getBusinessCode() {
        return BusinessType.Global.getType();
    }

    private int getPlatform() {
        boolean isPad = DeviceUtils.isPad(mContext);
        if (isPad) {
            return PlatformType.ANDROID_PAD.value();
        } else {
            return PlatformType.ANDROID_PHONE.value();
        }
    }

    private GlobalRestful() {
        mContext = YiDeGuanApplication.INSTANCE;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(new DecryptedPayloadInterceptor());  //请求前加密,返回前解密

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        globalService = retrofit.create(GlobalService.class);
    }

    private void initRequestDateAndPost(String queryData, Callback<ResponseData> callback) {
        requestData = new RequestData();

        requestData.setQueryData(queryData);

        requestData.setFunctionName("LoginUser");
        requestData.setCulture(DeviceUtils.getLanguage(mContext));
        requestData.setBusinessCode(getBusinessCode());
        requestData.setActionTimeSpan(System.currentTimeMillis() / 1000);
        requestData.setPlatform(getPlatform());

        String serviceToken = "";
        requestData.setDeviceID(DeviceUtils.getDeviceId());

        User user = YiDeGuanApplication.mLoginUser;
        if (user != null) {
            serviceToken = user.getServiceToken();
            requestData.setUserID(user.getUserID());
        }
        requestData.setServiceToken(serviceToken);
        requestData.setSign(appSign(requestData));

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Call<ResponseData> call = globalService.requestSever(requestBody);
        call.enqueue(callback);
    }

    protected String appSign(RequestData data) {
        Map<String, String> params = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }

            ;
        });

        params.put("Version", String.valueOf(data.getVersion()));
        params.put("Culture", data.getCulture());
        params.put("Platform", String.valueOf(data.getPlatform()));
        params.put("BusinessCode", String.valueOf(data.getBusinessCode()));
        params.put("FunctionName", data.getFunctionName());
        params.put("DeviceID", String.valueOf(data.getDeviceID()));
        params.put("UserID", String.valueOf(data.getUserID()));
        params.put("ServiceToken", data.getServiceToken());
        params.put("ActionTimeSpan", String.valueOf(data.getActionTimeSpan()));

        Set<String> keySet = params.keySet();
        Iterator<String> iter = keySet.iterator();

        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key);
            sb.append(params.get(key));
        }
        sb.append("@2O!5");

        return MD5Util.MD5(sb.toString());
    }


    public void LoginUser(String loginID, String pwd, Device device, Callback<ResponseData> callback) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", loginID);
            obj.put("Pwd", pwd);
            obj.put("Device", new Gson().toJson(device));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initRequestDateAndPost(obj.toString(), callback);
//        requestData.setFunctionName(getFunctionName());
    }

}
