package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.ChooseCouponActivity;
import com.MDGround.HaiLanPrint.activity.deliveryaddress.ChooseDeliveryAddressActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.enumobject.PayType;
import com.MDGround.HaiLanPrint.enumobject.SettingType;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.Coupon;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.SystemSetting;
import com.MDGround.HaiLanPrint.models.Template;
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

import static com.MDGround.HaiLanPrint.application.MDGroundApplication.mChoosedTemplate;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PaymentPreviewActivity extends ToolbarActivity<ActivityPaymentPreviewBinding> {

    private final int REQEUST_CODE_SELECT_DELIVERY_ADDRESS = 0x11;
    private final int REQEUST_CODE_SELECT_COUPON = 0x12;

    private OrderWork mOrderWork;

    private DeliveryAddress mDeliveryAddress;

    public ArrayList<UserIntegral> mUserCreditArrayList = new ArrayList<>();

    private ArrayList<Coupon> mAvailableCouponArrayList = new ArrayList<>();

    private SystemSetting mSystemSetting;

    private Coupon mSelectedCoupon;

    private int mUnitFee, mAmountFee, mCouponFee, mCredit, mReceivableFee, mFreightFee;

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
        mOrderWork = MDGroundApplication.mOrderutUtils.getmOrderWork();

        mDataBinding.setOrderWork(mOrderWork);

        Measurement measurement = MDGroundApplication.mChoosedMeasurement;

        Template template = mChoosedTemplate;

        int amountFee = getAmountFee();
        mDataBinding.tvAmount.setText(getString(R.string.yuan_amount, StringUtil.toYuanWithoutUnit(amountFee)));

        int receivableFee = getReceivableFee();
        mDataBinding.tvReceivable.setText(getString(R.string.receivables, StringUtil.toYuanWithoutUnit(receivableFee)));

        String showProductDetail = null;
        switch (MDGroundApplication.mChoosedProductType) {
            case PrintPhoto:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + " " + mOrderWork.getWorkMaterial() + ")";
                break;
            case Postcard:
                showProductDetail = mOrderWork.getTypeName();
                break;
            case MagazineAlbum:
                break;
            case ArtAlbum:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + " " + template.getPageCount() + "P)";
                break;
            case PictureFrame:
                break;
            case Calendar:
                showProductDetail = measurement.getTitle();
                break;
            case PhoneShell:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + template.getTemplateName() + ")";
                break;
            case Poker:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
                break;
            case Puzzle:
                showProductDetail = mOrderWork.getTypeName();
                break;
            case MagicCup:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
                break;
            case LOMOCard:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + ")";
                break;
            case Engraving:
                showProductDetail = mOrderWork.getWorkMaterial() + mOrderWork.getTypeName();
                break;
        }
        mDataBinding.tvProductType.setText(showProductDetail);
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

    private int getAmountFee() {
        int amountFee = 0;
        switch (MDGroundApplication.mChoosedProductType) {
            case PrintPhoto:
            case PictureFrame:
            case Engraving:
                return mOrderWork.getPrice() * mOrderWork.getPhotoCount();
            case Postcard:
            case MagazineAlbum:
            case ArtAlbum:
            case Calendar:
            case PhoneShell:
            case Poker:
            case Puzzle:
            case MagicCup:
            case LOMOCard:
                return mOrderWork.getPrice();
        }
        return 0;
    }

    private int getCreditFee() {
        int creditFee = 0;
        if (mSystemSetting != null) {
            creditFee = mCredit * mSystemSetting.getValue();
        }
        return creditFee;
    }

    private int getReceivableFee() {
        int amountFee = getAmountFee() - mCouponFee - getCreditFee() + mFreightFee;
        if (amountFee < 0) {
            amountFee = 0;
        }
        return amountFee;
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

                        Location province = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getProvinceID());
                        Location city = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getCityID());
                        Location county = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getDistrictID());

                        mDataBinding.tvAddress.setText(province.getLocationName() + city.getLocationName() + county.getLocationName() + mDeliveryAddress.getStreet());

                        mDataBinding.tvChooseFirst.setVisibility(View.GONE);
                        mDataBinding.tvName.setVisibility(View.VISIBLE);
                        mDataBinding.tvPhone.setVisibility(View.VISIBLE);
                        mDataBinding.tvAddress.setVisibility(View.VISIBLE);
                        mDataBinding.ivDeliveryAddress.setVisibility(View.VISIBLE);
                    }

                    break;
                case REQEUST_CODE_SELECT_COUPON:
                    mSelectedCoupon = data.getParcelableExtra(Constants.KEY_SELECTED_COUPON);
                    mDataBinding.tvCouponYuan.setText(getString(R.string.offset_fee, StringUtil.toYuanWithoutUnit(mSelectedCoupon.getPrice())));

                    break;
            }
        }
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
        startActivityForResult(intent, REQEUST_CODE_SELECT_COUPON);
    }

    public void payAction(View view) {
        if (mDeliveryAddress == null) {
            ViewUtils.toast(R.string.add_address_first);
            return;
        }
        PayType payType = PayType.Alipay;
        if (mDataBinding.rgPayment.getCheckedRadioButtonId() == R.id.rbWechatPay) {
            payType = PayType.WeChat;
        }
        MDGroundApplication.mOrderutUtils.updateOrderPrepayRequest(PaymentPreviewActivity.this,
                mDeliveryAddress, payType, getAmountFee(), getReceivableFee());
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

                    if (isAvailable && coupon.getPriceLimit() <= getAmountFee()) {
                        mAvailableCouponArrayList.add(coupon);
                    }
                }
                mDataBinding.tvAvailableCoupon.setText(getString(R.string.available_num, mAvailableCouponArrayList.size()));
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
    //endregion
}
