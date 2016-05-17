package com.MDGround.HaiLanPrint.activity.selectimage;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.adapter.AlbumAdapter;
import com.MDGround.HaiLanPrint.adapter.SelectedImageAdapter;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectAlbumBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.LocalMediaLoader;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.DividerItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/11/16.
 */
public class SelectAlbumActivity extends ToolbarActivity<ActivitySelectAlbumBinding> {

    private ProductType mProductType;

    private int mMaxSelectImageNum = 0;

    private AlbumAdapter mAlbumAdapter;

    private SelectedImageAdapter mSelectedImageAdapter;

    private List<Album> mAlbumsList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_album;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedImageAdapter.notifyDataSetChanged();
        changeTips();
    }

    @Override
    protected void initData() {

        mProductType = (ProductType) getIntent().getSerializableExtra(Constants.KEY_PRODUCT_TYPE);

        mMaxSelectImageNum = SelectImageUtil.getMaxSelectImageNum(mProductType);

        // 相册
        LinearLayoutManager albumLayoutManager = new LinearLayoutManager(this);
        albumLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.albumRecyclerView.setLayoutManager(albumLayoutManager);
        mDataBinding.albumRecyclerView.addItemDecoration(new DividerItemDecoration());
        mAlbumAdapter = new AlbumAdapter(mProductType);
        mDataBinding.albumRecyclerView.setAdapter(mAlbumAdapter);

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

        new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<Album> albums) {
                mAlbumsList = albums;
                mAlbumAdapter.bindAlbum(mAlbumsList);
            }
        });

        getPhotoCountRequest();
    }

    @Override
    protected void setListener() {

    }

    private void changeTips() {
        mDataBinding.tvChooseTips.setText(getString(R.string.choose_image_tips, SelectImageUtil.mAlreadySelectImage.size(), mMaxSelectImageNum));
    }

    //region SERVER
    private void getPhotoCountRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetCloudPhotoCount(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                if (ResponseCode.isSuccess(response.body())) {

                    ArrayList<MDImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });

                    for (MDImage mdImage : tempImagesList) {
                        Album album = new Album();
                        if (mdImage.isShared()) {
                            album.setName(getString(R.string.cloud_album));
                        } else {
                            album.setName(getString(R.string.personal_album));
                        }
                        album.setImageNum(mdImage.getPhotoCount());

                        List<MDImage> images = new ArrayList<MDImage>();
                        images.add(mdImage);

                        album.setImages(images);
                        mAlbumsList.add(album);
                    }

                    mAlbumAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }
    //endregion

    //region ACTION
    public void nextStepAction(View view) {
        if (SelectImageUtil.mAlreadySelectImage.size() == 0) {
            return;
        }
        Intent intent = new Intent();
        switch (mProductType) {
            case PrintPhoto:
                intent.setClass(this, PrintPhotoChoosePaperNumActivity.class);
                break;
        }
        startActivity(intent);
    }
    //endregion
}
