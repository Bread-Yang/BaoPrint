package com.MDGround.HaiLanPrint.activity.deliveryaddress;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.BR;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityChooseDeliveryAddressBinding;
import com.MDGround.HaiLanPrint.databinding.ItemDeliveryAddressBinding;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.views.itemdecoration.DividerItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/24/16.
 */

public class ChooseDeliveryAddressActivity extends ToolbarActivity<ActivityChooseDeliveryAddressBinding> {

    private DeliveryAddressAdapter mAdapter;

    private ArrayList<DeliveryAddress> mAddressArrayList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_delivery_address;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddressListRequest();
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(0));

        mAdapter = new DeliveryAddressAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        getUserAddressListRequest();
    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void toAddDeliveryAddressActivityAction(View view) {
        if (Constants.MAX_DELIVERY_ADDRESS - mAddressArrayList.size() > 0) {
            Intent intent = new Intent(this, EditDeliveryAddressActivity.class);
            startActivity(intent);
        }
    }
    //endregion

    //region SERVER
    private void getUserAddressListRequest() {
        GlobalRestful.getInstance().GetUserAddressList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mAddressArrayList = response.body().getContent(new TypeToken<ArrayList<com.MDGround.HaiLanPrint.models.DeliveryAddress>>() {
                });

                mDataBinding.tvCanAddNum.setText(getString(R.string.still_can_add_address, Constants.MAX_DELIVERY_ADDRESS - mAddressArrayList.size()));

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region ADAPTER
    public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.ViewHolder> {

        @Override
        public DeliveryAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_delivery_address, parent, false);
            DeliveryAddressAdapter.ViewHolder holder = new DeliveryAddressAdapter.ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(DeliveryAddressAdapter.ViewHolder holder, int position) {
            holder.viewDataBinding.setVariable(BR.deliveryAddress, mAddressArrayList.get(position));
            holder.viewDataBinding.setVariable(BR.handlers, holder);
        }

        @Override
        public int getItemCount() {
            return mAddressArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemDeliveryAddressBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void toEditDeliveryAddressActivityAction(View view) {
                DeliveryAddress deliveryAddress = mAddressArrayList.get(getAdapterPosition());

                Intent intent = new Intent(ChooseDeliveryAddressActivity.this, EditDeliveryAddressActivity.class);
                intent.putExtra(Constants.KEY_DELIVERY_ADDRESS, deliveryAddress);
                startActivity(intent);
            }

        }
    }
    //endregion

}
