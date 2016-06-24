package com.MDGround.HaiLanPrint.activity.engraving;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityEngravingChoosePaperNumBinding;
import com.MDGround.HaiLanPrint.databinding.ItemEngravingChoosePagerNumBinding;
import com.MDGround.HaiLanPrint.enumobject.ProductMaterial;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by yoghourt on 5/11/16.
 */
public class EngravingChoosePaperNumActivity extends ToolbarActivity<ActivityEngravingChoosePaperNumBinding> {

    private EngravingChoosePaperNumAdapter mAdapter;

    private ArrayList<Measurement> mSpecList = new ArrayList<Measurement>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_engraving_choose_paper_num;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new EngravingChoosePaperNumAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mDataBinding.rgMaterial.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbCrystal:
                        mDataBinding.tvPaperQuality.setText(R.string.crystal_paper_quality);
                        break;
                    case R.id.rbYogon:
                        mDataBinding.tvPaperQuality.setText(R.string.yogon_paper_quality);
                        break;
                }
            }
        });
    }

    //region ACTION
    public void nextStepAction(View view) {
        ViewUtils.loading(this);

        String workMaterial = null;
        if (mDataBinding.rbCrystal.isChecked()) {
            workMaterial = ProductMaterial.Engraving_Crystal.getText();
        } else {
            workMaterial = ProductMaterial.Engraving_Yogon.getText();
        }

        MDGroundApplication.mOrderutUtils = new OrderUtils(this, false,
                1, MDGroundApplication.mInstance.getChoosedMeasurement().getPrice(), null, workMaterial, null);
        MDGroundApplication.mOrderutUtils.uploadImageRequest(this, 0);
    }
    //endregion

    //region ADAPTER
    public class EngravingChoosePaperNumAdapter extends RecyclerView.Adapter<EngravingChoosePaperNumAdapter.BindingHolder> {

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_engraving_choose_pager_num, parent, false);
            BindingHolder holder = new BindingHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            holder.viewDataBinding.setImage(SelectImageUtils.mAlreadySelectImage.get(position));
            holder.viewDataBinding.setHandlers(holder);
        }

        @Override
        public int getItemCount() {
            return SelectImageUtils.mAlreadySelectImage.size();
        }

        public class BindingHolder extends RecyclerView.ViewHolder {

            public ItemEngravingChoosePagerNumBinding viewDataBinding;

            public BindingHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void addPrintNumAction(View view) {
                int position = getAdapterPosition();

                MDImage mdImage = SelectImageUtils.mAlreadySelectImage.get(position);
                int photoCount = mdImage.getPhotoCount();

                if (photoCount == 1) {
                    viewDataBinding.ivMinus.setImageResource(R.drawable.btn_optionbox_reduce_sel);
                }
                viewDataBinding.ivMinus.setEnabled(true);

                mdImage.setPhotoCount(++photoCount);
            }

            public void minusPrintNumAction(View view) {
                int position = getAdapterPosition();

                MDImage mdImage = SelectImageUtils.mAlreadySelectImage.get(position);
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
