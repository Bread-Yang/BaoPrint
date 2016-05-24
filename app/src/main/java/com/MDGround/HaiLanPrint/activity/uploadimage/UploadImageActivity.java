package com.MDGround.HaiLanPrint.activity.uploadimage;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.ChooseImageListAdapter;
import com.MDGround.HaiLanPrint.databinding.ActivityUploadImageBinding;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.LocalMediaLoader;
import com.MDGround.HaiLanPrint.utils.ToolNetwork;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.dialog.NotifyDialog;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/12/16.
 */
public class UploadImageActivity extends ToolbarActivity<ActivityUploadImageBinding> {

    private boolean mIsShared;

    private int mCountPerLine = 3; // 每行显示3个

    private ChooseImageListAdapter mImageAdapter;

    private NotifyDialog notifyDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_upload_image;
    }

    @Override
    protected void initData() {
        mDataBinding.recyclerView.setHasFixedSize(true);
        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mImageAdapter = new ChooseImageListAdapter(this, Integer.MAX_VALUE, true);
        mDataBinding.recyclerView.setAdapter(mImageAdapter);

        new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<Album> albums) {
                mImageAdapter.bindImages(albums.get(0).getImages());
            }
        });
    }

    @Override
    protected void setListener() {
        mDataBinding.cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mImageAdapter.selectAllImage(isChecked);
            }
        });
    }

    //region ACTION
    public void uploadAction(View view) {
        List<MDImage> selectImages = mImageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        if (ToolNetwork.getInstance().isConnected()) {
            if (ToolNetwork.isWIFIConnected(this)) {
                uploadImage();
            } else {
                showUseCelluarNetworkTips();
            }
        } else {
            ViewUtils.toast(R.string.network_unavailable);
        }
    }
    //endregion

    private void showUseCelluarNetworkTips() {
        if (notifyDialog == null) {
            notifyDialog = new NotifyDialog(this);
            notifyDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {
                @Override
                public void onSureClick() {
                    notifyDialog.dismiss();
                    uploadImage();
                }
            });
        }
        notifyDialog.show();
    }

    private void uploadImage() {
        ViewUtils.loading(this);
        mDataBinding.btnUpload.setEnabled(false);
        uploadImageRequest(0);
    }

    private void uploadImageRequest(final int upload_image_index) {
        if (upload_image_index < mImageAdapter.getSelectedImages().size()) {
            MDImage localMedia = mImageAdapter.getSelectedImages().get(upload_image_index);

            File file = new File(localMedia.getImageLocalPath());

            KLog.e("localMedia.getImageLocalPath() : " + localMedia.getImageLocalPath());

            FileRestful.getInstance().UploadCloudPhoto(mIsShared, file, null, new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    int nextUploadIndex = upload_image_index + 1;
                    uploadImageRequest(nextUploadIndex);
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                }
            });
        } else {
            ViewUtils.toast("上传图片成功");
            ViewUtils.dismiss();
            mImageAdapter.selectAllImage(false);
            mDataBinding.btnUpload.setEnabled(true);
        }
    }

}
