package com.MDGround.HaiLanPrint.activity.selectimage;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.ChooseImageListAdapter;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectImageWhenEditBinding;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.NavUtils;
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
public class SelectImageWhenEditActivity extends ToolbarActivity<ActivitySelectImageWhenEditBinding> {

    private ChooseImageListAdapter mImageAdapter;

    private int mCountPerLine = 3; // 每行显示3个

    private Album mAlbum;

    private List<MDImage> mImagesList;

    private boolean mIsShared;

    private int mPageIndex;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_image_when_edit;
    }

    @Override
    protected void initData() {
        mAlbum = getIntent().getParcelableExtra(Constants.KEY_ALBUM);

        mImagesList = mAlbum.getImages();

        if (mImagesList.get(0).getPhotoSID() != 0) {
            mIsShared = mImagesList.get(0).isShared();
//            mImagesList.clear();

            mDataBinding.imageRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    loadImageRequest();
                }
            }, Constants.ITEM_LEFT_TO_LOAD_MORE);

            loadImageRequest();
        } else {
            mDataBinding.imageRecyclerView.setLoadingMore(false);
        }

        if (mImagesList.get(0).getImageLocalPath() == null
                && mImagesList.get(0).getPhotoID() == 0
                && mImagesList.get(0).getPhotoCount() == 0) {
            mImagesList.clear();
        }

        // 相册
//        mDataBinding.imageRecyclerView.setHasFixedSize(true);
        mDataBinding.imageRecyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.imageRecyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mImageAdapter = new ChooseImageListAdapter(this, 1, true, false);
        mImageAdapter.bindImages(mImagesList);
        mDataBinding.imageRecyclerView.setAdapter(mImageAdapter);

    }

    @Override
    protected void setListener() {
        mImageAdapter.setOnImageSelectChangedListener(new ChooseImageListAdapter.OnImageSelectChangedListener() {

            @Override
            public void onSelectImage(MDImage selectImage, int selectNum) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_SELECT_IMAGE, selectImage);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onUnSelectImage(MDImage unselectImage, int selectNum) {
            }

            @Override
            public void onTakePhoto() {

            }

            @Override
            public void onPictureClick(MDImage media, int position) {

            }

            @Override
            public void onIsSelectAllImage(boolean isSelectAll) {
            }
        });
    }

    private void loadImageRequest() {
        GlobalRestful.getInstance().GetCloudPhoto(mPageIndex, mIsShared, 0, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mPageIndex++;

                if (StringUtil.isEmpty(response.body().getContent())) {
                    mDataBinding.imageRecyclerView.setLoadingMore(false);
                    mDataBinding.imageRecyclerView.setupMoreListener(null, 0);
                } else {
                    ArrayList<MDImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });

                    mImagesList.addAll(tempImagesList);
                    mImageAdapter.bindImages(mImagesList);
                }

                mDataBinding.imageRecyclerView.hideMoreProgress();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    //region ACTION
    public void nextStepAction(View view) {
        NavUtils.toPhotoEditActivity(view.getContext());
    }
    //endregion
}
