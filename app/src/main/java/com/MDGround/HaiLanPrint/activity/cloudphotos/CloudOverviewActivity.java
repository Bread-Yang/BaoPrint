package com.MDGround.HaiLanPrint.activity.cloudphotos;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.BR;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityCloudOverviewBinding;
import com.MDGround.HaiLanPrint.databinding.ItemCloudOverviewBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.CloudImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/11/16.
 */
public class CloudOverviewActivity extends ToolbarActivity<ActivityCloudOverviewBinding> {

    private CloudOverviewAdapter mAdapter;

    private ArrayList<CloudImage> mImagesList = new ArrayList<CloudImage>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_cloud_overview;
    }


    @Override
    protected void onResume() {
        super.onResume();

        getPhotoCountRequest();
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);

        mAdapter = new CloudOverviewAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

    }

    private void getPhotoCountRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetCloudPhotoCount(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    mImagesList.clear();

                    ArrayList<CloudImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<CloudImage>>() {
                    });

                    mImagesList.addAll(tempImagesList);

                    mAdapter.notifyDataSetChanged();

                    ViewUtils.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private class CloudOverviewAdapter extends RecyclerView.Adapter<CloudOverviewAdapter.BindingHolder> {

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cloud_overview, parent, false);
            BindingHolder holder = new BindingHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            holder.viewDataBinding.setVariable(BR.image, mImagesList.get(position));
        }

        @Override
        public int getItemCount() {
            return mImagesList.size();
        }

        public class BindingHolder extends RecyclerView.ViewHolder {

            public ItemCloudOverviewBinding viewDataBinding;

            public BindingHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }
        }
    }
}
