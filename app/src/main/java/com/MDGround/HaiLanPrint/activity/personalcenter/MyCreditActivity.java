package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalCreditBinding;
import com.MDGround.HaiLanPrint.databinding.ItemCreditQueryBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.UserIntegral;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.DividerItemDecoration;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-08.
 */

public class MyCreditActivity extends ToolbarActivity<ActivityPersonalCreditBinding> {
    public String mTotalAmount;
    public ArrayList<UserIntegral> mUserCreditList = new ArrayList<>();

    MyCreditadapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_credit;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
        getSpecificationRequest();


    }

    public class BindHandler {
        public void ClicEvent(View v) {
            int position = mDataBinding.recyclerView.getChildAdapterPosition(v);
        }
    }

    @Override
    protected void setListener() {

    }

    //region SERVER
    public void getSpecificationRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetUserIntegralInfo(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().getContent());
                        mTotalAmount = jsonObject.getString("TotalAmount");
                        String UserIntegralList = jsonObject.getString("UserIntegralList");
                        mUserCreditList = StringUtil.getInstanceByJsonString(UserIntegralList, new TypeToken<ArrayList<UserIntegral>>() {
                        });

                        mDataBinding.tvCredit.setText(mTotalAmount);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCreditActivity.this);
                        mDataBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(12));
                        mAdapter = new MyCreditadapter(MyCreditActivity.this);
                        mDataBinding.recyclerView.setAdapter(mAdapter);
                        ViewUtils.dismiss();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
        //endregion


    }
    //endregion

    //region ADAPTER
    public class MyCreditadapter extends RecyclerView.Adapter<MyCreditadapter.ViewHolder> {
        Context context;

        MyCreditadapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_credit_query, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BindHandler handler = new BindHandler();
            holder.itemIntegralQueryBinding.setHandler(handler);

            holder.itemIntegralQueryBinding.setCreditInfo(mUserCreditList.get(position));
            if(Integer.valueOf(mUserCreditList.get(position).getAmount())<0){
                holder.itemIntegralQueryBinding.tvIntegral.setTextColor(getResources().getColor(R.color.color_0ec100));
            }else{
                 String plus="+ "+mUserCreditList.get(position).getAmount();
                holder.itemIntegralQueryBinding.tvIntegral.setTextColor(getResources().getColor(R.color.colorRed));
            }
        }

        @Override
        public int getItemCount() {
            return mUserCreditList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ItemCreditQueryBinding itemIntegralQueryBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                itemIntegralQueryBinding = DataBindingUtil.bind(itemView);
            }
        }
    }
    //endregion
}
