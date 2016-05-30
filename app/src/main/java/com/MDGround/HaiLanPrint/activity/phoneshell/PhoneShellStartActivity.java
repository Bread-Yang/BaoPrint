package com.MDGround.HaiLanPrint.activity.phoneshell;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPhoneShellStartBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.NavUtils;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (MDGroundApplication.mChoosedTemplate != null) {
                mDataBinding.tvPhoneModel.setText(MDGroundApplication.mChoosedTemplate.getTemplateName());
                mDataBinding.tvPrice.setText(StringUtil.toYuanWithUnit(MDGroundApplication.mChoosedTemplate.getPrice()));
            }
        }
    }

    //region ACTION
    public void toSelectBrandActivityAction(View view) {
        Intent intent = new Intent(this, PhoneShellSelectBrandActivity.class);
        startActivityForResult(intent, 0);
    }

    public void nextStepAction(View view) {
        NavUtils.toSelectAlbumActivity(this);
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

                        ArrayList<Measurement> specList = StringUtil.getInstanceByJsonString(PhotoTypeDescList, new TypeToken<ArrayList<Measurement>>() {
                        });

                        if (specList.size() > 0) {
                            getPhotoTemplateListRequest(specList.get(0));
                        }

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

    private void getPhotoTemplateListRequest(Measurement measurement) {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoTemplateList(measurement.getTypeDescID(),
                new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ViewUtils.dismiss();
                        if (ResponseCode.isSuccess(response.body())) {
                            ArrayList<Template> templateList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                            });

                            if (templateList.size() > 0) {
                                Template template = templateList.get(0);

                                mDataBinding.tvPhoneModel.setText(template.getTemplateName());
                                mDataBinding.tvPrice.setText(StringUtil.toYuanWithUnit(template.getPrice()));
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
