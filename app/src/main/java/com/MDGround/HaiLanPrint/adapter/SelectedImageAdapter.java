package com.MDGround.HaiLanPrint.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.BR;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.ItemSelectedImageBinding;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;

/**
 * Created by yoghourt on 5/17/16.
 */
public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ViewHolder> {

    public interface onDeleteImageLisenter {
        void deleteImage();
    }

    private onDeleteImageLisenter onDeleteImageLisenter;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_image, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewDataBinding.setVariable(BR.image, SelectImageUtils.mAlreadySelectImage.get(position));
        holder.viewDataBinding.setVariable(BR.viewHolder, holder);
    }

    @Override
    public int getItemCount() {
        return SelectImageUtils.mAlreadySelectImage.size();
    }

    public SelectedImageAdapter.onDeleteImageLisenter getOnDeleteImageLisenter() {
        return onDeleteImageLisenter;
    }

    public void setOnDeleteImageLisenter(SelectedImageAdapter.onDeleteImageLisenter onDeleteImageLisenter) {
        this.onDeleteImageLisenter = onDeleteImageLisenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemSelectedImageBinding viewDataBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }

        public void deleteImageAction(View view) {
            int position = getAdapterPosition();

            if (position < SelectImageUtils.mAlreadySelectImage.size()) {
                SelectImageUtils.mAlreadySelectImage.remove(position);

                notifyDataSetChanged();

                if (onDeleteImageLisenter != null) {
                    onDeleteImageLisenter.deleteImage();
                }
            }
        }
    }
}