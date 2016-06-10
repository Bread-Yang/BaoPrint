package com.MDGround.HaiLanPrint.activity.cloudphotos;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.uploadimage.UploadImageActivity;
import com.MDGround.HaiLanPrint.adapter.ChooseImageListAdapter;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityCloudDetailBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.malinskiy.superrecyclerview.OnMoreListener;

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

    private ChooseImageListAdapter mImageAdapter;

    private ArrayList<MDImage> mImagesList = new ArrayList<MDImage>();

    private MDImage mImage;

    private int mPageIndex;

    private boolean isManualChangeState;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_cloud_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImageRequest();
    }

    @Override
    protected void initData() {
        mImage = getIntent().getParcelableExtra(Constants.KEY_CLOUD_IMAGE);

        if (mImage.isShared()) {
            mDataBinding.lltOperation.setVisibility(View.GONE);
            mDataBinding.btnOperation.setText(R.string.forward);
            tvRight.setText(R.string.forward);
            tvTitle.setText(R.string.share_album);
        } else {
            tvRight.setText(R.string.edit);
            tvTitle.setText(R.string.personal_album);
        }

        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), true));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mImageAdapter = new ChooseImageListAdapter(this, Integer.MAX_VALUE, false, !mImage.isShared());
        mDataBinding.recyclerView.setAdapter(mImageAdapter);

    }

    @Override
    protected void setListener() {
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = tvRight.getText().toString();
                if (mImage.isShared()) {
                    if (btnText.equals(getString(R.string.forward))) {
                        tvRight.setText(R.string.cancel);
                        mImageAdapter.setSelectable(true);
                        mDataBinding.lltOperation.setVisibility(View.VISIBLE);
                        mDataBinding.cbSelectAll.setVisibility(View.VISIBLE);
                    } else {
                        tvRight.setText(R.string.forward);
                        mImageAdapter.setSelectable(false);
                        mDataBinding.lltOperation.setVisibility(View.GONE);
                        mDataBinding.cbSelectAll.setVisibility(View.GONE);
                    }
                } else {
                    if (btnText.equals(getString(R.string.cancel))) {
                        tvRight.setText(R.string.edit);
                        mDataBinding.cbSelectAll.setVisibility(View.GONE);
                        mDataBinding.cbSelectAll.setChecked(false);
                        mImageAdapter.setSelectable(false);

                        mDataBinding.btnOperation.setText(R.string.upload_image);
                        mDataBinding.btnOperation.setBackgroundResource(R.drawable.ripple_button_right_angle_orange);
//                        mDataBinding.btnOperation.setBackgroundColor();
                    } else {
                        tvRight.setText(R.string.cancel);
                        mImageAdapter.setSelectable(true);

                        mDataBinding.cbSelectAll.setVisibility(View.VISIBLE);

                        mDataBinding.btnOperation.setText(R.string.delete);
                        mDataBinding.btnOperation.setBackgroundResource(R.drawable.ripple_button_right_angle_red);
                    }
                }
            }
        });

        mDataBinding.cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isManualChangeState) {
                    mImageAdapter.selectAllImage(isChecked);
                }
            }
        });

        mDataBinding.recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                loadImageRequest();
            }
        }, Constants.ITEM_LEFT_TO_LOAD_MORE);

        mImageAdapter.setOnImageSelectChangedListener(new ChooseImageListAdapter.OnImageSelectChangedListener() {
            @Override
            public void onSelectImage(MDImage selectImage, int selectNum) {
                changeTextNum(selectNum);
            }

            @Override
            public void onUnSelectImage(MDImage unselectImage, int selectNum) {
                changeTextNum(selectNum);
            }

            @Override
            public void onTakePhoto() {

            }

            @Override
            public void onPictureClick(MDImage media, int position) {

            }

            @Override
            public void onIsSelectAllImage(boolean isSelectAll) {
                isManualChangeState = true;
                mDataBinding.cbSelectAll.setChecked(isSelectAll);
                isManualChangeState = false;
            }
        });
    }

    private void changeTextNum(int selectNum) {
        if (selectNum != 0) {
            if (mImage.isShared()) {
                mDataBinding.btnOperation.setText(getString(R.string.forward_num, selectNum));
            } else {
                mDataBinding.btnOperation.setText(getString(R.string.delete_num, selectNum));
            }
        } else {
            mDataBinding.btnOperation.setText(getString(R.string.delete));
        }
    }

    private void toImageSelectActivity() {
        Intent intent = new Intent(this, UploadImageActivity.class);
        startActivity(intent);
    }

    //region ACTION
    public void btnOperationAction(View view) {
        String btnText = mDataBinding.btnOperation.getText().toString();
        if (mImage.isShared()) {
            transferImageRequest();
        } else {
            if (btnText.equals(getString(R.string.upload_image))) {
                toImageSelectActivity();
            } else {
                deleteImageRequest();
            }
        }
    }
    //endregion

    //region SERVER
    private void loadImageRequest() {
        GlobalRestful.getInstance().GetCloudPhoto(mPageIndex, mImage.isShared(), new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mPageIndex++;

                if (StringUtil.isEmpty(response.body().getContent())) {
                    mDataBinding.recyclerView.setLoadingMore(false);
                    mDataBinding.recyclerView.setupMoreListener(null, 0);
                } else {
                    ArrayList<MDImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });

                    mImagesList.addAll(tempImagesList);
                    mImageAdapter.bindImages(mImagesList);
                }

                mDataBinding.recyclerView.hideMoreProgress();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void deleteImageRequest() {
        final List<MDImage> selectImages = mImageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        ArrayList<Integer> autoIDList = new ArrayList<>();
        for (MDImage image : selectImages) {
            autoIDList.add(image.getAutoID());
        }

        ViewUtils.loading(this);

        GlobalRestful.getInstance().DeleteCloudPhoto(autoIDList, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                for (MDImage selectImage : selectImages) {
                    for (MDImage image : mImagesList) {
                        if (selectImage.getAutoID() == image.getAutoID()) {
                            mImagesList.remove(image);
                            break;
                        }
                    }
                }

                mDataBinding.btnOperation.setText(getString(R.string.delete));

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
        final List<MDImage> selectImages = mImageAdapter.getSelectedImages();

        if (selectImages == null || selectImages.size() == 0) {
            ViewUtils.toast(R.string.choose_photo);
            return;
        }

        ArrayList<Integer> autoIDList = new ArrayList<>();
        for (MDImage image : selectImages) {
            autoIDList.add(image.getAutoID());
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
    //endregion
}
