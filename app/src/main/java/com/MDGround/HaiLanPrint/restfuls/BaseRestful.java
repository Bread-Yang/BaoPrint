package com.MDGround.HaiLanPrint.restfuls;

import android.content.Context;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.enumobject.restfuls.PlatformType;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.Interceptor.DecryptedPayloadInterceptor;
import com.MDGround.HaiLanPrint.restfuls.Interceptor.ProgressRequestBody;
import com.MDGround.HaiLanPrint.restfuls.bean.RequestData;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.MDGround.HaiLanPrint.utils.EncryptUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.ToolNetwork;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yoghourt on 5/5/16.
 */
public abstract class BaseRestful {

    private Context mContext;

    private BaseService baseService;

    protected abstract BusinessType getBusinessType();

    protected abstract String getHost();

    // service接口
    public interface BaseService {
        @POST("api/RpcService.ashx/")
        Call<ResponseData> normalRequest(@Body RequestBody requestBody);  // 普通接口请求地址

        @POST("Api/RpcInternalService.ashx/")
        Call<ResponseData> fileRequest(@Body RequestBody requestBody);    // 图片请求地址

        @POST("Api/RpcInternalService.ashx/")
        Call<ResponseData> imageUploadRequest(@Body ProgressRequestBody requestBody);    // 图片请求地址
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
        mContext = MDGroundApplication.mInstance;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getHost())
                .addConverterFactory(GsonConverterFactory.create()); // json转成对象

        if (getBusinessType() == BusinessType.FILE) {  // 请求图片不需要加密,增加上传进度拦截器
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.interceptors().add(new UpLoadProgressInterceptor(new CountingRequestBody.Listener() {
//                @Override
//                public void onRequestProgress(long bytesWritten, long contentLength) {
//                    KLog.e("bytesWritten : " + bytesWritten);
//                    KLog.e("contentLength : " + contentLength);
//                }
//            }));
//
//            builder = builder.client(httpClient.build());
        } else {    // 其他普通请求需要加密
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.interceptors().add(new DecryptedPayloadInterceptor());  //请求前加密,返回前解密

            builder = builder.client(httpClientBuilder.build());
        }

        Retrofit retrofit = builder.build();
        baseService = retrofit.create(BaseService.class);
    }

    private RequestData createRequestData(String functionName, JsonObject queryData) {
        RequestData requestData = new RequestData();

        if (queryData != null) {
            requestData.setQueryData(queryData.toString());
        }
        requestData.setFunctionName(functionName);
        requestData.setCulture(DeviceUtil.getLanguage(mContext));
//        requestData.setBusinessCode(getBusinessType().getType());
        requestData.setBusinessCode(BusinessType.Global.getType()); // 全部用1
        requestData.setActionTimeSpan(System.currentTimeMillis() / 1000);
        requestData.setPlatform(getPlatform());

        String serviceToken = "";
        requestData.setDeviceID(DeviceUtil.getDeviceId());

        User user = MDGroundApplication.mLoginUser;
        if (user != null) {
            serviceToken = user.getServiceToken();
            requestData.setUserID(user.getUserID());
        }
        requestData.setServiceToken(serviceToken);
        requestData.setSign(EncryptUtil.appSign(requestData));

        return requestData;
    }

    private RequestBody createRequestBody(String functionName, JsonObject queryData) {
        RequestData requestData = createRequestData(functionName, queryData);

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        return requestBody;
    }

    private ProgressRequestBody createProgressRequestBody(String functionName, JsonObject queryData, ProgressRequestBody.UploadCallbacks uploadCallbacks) {
        RequestData requestData = createRequestData(functionName, queryData);

        String json = new Gson().toJson(requestData);

        return new ProgressRequestBody(json, uploadCallbacks);
    }

    // 普通接口请求(异步)
    protected void asynchronousPost(final String functionName, JsonObject queryData, final Callback<ResponseData> secondCallback) {
        Callback firstCallback = new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                KLog.e("\n\"" + functionName + "\"接口返回的Response是 : " + "\n" + "{"
                        + "\"Code\" :" + response.body().getCode() + ","
                        + "\"Message\" :" + response.body().getMessage() + ","
                        + "\"Content\" : " + response.body().getContent() + "}" + "\n");

                if (response.body().getCode() == ResponseCode.InvalidToken.getValue()) { // 请求token失效,重新登录
                    DeviceUtil.logoutUser();
                    NavUtils.toLoginActivity(mContext);
                } else if (response.body().getCode() == ResponseCode.SystemError.getValue()) {
                    ViewUtils.toast(R.string.request_fail);  // 请求超时
                    ViewUtils.dismiss();
                } else {
                    if (secondCallback != null) {
                        secondCallback.onResponse(call, response);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.toast(R.string.request_timeout);  // 请求超时
                ViewUtils.dismiss();
                if (secondCallback != null) {
                    secondCallback.onFailure(call, t);
                }
            }
        };

        if (ToolNetwork.getInstance().isConnected()) {
            RequestBody requestBody = createRequestBody(functionName, queryData);

            Call<ResponseData> call = null;
            if (getBusinessType() == BusinessType.Global) {
                KLog.e("\n\"" + functionName + "\"接口请求json数据:" + "\n" + new Gson().toJson(createRequestData(functionName, queryData)));

                call = baseService.normalRequest(requestBody);
            } else if (getBusinessType() == BusinessType.FILE) {
                call = baseService.fileRequest(requestBody);
            }
            call.enqueue(firstCallback);
        } else {
            ViewUtils.toast(R.string.network_unavailable);
            ViewUtils.dismiss();
            secondCallback.onFailure(null, null);
        }
    }

    // 请求文件/图片等请求(同步)
    protected ResponseData synchronousPost(String functionName, JsonObject queryData) {
        RequestBody requestBody = createRequestBody(functionName, queryData);

        Call<ResponseData> call = baseService.fileRequest(requestBody);
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 上传图片
    protected void uploadImagePost(String functionName, JsonObject queryData,
                                   ProgressRequestBody.UploadCallbacks uploadCallbacks,
                                   Callback<ResponseData> callback) {
        ProgressRequestBody requestBody = createProgressRequestBody(functionName, queryData, uploadCallbacks);

        Call<ResponseData> call = null;
        call = baseService.imageUploadRequest(requestBody);
        call.enqueue(callback);
    }

    protected String convertObjectToString(Object object) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(object);
    }
}
