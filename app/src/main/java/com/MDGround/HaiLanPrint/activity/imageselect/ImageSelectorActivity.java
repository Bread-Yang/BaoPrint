package com.MDGround.HaiLanPrint.activity.imageselect;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.LocalImageListAdapter;
import com.MDGround.HaiLanPrint.databinding.ActivityImageSelectorBinding;
import com.MDGround.HaiLanPrint.models.LocalMedia;
import com.MDGround.HaiLanPrint.models.LocalMediaFolder;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.LocalMediaLoader;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
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
public class ImageSelectorActivity extends ToolbarActivity<ActivityImageSelectorBinding> {

    private boolean mIsShared;

    private int mCountPerLine = 3; // 每行显示3个

    private LocalImageListAdapter imageAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_image_selector;
    }

    @Override
    protected void initData() {
        mDataBinding.recyclerView.setHasFixedSize(true);
        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        imageAdapter = new LocalImageListAdapter(this, 1000, LocalImageListAdapter.MODE_MULTIPLE, false, false);
        mDataBinding.recyclerView.setAdapter(imageAdapter);

        new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                imageAdapter.bindImages(folders.get(0).getImages());
            }
        });
    }

    @Override
    protected void setListener() {

    }

    private void uploadImageReuqest(final int upload_image_index) {
        if (upload_image_index < imageAdapter.getSelectedImages().size()) {
            LocalMedia localMedia = imageAdapter.getSelectedImages().get(upload_image_index);

            File file = new File(localMedia.getImageLocalPath());

            FileRestful.getInstance().UploadCloudPhoto(mIsShared, file, new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    int nextUploadIndex = upload_image_index + 1;
                    uploadImageReuqest(nextUploadIndex);
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    KLog.e("上传图片失败");
                    KLog.e("失败原因 : " + t);
                }
            });
        } else {
            ViewUtils.toast("上传图片成功");
            ViewUtils.dismiss();
            mDataBinding.btnUpload.setEnabled(true);
        }
    }

    public void selectAllAction(View view) {

    }

    public void uploadAction(View view) {
        List<LocalMedia> selectImages = imageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        ViewUtils.loading(this);
        mDataBinding.btnUpload.setEnabled(false);
        uploadImageReuqest(0);
    }

}
