package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.support.v7.widget.GridLayoutManager;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityTemplateListBinding;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/30/16.
 */

public class TemplateListActivity extends ToolbarActivity<ActivityTemplateListBinding> {

    private final int mCountPerLine = 2; // 每行显示2个

    ArrayList<Template> mTemplateArrayList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_template_list;
    }

    @Override
    protected void initData() {
        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));
    }

    @Override
    protected void setListener() {

    }

    //region SERVER
    private void getPhotoTemplateListByTypeRequest() {
        GlobalRestful.getInstance().GetPhotoTemplateListByType(MDGroundApplication.mChoosedProductType, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mTemplateArrayList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                });


            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion
}
