package com.MDGround.HaiLanPrint.activity.coupon;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityMyCouponBinding;
import com.MDGround.HaiLanPrint.databinding.ItemCouponBinding;
import com.MDGround.HaiLanPrint.models.Coupon;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/23/16.
 */

public class MyCouponActivity extends ToolbarActivity<ActivityMyCouponBinding> {

    private MyCouponAdapter mAdapter;

    private ArrayList<Coupon> mAllCouponArrayList = new ArrayList<>();
    private ArrayList<Coupon> mShowCouponArrayList = new ArrayList<>();

    private boolean mIsAvailable = true; // 默认是"可使用"

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_coupon;
    }

    @Override
    protected void initData() {
        mDataBinding.tabLayout.addTab(mDataBinding.tabLayout.newTab().setText(getString(R.string.available_coupon, 0)));
        mDataBinding.tabLayout.addTab(mDataBinding.tabLayout.newTab().setText(getString(R.string.unavailable_coupon, 0)));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyCouponAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        getUserCouponListRequest();
    }

    @Override
    protected void setListener() {
        mDataBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentSelectedTabIndex = tab.getPosition();

                if (currentSelectedTabIndex == 0) {
                    mIsAvailable = true; // 可使用
                } else {
                    mIsAvailable = false; // 不可以使用
                }

                refreshRecyclerView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void refreshRecyclerView() {
        mShowCouponArrayList.clear();
        for (Coupon coupon : mAllCouponArrayList) {
            // 判断优惠券是否可用条件：当前时间在ActiveTime和ExpireTime 之间，并且 CouponStatus是0
            boolean couponStatus = DateUtils.isWithinTwoDate(coupon.getActiveTime(), coupon.getExpireTime());

            if (couponStatus == mIsAvailable) {
                mShowCouponArrayList.add(coupon);
            }
        }

        if (mShowCouponArrayList.size() > 0) {
            mDataBinding.ivEmpty.setVisibility(View.GONE);
            mDataBinding.recyclerView.setVisibility(View.VISIBLE);

            mAdapter.notifyDataSetChanged();
        } else {
            mDataBinding.ivEmpty.setVisibility(View.VISIBLE);
            mDataBinding.recyclerView.setVisibility(View.GONE);
        }
    }

    //region ACTION
    public void activateCouponAction(View view) {
        String activationCode = mDataBinding.cetActivationCode.getText().toString();

        if (StringUtil.isEmpty(activationCode)) {
            ViewUtils.toast(R.string.input_activation_code);
            return;
        }

        ViewUtils.loading(this);
        GlobalRestful.getInstance().ActivatingCoupon(activationCode, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                getUserCouponListRequest();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region SERVER
    private void getUserCouponListRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetUserCouponList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mAllCouponArrayList = response.body().getContent(new TypeToken<ArrayList<Coupon>>() {
                });

                int availableCount = 0;
                int unavailableCount = 0;

                for (Coupon coupon : mAllCouponArrayList) {
                    boolean couponStatus = DateUtils.isWithinTwoDate(coupon.getActiveTime(), coupon.getExpireTime());
                    if (couponStatus) {
                        availableCount++;
                    } else {
                        unavailableCount++;
                    }
                }

                mDataBinding.tabLayout.getTabAt(0).setText(getString(R.string.available_coupon, availableCount));
                mDataBinding.tabLayout.getTabAt(1).setText(getString(R.string.unavailable_coupon, unavailableCount));

                refreshRecyclerView();

                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region ADAPTER
    public class MyCouponAdapter extends RecyclerView.Adapter<MyCouponAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_coupon, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.viewDataBinding.setCoupon(mShowCouponArrayList.get(position));
            holder.viewDataBinding.setIsAvailable(mIsAvailable);
        }

        @Override
        public int getItemCount() {
            return mShowCouponArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemCouponBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }
        }
    }
    //endregion
}
