package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalMyworksBinding;
import com.MDGround.HaiLanPrint.databinding.ItemMyworksBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.WorksInfo;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-09.
 */

public class MyWorksActivity extends ToolbarActivity<ActivityPersonalMyworksBinding> {
    public static final String TAG = "MyWorksActivity";
    public List<WorksInfo> mWorksInfoList = new ArrayList<>();
    public List<WorksInfo> mAllWorkInfoList = new ArrayList<>();
    public List<WorksInfo> mBlankWorkInfoList = new ArrayList<>();
    private MyWorksAdapter mAdapter;
    private boolean isEditor = true;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_myworks;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWorksList();
    }

    @Override
    protected void initData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.myworksrecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyWorksAdapter();
        mDataBinding.myworksrecyclerView.setAdapter(mAdapter);
        CountPriceAndAmunt();
        ViewUtils.loading(this);
        getWorksList();
    }

    //region ACTION
    //选中同一组内的作品
    public void selectAlikeType(View view, boolean b) {

        if (b) {
            int position = mDataBinding.myworksrecyclerView.getChildAdapterPosition(view);
            int type = mWorksInfoList.get(position).getTypeID();
            for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                if (mAllWorkInfoList.get(i).getTypeID() == type) {
                    mAllWorkInfoList.remove(i);
                    i--;
                }
            }
            for (int j = 0; j < mWorksInfoList.size(); j++) {
                if (mWorksInfoList.get(j).getTypeID() == type) {
                    WorksInfo worksInfo = mWorksInfoList.get(j);
                    KLog.e("--->" + worksInfo.getWorkID());
                    mAllWorkInfoList.add(worksInfo);
                }
            }
            if(mAllWorkInfoList.size()==mWorksInfoList.size()){
                mDataBinding.cbSelectAll.setChecked(true);
            }
            CountPriceAndAmunt();
        } else {
            mDataBinding.cbSelectAll.setChecked(false);
            int position = mDataBinding.myworksrecyclerView.getChildAdapterPosition(view);
            int type = mWorksInfoList.get(position).getTypeID();
            for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                if (mAllWorkInfoList.get(i).getTypeID() == type) {
                    mAllWorkInfoList.remove(i);
                    i--;
                }
            }
            CountPriceAndAmunt();
        }

        mAdapter.notifyDataSetChanged();
    }

    //region ACTION
    //选择指定作品
    public void selectPlaces(View view, boolean b) {
        int postion = mDataBinding.myworksrecyclerView.getChildPosition(view);
        int workID = mWorksInfoList.get(postion).getWorkID();
        if (b) {
            for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                if (mAllWorkInfoList.get(i).getWorkID() == workID) {
                    return;
                }
            }
            WorksInfo worksInfo = mWorksInfoList.get(postion);
            mAllWorkInfoList.add(worksInfo);
            if(mAllWorkInfoList.size()==mWorksInfoList.size()){
                mDataBinding.cbSelectAll.setChecked(true);
            }
            CountPriceAndAmunt();
        } else {
            mDataBinding.cbSelectAll.setChecked(false);
            for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                if (mAllWorkInfoList.get(i).getWorkID() == workID) {
                    mAllWorkInfoList.remove(i);
                    CountPriceAndAmunt();
                    return;
                }
            }
        }

    }
    //endregion

    //endregio
    @Override
    protected void setListener() {
        //编辑按钮
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditor) {
                    isEditor = true;
                    tvRight.setText(R.string.finish);
                    mDataBinding.llTotalprice.setVisibility(View.VISIBLE);
                    mDataBinding.tvBuy.setText(R.string.purchase);
                    mDataBinding.llAmunt.setBackgroundResource(R.color.colorOrange);
                } else {
                    isEditor = false;
                    tvRight.setText(R.string.edit);
                    mDataBinding.llTotalprice.setVisibility(View.INVISIBLE);
                    mDataBinding.tvBuy.setText(R.string.delete);
                    mDataBinding.llAmunt.setBackgroundResource(R.color.colorRed);
                }
            }
        });
        //region ACTION
        //全选按钮
        mDataBinding.cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBinding.cbSelectAll.isChecked()) {
                    mAllWorkInfoList = new ArrayList<WorksInfo>(mWorksInfoList);
                    float totalPrice=0;
                    for(int i=0;i<mAllWorkInfoList.size();i++){

                    }
                } else {
                    mAllWorkInfoList.clear();
                    mDataBinding.tvTotalPrice.setText("0.00");
                    mDataBinding.tvAmunt.setText("(0)");
                }
                CountPriceAndAmunt();
                mAdapter.notifyDataSetChanged();
            }
        });


        //endregion
    }

    //计算价格和数量并显示
    public void CountPriceAndAmunt(){
        if(mAllWorkInfoList.size()>0){
            float total=0;
            int amunt=0;
            for (int i=0;i<mAllWorkInfoList.size();i++){
                total=mAllWorkInfoList.get(i).getPrice()+total;
            }
            mDataBinding.tvAmunt.setText("("+mAllWorkInfoList.size()+")");
            mDataBinding.tvTotalPrice.setText(StringUtil.toYuanWithoutUnit(total));
        }else{
            mDataBinding.tvAmunt.setText("(0)");
            mDataBinding.tvTotalPrice.setText("0.00");
        }
    }
    public class MyWorksAdapter extends RecyclerView.Adapter<MyWorksAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myworks, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            WorksInfo worksInfo = mWorksInfoList.get(position);
            holder.itemMyworksBinding.setWorksInfo(worksInfo);
            holder.itemMyworksBinding.setShowHeader(isShowHeader(position));
            holder.itemMyworksBinding.tvWorksPice.setText(StringUtil.toYuanWithoutUnit(worksInfo.getPrice()));
            holder.itemMyworksBinding.cbTitle.setChecked(false);
            holder.itemMyworksBinding.cbItem.setChecked(false);
            //解决界面notifysetdatechage界面闪烁问题
            int photoPath = worksInfo.getPhotoCover();
            if (!String.valueOf(photoPath).equals(String.valueOf(holder.itemMyworksBinding.ivImage.getTag()))) {
                holder.itemMyworksBinding.ivImage.setTag(String.valueOf(photoPath));
                GlideUtil.loadImageByPhotoSID(holder.itemMyworksBinding.ivImage, photoPath);
                KLog.e("又加载了么");
            }
            if (mAllWorkInfoList.size() > 0) {
                for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                    if (mAllWorkInfoList.get(i).getWorkID() == worksInfo.getWorkID()) {
                        holder.itemMyworksBinding.cbItem.setChecked(true);
                        holder.itemMyworksBinding.cbTitle.setChecked(true);
                        break;
                    }
//                    for (int j = 0; j < mWorksInfoList.size(); j++) {
//                        if (mAllWorkInfoList.get(i).getWorkID() == mWorksInfoList.get(j).getWorkID())
//                            if (position == j) {
//                                holder.itemMyworksBinding.cbItem.setChecked(true);
//                                holder.itemMyworksBinding.cbTitle.setChecked(true);
//                            }
//                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return mWorksInfoList.size();
        }

        public boolean isShowHeader(int positon) {
            if (positon == 0) {
                return true;
            } else {
                WorksInfo currentWorksInfo = mWorksInfoList.get(positon);
                WorksInfo previousWorkInfo = mWorksInfoList.get(positon - 1);
                if (currentWorksInfo.getTypeID() != previousWorkInfo.getTypeID()) {
                    return true;
                }
            }
            return false;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public ItemMyworksBinding itemMyworksBinding;
            public MyViewHolder(final View itemView) {
                super(itemView);
                itemMyworksBinding = DataBindingUtil.bind(itemView);
                itemMyworksBinding.cbTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemMyworksBinding.cbTitle.isChecked()) {
                            KLog.e("选中");
                            selectAlikeType(itemView, true);
                        } else {
                            KLog.e("取消");
                            selectAlikeType(itemView, false);
                        }
                    }
                });
                itemMyworksBinding.cbItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemMyworksBinding.cbItem.isChecked()) {
                            selectPlaces(itemView, true);
                        } else {
                            selectPlaces(itemView, false);
                        }
                    }
                });
            }
        }

    }
    //region SERVEER
    //获取作品列表
    public void getWorksList(){
        GlobalRestful.getInstance().GetUserWorkList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    KLog.e(TAG, response.body().toString());
                    try {
                        JSONArray jsonObject = new JSONArray(response.body().getContent());
                        KLog.e(TAG, jsonObject.toString());
                        String worksInfos = jsonObject.toString();
                        mWorksInfoList = StringUtil.getInstanceByJsonString(worksInfos, new TypeToken<List<WorksInfo>>() {
                        });
                        KLog.e(TAG, mWorksInfoList.size());
                        if(mWorksInfoList.size()>0){
                            tvRight.setText("编辑");
                            tvRight.setVisibility(View.VISIBLE);
                            mDataBinding.llEnough.setVisibility(View.VISIBLE);
                            mDataBinding.llBlank.setVisibility(ViewStub.GONE);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            tvRight.setVisibility(View.GONE);
                            mDataBinding.llBlank.setVisibility(View.VISIBLE);
                            mDataBinding.llEnough.setVisibility(View.GONE);
                        }

                        ViewUtils.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
            }
        });
    }
    //endregion
    //region SEVER
    //删除掉选中列表
    public void DeleteUserWork(List<Integer> WorkIDList){
        GlobalRestful.getInstance().DeleteUserWork(WorkIDList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region ACITON
    //购买或者删除按钮
    public void toBuyOrDelete(View view){
        if(isEditor){

        }else{

        }
    }
    //endregion
}
