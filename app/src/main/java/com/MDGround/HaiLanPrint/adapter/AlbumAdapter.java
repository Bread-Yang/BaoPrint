package com.MDGround.HaiLanPrint.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.BR;
import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.ItemAlbumBinding;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.utils.NavUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoghourt on 16/5/16.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private ProductType mProductType;
    private List<Album> mAlbumsList = new ArrayList<>();

    public void bindAlbum(List<Album> albumList){
        this.mAlbumsList = albumList;
        notifyDataSetChanged();
    }

    public AlbumAdapter(ProductType productType) {
        this.mProductType = productType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Album album = mAlbumsList.get(position);

        holder.viewDataBinding.setVariable(BR.album, album);
        holder.viewDataBinding.setVariable(BR.viewHolder, holder);
    }

    @Override
    public int getItemCount() {
        return mAlbumsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ItemAlbumBinding viewDataBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }

        public void toSelectImageActivityAction(View view) {
            Album album = mAlbumsList.get(getAdapterPosition());

            NavUtils.toSelectImageActivity(view.getContext(), album, mProductType);
        }
    }
}
