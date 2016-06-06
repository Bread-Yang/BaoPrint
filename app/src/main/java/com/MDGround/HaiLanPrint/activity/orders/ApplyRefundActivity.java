package com.MDGround.HaiLanPrint.activity.orders;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityApplyRefundBinding;
import com.MDGround.HaiLanPrint.databinding.ItemApplyRefundBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.SelectSingleImageDialog;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.socks.library.KLog;

import java.util.ArrayList;

/**
 * Created by yoghourt on 5/30/16.
 */

public class ApplyRefundActivity extends ToolbarActivity<ActivityApplyRefundBinding> {

    private ApplyRefundAdapter mAdapter;

    private SelectSingleImageDialog mSelectSingleImageDialog;

    private ArrayList<MDImage> mUploadImageArrayList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_apply_refund;
    }

    @Override
    protected void initData() {
        mSelectSingleImageDialog = new SelectSingleImageDialog(this);

        mDataBinding.recyclerView.setHasFixedSize(true);
        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ViewUtils.dp2px(2), false));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new ApplyRefundAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SelectSingleImageDialog.PHOTO_REQUEST_GALLERY) {// 从相册返回的数据
                Uri uri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                KLog.e("picturePath : " + picturePath);
            } else if (requestCode == SelectSingleImageDialog.PHOTO_REQUEST_CAREMA) {// 从相机返回的数据
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
            }
        }
    }

    //region SERVER
    //endregion

    //region ADAPTER
    public class ApplyRefundAdapter extends RecyclerView.Adapter<ApplyRefundAdapter.ViewHolder> {

        @Override
        public ApplyRefundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_apply_refund, parent, false);
            ApplyRefundAdapter.ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ApplyRefundAdapter.ViewHolder holder, int position) {
            if (mUploadImageArrayList.size() == 0 || (position >= mUploadImageArrayList.size() && mUploadImageArrayList.size() != 3)) {
                holder.viewDataBinding.ivImage.setImageResource(R.drawable.layerlist_camara_placeholder);
            } else {
                holder.viewDataBinding.setImage(mUploadImageArrayList.get(position));
            }
            holder.viewDataBinding.setHandlers(holder);
        }

        @Override
        public int getItemCount() {
            if (mUploadImageArrayList.size() < 3) {
                return mUploadImageArrayList.size() + 1;
            }
            return 3;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ItemApplyRefundBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void onImageLayoutClickAction(View view) {
                int position = getAdapterPosition();
                if (mUploadImageArrayList.size() != 3 && position == getItemCount() - 1) {
                    mSelectSingleImageDialog.show();
                }
            }
        }
    }
    //endregion
}
