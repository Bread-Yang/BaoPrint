package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
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
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-14.
 */
public class ManageAddressActivity extends ToolbarActivity<ActivityManageAddressBinding> {
    public static final String FROM_HERE="FROM_HERE";
    public static final  String ADDRESS="ADDRESS";
    public static final int FOR_UPATE=1;
    public static final  int FOR_SAVE=2;
    private ArrayList<DeliveryAddress> mUserAddressList=new ArrayList<>();
    private DeliveryAddress mUserAddress;
    private  ManageAddressAdatper adatper;
    private View view;
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
        View view=LayoutInflater.from(this).inflate(R.layout.item_address_foot,null);
        adatper=new ManageAddressAdatper();
        mDataBinding.recyclerView.setAdapter(adatper);
//        adatper.addFooter()
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
        public  void onEidtorItem(View view){
//            int position = mDataBinding.recyclerView.getChildAdapterPosition(view);
//            DeliveryAddress deliveryAddress=mUserAddressList.get(position);

        }
        public void onDeleteItem(View view){
//            int position = mDataBinding.recyclerView.getChildAdapterPosition(view);;
//            DeliveryAddress address=mUserAddressList.get(position);
//            int AutoID=address.getAutoID();
//            deleteAddress(AutoID);
        }

    }
    BindingHandler handler=new BindingHandler();
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
            viewItemBinding.setHandler(handler);

        }

        @Override
        public int getItemCount() {
            return mUserAddressList.size();
        }
             public  ItemManageAddressBinding viewItemBinding;
        class  MyViewHolder extends RecyclerView.ViewHolder{
              public MyViewHolder(View itemView) {
                  super(itemView);
                  viewItemBinding= DataBindingUtil.bind(itemView);
              }
          }
    }
    //enregion SERVER
    //编辑地址
    public void updateAddress(DeliveryAddress address){
       Intent intent=new Intent(this,EditAddressActivity.class);
        intent.putExtra(FROM_HERE,FOR_UPATE);
        intent.putExtra(ADDRESS,address);
        startActivity(intent);
    }
    //删除地址
    public void  deleteAddress(int AutoID){
        ViewUtils.loading(this);
        GlobalRestful.getInstance().DeleteUserAddress(AutoID, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               if(ResponseCode.isSuccess(response.body())){
                    ViewUtils.dismiss();
                };
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                    ViewUtils.dismiss();
                    ViewUtils.toast("删除失败");
            }
        });
    }
    //endregion

}
