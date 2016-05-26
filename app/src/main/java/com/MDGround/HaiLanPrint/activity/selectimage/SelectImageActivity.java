package com.MDGround.HaiLanPrint.activity.selectimage;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.ChooseImageListAdapter;
import com.MDGround.HaiLanPrint.adapter.SelectedImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectImageBinding;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
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
public class SelectImageActivity extends ToolbarActivity<ActivitySelectImageBinding> {

    private ChooseImageListAdapter mImageAdapter;

    private SelectedImageAdapter mSelectedImageAdapter;

    private int mCountPerLine = 3; // 每行显示3个

    private Album mAlbum;

    private List<MDImage> mImagesList;

    private boolean mIsShared;

    private int mMaxSelectImageNum = 0;

    private int mPageIndex;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initData() {

        changeTips();

        mMaxSelectImageNum = SelectImageUtil.getMaxSelectImageNum(MDGroundApplication.mSelectProductType);

        mAlbum = getIntent().getParcelableExtra(Constants.KEY_ALBUM);

        mImagesList = mAlbum.getImages();

        if (mImagesList.get(0).getPhotoSID() != 0) {
            mIsShared = mImagesList.get(0).isShared();
            mImagesList.clear();

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

        // 相册
//        mDataBinding.imageRecyclerView.setHasFixedSize(true);
        mDataBinding.imageRecyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(2), false));
        mDataBinding.imageRecyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mImageAdapter = new ChooseImageListAdapter(this, mMaxSelectImageNum, true);
        mImageAdapter.bindImages(mImagesList);
        mImageAdapter.bindSelectImages(SelectImageUtil.mAlreadySelectImage);
        mDataBinding.imageRecyclerView.setAdapter(mImageAdapter);

        // 选中图片
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.selectedImageRecyclerView.setLayoutManager(imageLayoutManager);
        mSelectedImageAdapter = new SelectedImageAdapter();
        mDataBinding.selectedImageRecyclerView.setAdapter(mSelectedImageAdapter);

//        int colorBlue = getResources().getColor(R.color.blue);
//        String text = getString(R.string.text);
//        SpannableString spannable = new SpannableString(text);
//        spannable.setSpan(new ForegroundColorSpan(colorBlue), 0, text.length(), 0);
    }

    @Override
    protected void setListener() {
        mImageAdapter.setOnImageSelectChangedListener(new ChooseImageListAdapter.OnImageSelectChangedListener() {
            @Override
            public void onSelectImage(MDImage selectImage) {
                SelectImageUtil.addImage(selectImage);
                changeTips();
                mSelectedImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelectImage(MDImage unselectImage) {
                SelectImageUtil.removeImage(unselectImage);
                changeTips();
                mSelectedImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTakePhoto() {

            }

            @Override
            public void onPictureClick(MDImage media, int position) {

            }
        });

        mSelectedImageAdapter.setOnDeleteImageLisenter(new SelectedImageAdapter.onDeleteImageLisenter() {
            @Override
            public void deleteImage() {
                mImageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void changeTips() {
        String tips = getString(R.string.choose_image_tips, SelectImageUtil.mAlreadySelectImage.size(), mMaxSelectImageNum);

        mDataBinding.tvChooseTips.setText(Html.fromHtml(tips));
    }

    private void loadImageRequest() {
        GlobalRestful.getInstance().GetCloudPhoto(mPageIndex, mIsShared, new Callback<ResponseData>() {
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
