package com.MDGround.HaiLanPrint.activity.phoneshell;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.enumobject.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPhoneShellStartBinding;
import com.MDGround.HaiLanPrint.enumobject.MaterialType;
import com.MDGround.HaiLanPrint.enumobject.PhotoExplainTypeEnum;
import com.MDGround.HaiLanPrint.enumobject.ProductMaterial;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
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
    }

    @Override
    protected void initData() {
        for (PhotoTypeExplain photoTypeExplain : MDGroundApplication.sInstance.getPhotoTypeExplainArrayList()) {
            if (photoTypeExplain.getExplainType() == PhotoExplainTypeEnum.Banner.value()
                    && photoTypeExplain.getTypeID() == ProductType.PhoneShell.value()) {
                GlideUtil.loadImageByPhotoSIDWithDialog(mDataBinding.ivBanner,
                        photoTypeExplain.getPhotoSID());
                break;
            }
        }
//        getSpecificationRequest();
    }

    @Override
    protected void setListener() {
    }

    private void changeModelTextAndMaterialAvailable() {
        mDataBinding.tvPhoneModel.setText(MDGroundApplication.sInstance.getChoosedMeasurement().getTitle() + "-" + MDGroundApplication.sInstance.getChoosedTemplate().getTemplateName());
        mDataBinding.tvPrice.setText(StringUtil.toYuanWithUnit(MDGroundApplication.sInstance.getChoosedTemplate().getPrice()));

        mDataBinding.rgMaterial.clearCheck();

        if ((MDGroundApplication.sInstance.getChoosedTemplate().getMaterialType() & MaterialType.Silicone.value()) != 0) {
            mDataBinding.rbSilicone.setEnabled(true);
            if (mDataBinding.rgMaterial.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbSilicone.setChecked(true);

                Template template = MDGroundApplication.sInstance.getChoosedTemplate();
                template.setSelectMaterial(ProductMaterial.PhoneShell_Silicone.getText());
                MDGroundApplication.sInstance.setChoosedTemplate(template);
            }
        } else {
            mDataBinding.rbSilicone.setEnabled(false);
        }

        if ((MDGroundApplication.sInstance.getChoosedTemplate().getMaterialType() & MaterialType.Plastic.value()) != 0) {
            mDataBinding.rbPlastic.setEnabled(true);
            if (mDataBinding.rgMaterial.getCheckedRadioButtonId() == -1) {
                mDataBinding.rbPlastic.setChecked(true);

                Template template = MDGroundApplication.sInstance.getChoosedTemplate();
                template.setSelectMaterial(ProductMaterial.PhoneShell_Plastic.getText());
                MDGroundApplication.sInstance.setChoosedTemplate(template);
            }
        } else {
            mDataBinding.rbPlastic.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (MDGroundApplication.sInstance.getChoosedTemplate() != null) {
                changeModelTextAndMaterialAvailable();
            }
        }
    }

    //region ACTION
    public void toPhoneShellIllutrationActivityAction(View view) {
        Intent intent = new Intent(this, PhoneShellForIllustrationActivity.class);
        startActivity(intent);
    }
    //endregion

    //region ACTION
    public void toSelectBrandActivityAction(View view) {
        Intent intent = new Intent(this, PhoneShellSelectBrandActivity.class);
        startActivityForResult(intent, 0);
    }

    public void nextStepAction(View view) {
        if (MDGroundApplication.sInstance.getChoosedTemplate() == null) {
            ViewUtils.toast(R.string.please_select_phone_model);
            return;
        }
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
                            MDGroundApplication.sInstance.setChoosedMeasurement(specList.get(0));
                            getPhotoTemplateListRequest();
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

    private void getPhotoTemplateListRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoTemplateList(MDGroundApplication.sInstance.getChoosedMeasurement().getTypeDescID(),
                new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ViewUtils.dismiss();
                        if (ResponseCode.isSuccess(response.body())) {
                            ArrayList<Template> templateList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                            });

                            if (templateList.size() > 0) {
                                Template template = templateList.get(0);

                                MDGroundApplication.sInstance.setChoosedTemplate(template);

                                changeModelTextAndMaterialAvailable();
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
