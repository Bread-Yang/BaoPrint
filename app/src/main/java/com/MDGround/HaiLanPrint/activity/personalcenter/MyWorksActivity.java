package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private boolean isEditor = false;
    private boolean flag = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_myworks;
    }

    @Override
    protected void initData() {
        tvRight.setText("编辑");
        tvRight.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.myworksrecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyWorksAdapter();
        mDataBinding.myworksrecyclerView.setAdapter(mAdapter);
        ViewUtils.loading(this);
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
                        mAdapter.notifyDataSetChanged();
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
            for (int i = 0; i < mWorksInfoList.size(); i++) {
                if (mWorksInfoList.get(i).getTypeID() == type) {
                    WorksInfo worksInfo = mWorksInfoList.get(i);
                    KLog.e("--->" + worksInfo.getWorkID());
                    mAllWorkInfoList.add(worksInfo);
                }
            }
            if(mAllWorkInfoList.size()==mWorksInfoList.size()){
                mDataBinding.cbSelectAll.setChecked(true);
            }
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
            KLog.e("？有没有加进来");
            WorksInfo worksInfo = mWorksInfoList.get(postion);
            mAllWorkInfoList.add(worksInfo);
            if(mAllWorkInfoList.size()==mWorksInfoList.size()){
                mDataBinding.cbSelectAll.setChecked(true);
            }
        } else {
            mDataBinding.cbSelectAll.setChecked(false);
            for (int i = 0; i < mAllWorkInfoList.size(); i++) {
                if (mAllWorkInfoList.get(i).getWorkID() == workID) {
                    mAllWorkInfoList.remove(i);
                    return;
                }
            }
        }
    }
    //endregion

    //endregio
    @Override
    protected void setListener() {
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditor) {
                    isEditor = true;
                    tvRight.setText("完成");
                } else {
                    isEditor = false;
                    tvRight.setText("编辑");
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
                } else {
                    mAllWorkInfoList.clear();
                }
                mAdapter.notifyDataSetChanged();
            }
        });


        //endregion
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


}
