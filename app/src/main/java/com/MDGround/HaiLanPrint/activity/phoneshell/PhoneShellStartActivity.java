package com.MDGround.HaiLanPrint.activity.phoneshell;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPhoneShellStartBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/11/16.
 */
public class PhoneShellStartActivity extends ToolbarActivity<ActivityPhoneShellStartBinding> {

    private ArrayList<Measurement> mSpecList = new ArrayList<Measurement>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone_shell_start;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSpecificationRequest();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    //region ACTION
    public void toSelectBrandActivityAction(View view) {
        Intent intent = new Intent(this, PhoneShellSelectBrandActivity.class);
        startActivity(intent);
    }
    //endregion

    //region SERVER
    private void getSpecificationRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoType(ProductType.PhoneShell, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                if (ResponseCode.isSuccess(response.body())) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().getContent());

                        String PhotoTypeDescList = jsonObject.getString("PhotoTypeDescList");

                        mSpecList = StringUtil.getInstanceByJsonString(PhotoTypeDescList, new TypeToken<ArrayList<Measurement>>() {
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }
    //endregion

}
