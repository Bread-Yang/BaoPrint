package com.MDGround.HaiLanPrint.activity.photoprint;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.payment.PaymentPreviewActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPrintPhotoChoosePaperNumBinding;
import com.MDGround.HaiLanPrint.databinding.ItemPrintPhotoChoosePagerNumBinding;
import com.MDGround.HaiLanPrint.enumobject.ProductMaterial;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.OrderWork;
import com.MDGround.HaiLanPrint.models.OrderWorkPhoto;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/11/16.
 */
public class PrintPhotoChoosePaperNumActivity extends ToolbarActivity<ActivityPrintPhotoChoosePaperNumBinding> {

    private PrintPhotoChoosePaperNumAdapter mAdapter;

    private ArrayList<Measurement> mSpecList = new ArrayList<Measurement>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_print_photo_choose_paper_num;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {

        // 讲所有选中图片的数量设为1
        for (MDImage mdImage : SelectImageUtil.mAlreadySelectImage) {
            mdImage.setPhotoCount(1);
        }

        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new PrintPhotoChoosePaperNumAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mDataBinding.rgPaper.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGlossyPaper:
                        mDataBinding.tvPaperQuality.setText(R.string.glossy_paper_quality);
                        break;
                    case R.id.rbMattePaper:
                        mDataBinding.tvPaperQuality.setText(R.string.matte_paper_quality);
                        break;
                }
            }
        });
    }

    //region ACTION
    public void nextStepAction(View view) {
        ViewUtils.loading(this);
        // 先上传所有图片
        uploadImageRequest(0);
    }
    //endregion

    //region REQUEST
    private void uploadImageRequest(final int upload_image_index) {
        if (upload_image_index < SelectImageUtil.mAlreadySelectImage.size()) {
            final MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(upload_image_index);

            final int nextUploadIndex = upload_image_index + 1;

            if (mdImage.getImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getImageLocalPath())) { // 本地图片
                File file = new File(mdImage.getImageLocalPath());

                // 上传本地照片
                FileRestful.getInstance().UploadCloudPhoto(false, file, null, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        final MDImage responseImage = response.body().getContent(MDImage.class);
                        responseImage.setPhotoCount(mdImage.getPhotoCount());

                        if (mdImage.getSyntheticImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getSyntheticImageLocalPath())) { // 合成图片
                            File syntheticFile = new File(mdImage.getSyntheticImageLocalPath());

                            // 上传合成图片
                            FileRestful.getInstance().UploadCloudPhoto(false, syntheticFile, null, new Callback<ResponseData>() {
                                @Override
                                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                    MDImage responseSyntheticImage = response.body().getContent(MDImage.class);

                                    responseImage.setSyntheticPhotoID(responseSyntheticImage.getPhotoID());
                                    responseImage.setSyntheticPhotoSID(responseSyntheticImage.getPhotoSID());

                                    SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
                                    uploadImageRequest(nextUploadIndex);
                                }

                                @Override
                                public void onFailure(Call<ResponseData> call, Throwable t) {

                                }
                            });
                        } else {
                            SelectImageUtil.mAlreadySelectImage.set(upload_image_index, responseImage);
                            uploadImageRequest(nextUploadIndex);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            } else {
                uploadImageRequest(nextUploadIndex);
            }
        } else {
            // 全部图片上传完之后,生成订单
            saveOrderRequest();
        }
    }

    private void saveOrderRequest() {
        GlobalRestful.getInstance().SaveOrder(0, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                int orderID = 0;

                try {
                    JSONObject jsonObject = new JSONObject(response.body().Content);

                    orderID = jsonObject.getInt("OrderID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                saveOrderWorkRequest(orderID);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void saveOrderWorkRequest(int orderID) {
        OrderWork orderWork = new OrderWork();
        orderWork.setOrderID(orderID);
        orderWork.setTypeID(MDGroundApplication.mChoosedMeasurement.getTypeID()); //作品类型（getPhotoType接口返回的TypeID）
        orderWork.setTypeName(MDGroundApplication.mChoosedMeasurement.getTitle()); //Title（getPhotoType接口返回的Title）
        orderWork.setPhotoCover(SelectImageUtil.mAlreadySelectImage.get(0).getPhotoSID()); //封面，第一张照片的缩略图ID
        orderWork.setPhotoCount(SelectImageUtil.mAlreadySelectImage.size());
        orderWork.setOrderCount(SelectImageUtil.getOrderCount());

        String workMaterial = null;
        if (mDataBinding.rbGlossyPaper.isChecked()) {
            workMaterial = ProductMaterial.PrintPhoto_Glossy.getText();
        } else {
            workMaterial = ProductMaterial.PrintPhoto_Matte.getText();
        }
        orderWork.setWorkMaterial(workMaterial);
        orderWork.setCreateTime(DateUtils.getServerDateStringByDate(new Date()));

        GlobalRestful.getInstance().SaveOrderWork(orderWork, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                int workOID = 0;

                try {
                    JSONObject jsonObject = new JSONObject(response.body().Content);

                    workOID = jsonObject.getInt("WorkOID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                saveOrderPhotoListAction(workOID);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void saveOrderPhotoListAction(int workOID) {
        List<OrderWorkPhoto> orderWorkPhotoList = new ArrayList<>();

        for (int i = 0; i < SelectImageUtil.mAlreadySelectImage.size(); i++) {
            MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(i);
            OrderWorkPhoto orderWorkPhoto = new OrderWorkPhoto();
            orderWorkPhoto.setAutoID(mdImage.getAutoID());
            orderWorkPhoto.setWorkOID(workOID);
            orderWorkPhoto.setPhoto1ID(mdImage.getPhotoID());
            orderWorkPhoto.setPhoto1SID(mdImage.getPhotoSID());
            int index = i + 1;
            orderWorkPhoto.setPhotoIndex(index);

            orderWorkPhotoList.add(orderWorkPhoto);
        }

        GlobalRestful.getInstance().SaveOrderPhotoList(orderWorkPhotoList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                toPaymentPreviewActivity();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }
    //endregion

    private void toPaymentPreviewActivity() {
        Intent intent = new Intent(this, PaymentPreviewActivity.class);
        startActivity(intent);
    }

    //region ADAPTER
    public class PrintPhotoChoosePaperNumAdapter extends RecyclerView.Adapter<PrintPhotoChoosePaperNumAdapter.BindingHolder> {

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_print_photo_choose_pager_num, parent, false);
            BindingHolder holder = new BindingHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            holder.viewDataBinding.setImage(SelectImageUtil.mAlreadySelectImage.get(position));
            holder.viewDataBinding.setHandlers(holder);
        }

        @Override
        public int getItemCount() {
            return SelectImageUtil.mAlreadySelectImage.size();
        }

        public class BindingHolder extends RecyclerView.ViewHolder {

            public ItemPrintPhotoChoosePagerNumBinding viewDataBinding;

            public BindingHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void addPrintNumAction(View view) {
                int position = getAdapterPosition();

                MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(position);
                int photoCount = mdImage.getPhotoCount();

                if (photoCount == 1) {
                    viewDataBinding.ivMinus.setImageResource(R.drawable.btn_optionbox_reduce_sel);
                }
                viewDataBinding.ivMinus.setEnabled(true);

                mdImage.setPhotoCount(++photoCount);
            }

            public void minusPrintNumAction(View view) {
                int position = getAdapterPosition();

                MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(position);
                int photoCount = mdImage.getPhotoCount();

                if (photoCount == 1) {
                    return;
                }

                if (photoCount == 2) {
                    view.setEnabled(false);
                    viewDataBinding.ivMinus.setImageResource(R.drawable.btn_optionbox_reduce_nor);
                }

                mdImage.setPhotoCount(--photoCount);

            }
        }
    }
    //endregion
}
