package com.MDGround.HaiLanPrint.activity.photoprint;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPrintPhotoChoosePaperNumBinding;
import com.MDGround.HaiLanPrint.databinding.ItemPrintPhotoChoosePagerNumBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;

import java.util.ArrayList;

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

        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new PrintPhotoChoosePaperNumAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
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

            public class BindingHandlers {

                public void addPrintNumAction(View view) {
                    int position = getAdapterPosition();

                    MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(position);
                    int photoCount = mdImage.getPhotoCount();
                    mdImage.setPhotoCount(photoCount++);
                }

                public void minusPrintNumAction(View view) {
                    int position = getAdapterPosition();

                    MDImage mdImage = SelectImageUtil.mAlreadySelectImage.get(position);
                    int photoCount = mdImage.getPhotoCount();

                    if (photoCount == 1) {
                        view.setEnabled(false);
                        return;
                    }
                    mdImage.setPhotoCount(photoCount--);
                }
            }
        }
    }
    //endregion
}
