package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.ChooseCouponActivity;
import com.MDGround.HaiLanPrint.activity.deliveryaddress.ChooseDeliveryAddressActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.databinding.ItemPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.enumobject.PayType;
import com.MDGround.HaiLanPrint.enumobject.SettingType;
import com.MDGround.HaiLanPrint.models.Coupon;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.SystemSetting;
import com.MDGround.HaiLanPrint.models.UserIntegral;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PaymentPreviewActivity extends ToolbarActivity<ActivityPaymentPreviewBinding> {

    private final int REQEUST_CODE_SELECT_DELIVERY_ADDRESS = 0x11;
    private final int REQEUST_CODE_SELECT_COUPON = 0x12;

    private DeliveryAddress mDeliveryAddress;

    private ArrayList<OrderWork> mOrderWorkArrayList;

    private ArrayList<UserIntegral> mUserCreditArrayList = new ArrayList<>();

    private ArrayList<Coupon> mAvailableCouponArrayList = new ArrayList<>();

    private SystemSetting mSystemSetting;

    private Coupon mSelectedCoupon;

    private int mUnitFee, mAmountFee, mCredit, mReceivableFee, mFreightFee;

    private boolean mHadChangedOrderCount;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment_preview;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserIntegralInfoRequest();
        getUserCouponListRequest();
        getSystemSettingRequest();
    }

    @Override
    protected void initData() {
        mOrderWorkArrayList = MDGroundApplication.mOrderutUtils.mOrderWorkArrayList;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.productRecyclerView.setLayoutManager(layoutManager);

        PaymentPreviewAdapter paymentPreviewAdapter = new PaymentPreviewAdapter();
        mDataBinding.productRecyclerView.setAdapter(paymentPreviewAdapter);

        refreshDisplayFee();
    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.toMainActivity(PaymentPreviewActivity.this);
            }
        });

        mDataBinding.cbUseCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDataBinding.tvCreditYuan.setText(getString(R.string.offset_fee, StringUtil.toYuanWithoutUnit(getCreditFee())));
                } else {
                    mDataBinding.tvCreditYuan.setText(getString(R.string.offset_fee, "0.00"));
                }
            }
        });
    }

    private void setShowFee() {
        String showProductDetail = null;
//        switch (MDGroundApplication.mChoosedProductType) {
//            case PrintPhoto:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + " " + mOrderWork.getWorkMaterial() + ")";
//                break;
//            case Postcard:
//                showProductDetail = mOrderWork.getTypeName();
//                break;
//            case MagazineAlbum:
//                break;
//            case ArtAlbum:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + " " + template.getPageCount() + "P)";
//                break;
//            case PictureFrame:
//                break;
//            case Calendar:
//                showProductDetail = measurement.getTitle();
//                break;
//            case PhoneShell:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + template.getTemplateName() + ")";
//                break;
//            case Poker:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
//                break;
//            case Puzzle:
//                showProductDetail = mOrderWork.getTypeName();
//                break;
//            case MagicCup:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
//                break;
//            case LOMOCard:
//                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
//                break;
//            case Engraving:
//                showProductDetail = mOrderWork.getWorkMaterial() + mOrderWork.getTypeName();
//                break;
//        }
    }

    private int getSingleOrderWorkAmountFee(OrderWork orderWork) {
        ProductType productType = ProductType.fromValue(orderWork.getTypeID());

        switch (productType) {
            case PrintPhoto:
            case Engraving:
                return orderWork.getPrice() * orderWork.getPhotoCount();
            case Postcard:
            case MagazineAlbum:
            case ArtAlbum:
            case PictureFrame:
            case Calendar:
            case PhoneShell:
            case Poker:
            case Puzzle:
            case MagicCup:
            case LOMOCard:
                return orderWork.getPrice() * orderWork.getOrderCount();
        }
        return 0;
    }

    private int getAllOrderWorkAmountFee() {
        int amountFee = 0;
        for (OrderWork orderWork : mOrderWorkArrayList) {
            amountFee += getSingleOrderWorkAmountFee(orderWork);
        }

        return amountFee;
    }

    private int getCouponFee() {
        int couponFee = 0;
        if (mSelectedCoupon != null) {
            couponFee = mSelectedCoupon.getPrice();
        }
        return couponFee;
    }

    private int getCreditFee() {
        int creditFee = 0;
        if (mSystemSetting != null) {
            creditFee = mCredit * mSystemSetting.getValue();
        }
        return creditFee;
    }

    private int getReceivableFee() {
        int amountFee = getAllOrderWorkAmountFee() - getCouponFee() - getCreditFee() + mFreightFee;
        if (amountFee < 0) {
            amountFee = 0;
        }
        return amountFee;
    }

    private void refreshDisplayFee() {
        mHadChangedOrderCount = true;

        // 总额
        int amountFee = getAllOrderWorkAmountFee();
        mDataBinding.tvAmount.setText(getString(R.string.yuan_amount, StringUtil.toYuanWithoutUnit(amountFee)));

        int matchLimitCoupon = 0;
        for (Coupon coupon : mAvailableCouponArrayList) {
            if (coupon.getPriceLimit() <= getAllOrderWorkAmountFee()) {
                matchLimitCoupon++;
            }
        }

        mDataBinding.tvAvailableCoupon.setText(getString(R.string.available_num, matchLimitCoupon));

        // 优惠劵
        int couponFee = 0;
        if (mSelectedCoupon != null) {
            couponFee = mSelectedCoupon.getPrice();
        }
        mDataBinding.tvCouponYuan.setText(getString(R.string.offset_fee, StringUtil.toYuanWithoutUnit(couponFee)));
        mDataBinding.tvCouponFee.setText(getString(R.string.yuan_amount, StringUtil.toYuanWithoutUnit(couponFee)));

        // 应付金额
        int receivableFee = getReceivableFee();
        mDataBinding.tvReceivable.setText(getString(R.string.receivables, StringUtil.toYuanWithoutUnit(receivableFee)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQEUST_CODE_SELECT_DELIVERY_ADDRESS:
                    mDeliveryAddress = data.getParcelableExtra(Constants.KEY_DELIVERY_ADDRESS);

                    if (mDeliveryAddress != null) {
                        mDataBinding.tvName.setText(mDeliveryAddress.getReceiver());
                        mDataBinding.tvPhone.setText(mDeliveryAddress.getPhone());

                        mDataBinding.tvAddress.setText(StringUtil.getCompleteAddress(mDeliveryAddress));

                        mDataBinding.tvChooseFirst.setVisibility(View.GONE);
                        mDataBinding.tvName.setVisibility(View.VISIBLE);
                        mDataBinding.tvPhone.setVisibility(View.VISIBLE);
                        mDataBinding.tvAddress.setVisibility(View.VISIBLE);
                        mDataBinding.ivDeliveryAddress.setVisibility(View.VISIBLE);
                    }

                    break;
                case REQEUST_CODE_SELECT_COUPON:
                    mSelectedCoupon = data.getParcelableExtra(Constants.KEY_SELECTED_COUPON);

                    refreshDisplayFee();
                    break;
            }
        }
    }

    private void updateOrderPrepay() {
        PayType payType = PayType.Alipay;
        if (mDataBinding.rgPayment.getCheckedRadioButtonId() == R.id.rbWechatPay) {
            payType = PayType.WeChat;
        }

        MDGroundApplication.mOrderutUtils.updateOrderPrepayRequest(PaymentPreviewActivity.this,
                mDeliveryAddress, payType, getAllOrderWorkAmountFee(), getReceivableFee());
    }

    //region ACTION
    public void toDeliveryAddressListActivityAction(View view) {
        Intent intent = new Intent(this, ChooseDeliveryAddressActivity.class);
        intent.putExtra(Constants.KEY_DELIVERY_ADDRESS, mDeliveryAddress);
        startActivityForResult(intent, REQEUST_CODE_SELECT_DELIVERY_ADDRESS);
    }

    public void toChooseCouponActivityAction(View view) {
        Intent intent = new Intent(this, ChooseCouponActivity.class);
        intent.putExtra(Constants.KEY_SELECTED_COUPON, mSelectedCoupon);
        intent.putExtra(Constants.KEY_COUPON_LIST, mAvailableCouponArrayList);
        intent.putExtra(Constants.KEY_ORDER_AMOUNT_FEE, getAllOrderWorkAmountFee());
        startActivityForResult(intent, REQEUST_CODE_SELECT_COUPON);
    }

    public void payAction(View view) {
        if (mDeliveryAddress == null) {
            ViewUtils.toast(R.string.add_address_first);
            return;
        }

        if (mHadChangedOrderCount) {
            saveOrderWorkRequest(0);
        } else {
            updateOrderPrepay();
        }
    }

    public void itemMinusNumAction(View view) {
        int position = mDataBinding.productRecyclerView.getChildAdapterPosition(view);

        OrderWork orderWork = mOrderWorkArrayList.get(position);
        int orderCount = orderWork.getOrderCount();

        if (orderCount == 1) {
            return;
        }

        orderWork.setOrderCount(--orderCount);
    }

    public void itemAddNumAction(View view) {
        int position = mDataBinding.productRecyclerView.getChildAdapterPosition(view);

        OrderWork orderWork = mOrderWorkArrayList.get(position);
        int orderCount = orderWork.getOrderCount();

        orderWork.setOrderCount(++orderCount);
    }
    //endregion

    //region SERVER
    private void getUserIntegralInfoRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetUserIntegralInfo(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().getContent());
                    mCredit = jsonObject.getInt("TotalAmount");
                    mDataBinding.tvCredit.setText(getString(R.string.credit_total, String.valueOf(mCredit)));

                    String UserIntegralList = jsonObject.getString("UserIntegralList");
                    mUserCreditArrayList = StringUtil.getInstanceByJsonString(UserIntegralList, new TypeToken<ArrayList<UserIntegral>>() {
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void getUserCouponListRequest() {
        GlobalRestful.getInstance().GetUserCouponList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ArrayList<Coupon> allCouponArrayList = response.body().getContent(new TypeToken<ArrayList<Coupon>>() {
                });

                mAvailableCouponArrayList.clear();
                for (Coupon coupon : allCouponArrayList) {
                    // 判断优惠券是否可用条件：当前时间在ExpireTime之前
                    boolean isAvailable = DateUtils.isBeforeExpireTime(coupon.getExpireTime());

                    if (isAvailable) {
                        mAvailableCouponArrayList.add(coupon);
                    }
                }
                refreshDisplayFee();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void getSystemSettingRequest() {
        GlobalRestful.getInstance().GetSystemSetting(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ArrayList<SystemSetting> systemSettingArrayList = response.body().getContent(new TypeToken<ArrayList<SystemSetting>>() {
                });

                for (SystemSetting systemSetting : systemSettingArrayList) {
                    SettingType settingType = SettingType.fromValue(systemSetting.getSettingType());
                    if (settingType == SettingType.PayIntegralAmount) {
                        mSystemSetting = systemSetting;

                        mDataBinding.cbUseCredit.setChecked(false);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void saveOrderWorkRequest(final int saveIndex) {
        if (saveIndex < mOrderWorkArrayList.size()) {
            GlobalRestful.getInstance().SaveOrderWork(mOrderWorkArrayList.get(saveIndex), new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    int nextSaveIndex = saveIndex + 1;
                    saveOrderWorkRequest(nextSaveIndex);
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    ViewUtils.dismiss();
                }
            });
        } else {
            updateOrderPrepay();
        }
    }
    //endregion

    //region ADAPTER
    public class PaymentPreviewAdapter extends RecyclerView.Adapter<PaymentPreviewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment_preview, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            OrderWork orderWork = mOrderWorkArrayList.get(position);

            holder.viewDataBinding.setOrderWork(orderWork);

            ProductType productType = ProductType.fromValue(orderWork.getTypeID());
            switch (productType) {
                case PrintPhoto:
                case Engraving:
                    holder.viewDataBinding.rltQuantity.setVisibility(View.GONE);
                    break;
            }

            holder.viewDataBinding.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderWork orderWork = mOrderWorkArrayList.get(position);
                    int orderCount = orderWork.getOrderCount();

                    if (orderCount == 1) {
                        return;
                    }

                    orderWork.setOrderCount(--orderCount);

                    mSelectedCoupon = null;
                    refreshDisplayFee();
                }
            });

            holder.viewDataBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderWork orderWork = mOrderWorkArrayList.get(position);
                    int orderCount = orderWork.getOrderCount();

                    orderWork.setOrderCount(++orderCount);

                    mSelectedCoupon = null;
                    refreshDisplayFee();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mOrderWorkArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemPaymentPreviewBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }
        }
    }
    //endregion

}
