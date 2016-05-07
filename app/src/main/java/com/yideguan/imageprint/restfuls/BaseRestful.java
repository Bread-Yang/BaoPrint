package com.yideguan.imageprint.restfuls;

import android.content.Context;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.yideguan.imageprint.application.YiDeGuanApplication;
import com.yideguan.imageprint.constants.Constants;
import com.yideguan.imageprint.enumobject.restfuls.PlatformType;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.restfuls.Interceptor.DecryptedPayloadInterceptor;
import com.yideguan.imageprint.restfuls.bean.RequestData;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.DeviceUtil;
import com.yideguan.imageprint.utils.EncryptUtil;

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
public abstract class BaseRestful {

    private Context mContext;

    private BaseService baseService;

    // 请求参数
    private RequestData requestData;

    protected abstract int getBusinessCode();

    // service接口
    public interface BaseService {
        @POST("api/RpcService.ashx/")
        Call<ResponseData> requestSever(@Body RequestBody requestBody);
    }

    private int getPlatform() {
        boolean isPad = DeviceUtil.isPad(mContext);
        if (isPad) {
            return PlatformType.ANDROID_PAD.value();
        } else {
            return PlatformType.ANDROID_PHONE.value();
        }
    }

    protected BaseRestful() {
        mContext = YiDeGuanApplication.INSTANCE;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(new DecryptedPayloadInterceptor());  //请求前加密,返回前解密

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create())  // json转成对象
                .client(httpClient.build())
                .build();
        baseService = retrofit.create(BaseService.class);
    }

    protected void initRequestDateAndPost(String functionName, String queryData, Callback<ResponseData> callback) {
        KLog.e("请求接口 \"" + functionName + "\" 的json数据:");

        requestData = new RequestData();

        requestData.setQueryData(queryData);
        requestData.setFunctionName(functionName);
        requestData.setCulture(DeviceUtil.getLanguage(mContext));
        requestData.setBusinessCode(getBusinessCode());
        requestData.setActionTimeSpan(System.currentTimeMillis() / 1000);
        requestData.setPlatform(getPlatform());

        String serviceToken = "";
        requestData.setDeviceID(DeviceUtil.getDeviceId());

        User user = YiDeGuanApplication.mLoginUser;
        if (user != null) {
            serviceToken = user.getServiceToken();
            requestData.setUserID(user.getUserID());
        }
        requestData.setServiceToken(serviceToken);
        requestData.setSign(EncryptUtil.appSign(requestData));

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Call<ResponseData> call = baseService.requestSever(requestBody);
        call.enqueue(callback);
    }
}
