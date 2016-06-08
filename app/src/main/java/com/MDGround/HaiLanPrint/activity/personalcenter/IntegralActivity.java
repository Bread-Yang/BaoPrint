package com.MDGround.HaiLanPrint.activity.personalcenter;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalIntegralBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-07.
 */
public class IntegralActivity extends ToolbarActivity<ActivityPersonalIntegralBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_integral;
    }

    @Override
    protected void initData() {
        // mDataBinding.recyclerView

        GlobalRestful.getInstance().GetUserIntegralInfo(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                System.out.println(response.body().toString());
                if (ResponseCode.isSuccess(response.body())) {

                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setListener() {

    }
}
