package com.MDGround.HaiLanPrint.activity.phoneshell;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoMeasurementDescription;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPhoneShellSelectBrandBinding;
import com.MDGround.HaiLanPrint.databinding.ItemPhoneSheelSelectBrandBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.NormalItemDecoration;
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
public class PhoneShellSelectBrandActivity extends ToolbarActivity<ActivityPhoneShellSelectBrandBinding> {

    private PhoneShellSelectBrandAdapter mAdapter;

    private ArrayList<Measurement> mSpecList = new ArrayList<Measurement>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone_shell_select_brand;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSpecificationRequest();
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);
        mDataBinding.recyclerView.addItemDecoration(new NormalItemDecoration(ViewUtils.dp2px(2)));

        mAdapter = new PhoneShellSelectBrandAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneShellSelectBrandActivity.this, PrintPhotoMeasurementDescription.class);
                startActivity(intent);
            }
        });
    }

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

                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    mImagesList.clear();
//
//                    ArrayList<MDImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
//                    });
//
//                    mImagesList.addAll(tempImagesList);
//
//                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }
    //endregion

    //region ADAPTER
    public class PhoneShellSelectBrandAdapter extends RecyclerView.Adapter<PhoneShellSelectBrandAdapter.BindingHolder> {

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_phone_sheel_select_brand, parent, false);
            BindingHolder holder = new BindingHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            holder.viewDataBinding.setMeasurement(mSpecList.get(position));
            holder.viewDataBinding.setHandlers(holder);
        }

        @Override
        public int getItemCount() {
            return mSpecList.size();
        }

        public class BindingHolder extends RecyclerView.ViewHolder {

            public ItemPhoneSheelSelectBrandBinding viewDataBinding;

            public BindingHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void toPhoneShellSelectModelActivityAction(View view) {
                MDGroundApplication.mChooseMeasurement = mSpecList.get(getAdapterPosition());

                NavUtils.toSelectAlbumActivity(view.getContext());
            }
        }
    }
    //endregion
}
