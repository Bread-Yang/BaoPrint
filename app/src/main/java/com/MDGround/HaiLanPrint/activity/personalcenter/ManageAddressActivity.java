package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityManageAddressBinding;
import com.MDGround.HaiLanPrint.databinding.ItemManageAddressBinding;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-14.
 */
public class ManageAddressActivity extends ToolbarActivity<ActivityManageAddressBinding> {
    private ArrayList<DeliveryAddress> mUserAddressList=new ArrayList<>();
    private DeliveryAddress mUserAddress;
    private  ManageAddressAdatper adatper;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_manage_address;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(linearLayoutManager);
        adatper=new ManageAddressAdatper();
        mDataBinding.recyclerView.setAdapter(adatper);
        getUserAddressListRequest();

    }
    //region SERVER
    private void getUserAddressListRequest() {
        GlobalRestful.getInstance().GetUserAddressList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mUserAddressList=response.body().getContent(new com.google.gson.reflect.TypeToken<ArrayList<DeliveryAddress>>(){});
                adatper.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
   //endregion
    @Override
    protected void setListener() {

    }

    public class BindingHandler{
        public  void onClickItem(View view){

        }
    }
    //region ADAPTER
       public class ManageAddressAdatper extends RecyclerView.Adapter<ManageAddressAdatper.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(ManageAddressActivity.this).inflate(R.layout.item_manage_address,parent,false);
            MyViewHolder holder=new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DeliveryAddress address=mUserAddressList.get(position);
            viewItemBinding.setAddress(address);

        }

        @Override
        public int getItemCount() {
            return mUserAddressList.size();
        }
               ItemManageAddressBinding viewItemBinding;
        class  MyViewHolder extends RecyclerView.ViewHolder{
              public MyViewHolder(View itemView) {
                  super(itemView);
                  viewItemBinding= DataBindingUtil.bind(itemView);
              }
          }
    }
    //enregion
}
