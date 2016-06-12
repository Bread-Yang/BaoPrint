package com.MDGround.HaiLanPrint.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.ItemTemplateImageBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.socks.library.KLog;

import static com.MDGround.HaiLanPrint.utils.SelectImageUtil.mTemplateImage;

/**
 * Created by yoghourt on 5/17/16.
 */
public class TemplageImageAdapter extends RecyclerView.Adapter<TemplageImageAdapter.ViewHolder> {

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

            KLog.e("position : " + position);

            if (position < mTemplateImage.size()) {
                MDImage mdImage = mTemplateImage.get(position);

                if (onSelectImageLisenter != null) {
                    onSelectImageLisenter.selectImage(position, mdImage);
                }
            }
        }
    }
}