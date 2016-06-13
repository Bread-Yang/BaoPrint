package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.ChooseCouponActivity;
import com.MDGround.HaiLanPrint.activity.deliveryaddress.ChooseDeliveryAddressActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.models.UserIntegralList;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
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

    private OrderWork mOrderWork;

    private DeliveryAddress mDeliveryAddress;

    public ArrayList<UserIntegralList> mUserCreditArrayList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment_preview;
    }

    @Override
    protected void initData() {
        mOrderWork = MDGroundApplication.mOrderutUtils.getmOrderWork();

        mDataBinding.setOrderWork(mOrderWork);

        getUserIntegralInfoRequest();

        Measurement measurement = MDGroundApplication.mChoosedMeasurement;

        Template template = MDGroundApplication.mChoosedTemplate;

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
            case ArtAlbum:
                showProductDetail = mOrderWork.getTypeName() + " (" + measurement.getTitle() + " " + template.getPageCount() + "P)";
                break;
            case Calendar:
                showProductDetail = measurement.getTitle();
                break;
            case PhoneShell:

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

    private int getReceivableFee() {
        int amountFee = getAmountFee();
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
        startActivity(intent);
    }

    public void payAction(View view) {
        if (mDeliveryAddress == null) {
            ViewUtils.toast(R.string.add_address_first);
            return;
        }
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        startActivity(intent);
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
                    String UserIntegralList = jsonObject.getString("UserIntegralList");
                    mUserCreditArrayList = StringUtil.getInstanceByJsonString(UserIntegralList, new TypeToken<ArrayList<UserIntegralList>>() {
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
    //endregion
}
