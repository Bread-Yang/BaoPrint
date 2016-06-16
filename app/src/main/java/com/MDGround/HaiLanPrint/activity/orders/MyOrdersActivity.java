package com.MDGround.HaiLanPrint.activity.orders;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityMyOrdersBinding;
import com.MDGround.HaiLanPrint.databinding.ItemMyOrderBinding;
import com.MDGround.HaiLanPrint.enumobject.OrderStatus;
import com.MDGround.HaiLanPrint.models.Order;
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/30/16.
 */

public class MyOrdersActivity extends ToolbarActivity<ActivityMyOrdersBinding> {

    private MyOrdersAdapter mAdapter;

    private ArrayList<Order> mOrdersArrayList = new ArrayList<>();

    private ArrayList<OrderWork> mOrderWorkArrayList = new ArrayList<>();

    private HashMap<Integer, OrderInfo> mOrderInfoHashMap = new HashMap<>();

    private OrderStatus mOrderStatus = OrderStatus.All;

    private boolean mIsBackToMainActivity = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_orders;
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            mIsBackToMainActivity = getIntent().getBooleanExtra(Constants.KEY_FROM_PAYMENT_SUCCESS, false);
        }

        String[] titles = getResources().getStringArray(R.array.order_status_array);

        for (String title : titles) {
            mDataBinding.tabLayout.addTab(mDataBinding.tabLayout.newTab().setText(title));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyOrdersAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        getUserOrderListRequest();
    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBackToMainActivity) {
                    NavUtils.toMainActivity(MyOrdersActivity.this);
                } else {
                    finish();
                }
            }
        });

        mDataBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentSelectedTabIndex = tab.getPosition();

                switch (currentSelectedTabIndex) {
                    case 0:
                        mOrderStatus = OrderStatus.All;
                        break;
                    case 1:
                        mOrderStatus = OrderStatus.Paid;
                        break;
                    case 2:
                        mOrderStatus = OrderStatus.Delivered;
                        break;
                    case 3:
                        mOrderStatus = OrderStatus.Finished;
                        break;
                    case 4:
                        mOrderStatus = OrderStatus.AllRefund;
                        break;

                }

                getUserOrderListRequest();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //region SERVER
    private void getUserOrderListRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetUserOrderList(mOrderStatus, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mOrdersArrayList = response.body().getContent(new TypeToken<ArrayList<Order>>() {
                });

                mOrderInfoHashMap.clear();
                mOrderWorkArrayList.clear();
                for (Order order : mOrdersArrayList) {
                    mOrderInfoHashMap.put(order.getOrderInfo().getOrderID(), order.getOrderInfo());

                    mOrderWorkArrayList.addAll(order.getOrderWorkList());
                }

                mAdapter.notifyDataSetChanged();

                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    // 确认收货
    private void updateOrderFinishedRequest(int orderID) {
        GlobalRestful.getInstance().UpdateOrderFinished(orderID, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                getUserOrderListRequest();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region ADAPTER
    public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_order, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyOrdersAdapter.ViewHolder holder, int position) {
            OrderWork orderWork = mOrderWorkArrayList.get(position);
            OrderInfo orderInfo = mOrderInfoHashMap.get(orderWork.getOrderID());

            holder.viewDataBinding.setHandlers(holder);
            holder.viewDataBinding.setOrderInfo(orderInfo);
            holder.viewDataBinding.setOrderWork(orderWork);
            holder.viewDataBinding.setShowHeader(isShowHeader(position));
            holder.viewDataBinding.setShowFooter(isShowFooter(position));

            OrderStatus orderStatus = OrderStatus.fromValue(orderInfo.getOrderStatus());

            holder.viewDataBinding.btnOperation.setVisibility(View.VISIBLE);
            switch (orderStatus) {
                case Paid:      // 已付款
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.already_paid);
                    holder.viewDataBinding.btnOperation.setText(R.string.apply_refund);
                    break;
                case Delivered: // 已发货
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.already_delivered);
                    holder.viewDataBinding.btnOperation.setText(R.string.confirm_receive);
                    break;
                case Finished:  // 已完成
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.already_finished);
                    holder.viewDataBinding.btnOperation.setVisibility(View.GONE);
                    break;
                case Refunding: // 退款中
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.refunding);
                    holder.viewDataBinding.btnOperation.setVisibility(View.GONE);
                    break;
                case Refunded:  // 已退款
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.already_refunded);
                    holder.viewDataBinding.btnOperation.setVisibility(View.GONE);
                    break;
                case RefundFail: // 退款失败
                    holder.viewDataBinding.tvOrderStatus.setText(R.string.refundFail);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mOrderWorkArrayList.size();
        }

        private boolean isShowHeader(int position) {
            if (position == 0) {
                return true;
            } else {
                OrderWork currentOrderWork = mOrderWorkArrayList.get(position);
                OrderWork previousOrderWork = mOrderWorkArrayList.get(position - 1);

                if (currentOrderWork.getOrderID() != previousOrderWork.getOrderID()) {
                    return true;
                }
            }
            return false;
        }

        private boolean isShowFooter(int position) {
            if (position == getItemCount() - 1) {
                return true;
            } else {
                OrderWork currentOrderWork = mOrderWorkArrayList.get(position);
                OrderWork previousOrderWork = mOrderWorkArrayList.get(position + 1);

                if (currentOrderWork.getOrderID() != previousOrderWork.getOrderID()) {
                    return true;
                }
            }
            return false;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemMyOrderBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            //region ACTION
            public void btnOperationAction(View view) {
                OrderWork orderWork = mOrderWorkArrayList.get(getAdapterPosition());
                OrderInfo orderInfo = mOrderInfoHashMap.get(orderWork.getOrderID());

                OrderStatus orderStatus = OrderStatus.fromValue(orderInfo.getOrderStatus());

                switch (orderStatus) {
                    case Paid:      // 已付款
                        Intent intent = new Intent(MyOrdersActivity.this, ApplyRefundActivity.class);
                        intent.putExtra(Constants.KEY_ORDER_INFO, orderInfo);
                        startActivity(intent);
                        break;
                    case Delivered: // 已发货
                        updateOrderFinishedRequest(orderInfo.getOrderID());
                        break;
                    case Finished:  // 已完成
                        break;
                    case Refunding: // 退款中
                        break;
                    case Refunded:  // 已退款
                        break;
                    case RefundFail: // 退款失败
                        break;
                }
            }
            //endregion
        }
    }
    //endregion
}
