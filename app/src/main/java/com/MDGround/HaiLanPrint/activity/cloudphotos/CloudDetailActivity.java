package com.MDGround.HaiLanPrint.activity.cloudphotos;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.imageselect.ImageSelectorActivity;
import com.MDGround.HaiLanPrint.adapter.CloudImageListAdapter;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityCloudDetailBinding;
import com.MDGround.HaiLanPrint.models.CloudImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/11/16.
 */
public class CloudDetailActivity extends ToolbarActivity<ActivityCloudDetailBinding> {

    private int mCountPerLine = 3; // 每行显示3个

    private CloudImageListAdapter mImageAdapter;

    private ArrayList<CloudImage> mImagesList = new ArrayList<CloudImage>();

    private CloudImage mCloudImage;

    private int mPageIndex;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_cloud_detail;
    }

    @Override
    protected void initData() {
        mCloudImage = getIntent().getParcelableExtra(Constants.KEY_CLOUD_IMAGE);

        if (mCloudImage.isShared()) {
            mDataBinding.lltOperation.setVisibility(View.GONE);
            tvRight.setText(R.string.forward);
        } else {
            tvRight.setText(R.string.edit);
        }

        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mImageAdapter = new CloudImageListAdapter(this, 1000, CloudImageListAdapter.MODE_MULTIPLE, false, false);
        mDataBinding.recyclerView.setAdapter(mImageAdapter);

        loadImageRequest();
    }

    @Override
    protected void setListener() {
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = tvRight.getText().toString();
                if (mCloudImage.isShared()) {
                    if (btnText.equals(getString(R.string.forward))) {
                        mDataBinding.btnOperation.setText(R.string.cancel);
                        mDataBinding.lltOperation.setVisibility(View.VISIBLE);
                    } else {
                        mDataBinding.btnOperation.setText(R.string.forward);
                        mDataBinding.lltOperation.setVisibility(View.GONE);
                    }
                } else {
                    if (btnText.equals(getString(R.string.finish))) {
                        mDataBinding.cbSelectAll.setVisibility(View.GONE);

                        mDataBinding.btnOperation.setText(R.string.upload);
                    } else {
                        mDataBinding.cbSelectAll.setVisibility(View.VISIBLE);

                        mDataBinding.btnOperation.setText(R.string.delete);
                    }
                }
            }
        });

        mDataBinding.btnOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = mDataBinding.btnOperation.getText().toString();
                if (mCloudImage.isShared()) {
                    transferImageRequest();
                } else {
                    if (btnText.equals(getString(R.string.upload))) {
                        toImageSelectActivity();
                    } else {
                        deleteImageRequest();
                    }
                }
            }
        });
    }

    private void loadImageRequest() {
        GlobalRestful.getInstance().GetCloudPhoto(mPageIndex, mCloudImage.isShared(), new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mPageIndex++;

                ArrayList<CloudImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<CloudImage>>() {
                });

                mImagesList.addAll(tempImagesList);
                mImageAdapter.bindImages(mImagesList);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void deleteImageRequest() {
        final List<CloudImage> selectImages = mImageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        ArrayList<Integer> autoIDList = new ArrayList<>();
        for (CloudImage cloudImage : selectImages) {
            autoIDList.add(cloudImage.getAutoID());
        }

        ViewUtils.loading(this);

        GlobalRestful.getInstance().DeleteCloudPhoto(autoIDList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                for (CloudImage selectImage : selectImages) {
                    for (CloudImage cloudImage : mImagesList) {
                        if (selectImage.getAutoID() == cloudImage.getAutoID()) {
                            mImagesList.remove(cloudImage);
                            break;
                        }
                    }
                }

                mImageAdapter.notifyDataSetChanged();

                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void transferImageRequest() {
        final List<CloudImage> selectImages = mImageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        ArrayList<Integer> autoIDList = new ArrayList<>();
        for (CloudImage cloudImage : selectImages) {
            autoIDList.add(cloudImage.getAutoID());
        }

        ViewUtils.loading(this);

        GlobalRestful.getInstance().TransferCloudPhoto(false, autoIDList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.toast("转存成功");

                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void toImageSelectActivity() {
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        startActivity(intent);
    }

    public void selectAllAction(View view) {

    }
}
