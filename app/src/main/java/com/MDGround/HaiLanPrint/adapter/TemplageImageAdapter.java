package com.MDGround.HaiLanPrint.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ItemTemplateImageBinding;
import com.MDGround.HaiLanPrint.models.MDImage;

import static com.MDGround.HaiLanPrint.utils.SelectImageUtil.mTemplateImage;

/**
 * Created by yoghourt on 5/17/16.
 */
public class TemplageImageAdapter extends RecyclerView.Adapter<TemplageImageAdapter.ViewHolder> {

    private int mCurrentSelectedIndex = 0;

    public interface onSelectImageLisenter {
        void selectImage(int position, MDImage mdImage);
    }

    private onSelectImageLisenter onSelectImageLisenter;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_template_image, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewDataBinding.setImage(mTemplateImage.get(position));
        holder.viewDataBinding.setViewHolder(holder);
        holder.viewDataBinding.setIsSelected(position == mCurrentSelectedIndex);

        String indexString = null;
        if (MDGroundApplication.mChoosedProductType == ProductType.LOMOCard) {
            indexString = String.valueOf(position + 1);
        } else {
            if (position == 0) {
                indexString = holder.viewDataBinding.tvIndex.getContext().getString(R.string.cover);
            } else {
                indexString = String.valueOf(position);
            }
        }
        holder.viewDataBinding.tvIndex.setText(indexString);
    }

    @Override
    public int getItemCount() {
        return mTemplateImage.size();
    }

    public TemplageImageAdapter.onSelectImageLisenter getOnSelectImageLisenter() {
        return onSelectImageLisenter;
    }

    public void setOnSelectImageLisenter(TemplageImageAdapter.onSelectImageLisenter onSelectImageLisenter) {
        this.onSelectImageLisenter = onSelectImageLisenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTemplateImageBinding viewDataBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }

        public void selectImageAction(View view) {
            int position = getAdapterPosition();
            int lastSelectIndex = mCurrentSelectedIndex;
            mCurrentSelectedIndex = position;
            notifyItemChanged(lastSelectIndex);
            notifyItemChanged(mCurrentSelectedIndex);

            if (position < mTemplateImage.size()) {
                MDImage mdImage = mTemplateImage.get(position);

                if (onSelectImageLisenter != null) {
                    onSelectImageLisenter.selectImage(/**/position, mdImage);
                }
            }
        }
    }
}