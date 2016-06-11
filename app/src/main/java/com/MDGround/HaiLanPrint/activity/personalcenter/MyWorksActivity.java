package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalMyworksBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.WorksInfo;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
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
    public MyWorksAdapter mAdapter;
//    public List<WorksInfo> mMagazineList = new ArrayList<>();
//    public List<WorksInfo> mPostCardList = new ArrayList<>();
//    public List<WorksInfo> mArtBookList = new ArrayList<>();
//    public List<WorksInfo> mLOMOCardList = new ArrayList<>();
//    public List<WorksInfo> mMagicCupList = new ArrayList<>();


    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_myworks;
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.rvWorksList.setLayoutManager(layoutManager);
        mAdapter = new MyWorksAdapter();
        mDataBinding.rvWorksList.setAdapter(mAdapter);
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

//                        for (WorksInfo worksInfo : worksInfoList) {
//                            if (worksInfo.getTypeID() == Constants.ART_BOOK_TYPE_ID) {
//                                mArtBookList.add(worksInfo);
//                            } else if (worksInfo.getTypeID() == Constants.MAGIC_CUP_TYPE_ID) {
//                                mMagicCupList.add(worksInfo);
//                            } else if (worksInfo.getTypeID() == Constants.MAGAZINE_B00k_TYPE_ID) {
//                                mMagazineList.add(worksInfo);
//                            } else if (worksInfo.getTypeID() == Constants.LOMO_CARD_TYPE_ID) {
//                                mLOMOCardList.add(worksInfo);
//                            } else if (worksInfo.getTypeID() == Constants.POST_CARD_TYPE_ID) {
//                                mPostCardList.add(worksInfo);
//                            }
//                        }
//                        KLog.e(TAG, mPostCardList.size());
//                        KLog.e(TAG, mLOMOCardList.size());
//                        KLog.e(TAG, mArtBookList.size());
//                        KLog.e(TAG, mMagazineList.size());

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

    @Override
    protected void setListener() {

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

        }

        @Override
        public int getItemCount() {
            return mWorksInfoList.size();
        }

        public boolean isShowHeader(int positon) {
            if (positon == 0) {
                return true;
            }else {

            }
              return  false;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
