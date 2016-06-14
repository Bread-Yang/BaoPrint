package com.MDGround.HaiLanPrint.activity.selectimage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.adapter.SelectedImageAdapter;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectAlbumBeforeEditBinding;
import com.MDGround.HaiLanPrint.databinding.ItemSelectAlbumBeforeEditBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.LocalMediaLoader;
import com.MDGround.HaiLanPrint.utils.NavUtils;
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
public class SelectAlbumBeforeEditActivity extends ToolbarActivity<ActivitySelectAlbumBeforeEditBinding> {

    private int mMaxSelectImageNum = 0;

    private AlbumAdapter mAlbumAdapter;

    private SelectedImageAdapter mSelectedImageAdapter;

    private List<Album> mAlbumsList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_album_before_edit;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        // 相册
        LinearLayoutManager albumLayoutManager = new LinearLayoutManager(this);
        albumLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.albumRecyclerView.setLayoutManager(albumLayoutManager);
        mDataBinding.albumRecyclerView.addItemDecoration(new DividerItemDecoration(0));
        mAlbumAdapter = new AlbumAdapter();
        mDataBinding.albumRecyclerView.setAdapter(mAlbumAdapter);

        // 选中图片
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(this);
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.selectedImageRecyclerView.setLayoutManager(imageLayoutManager);
        mSelectedImageAdapter = new SelectedImageAdapter();
        mDataBinding.selectedImageRecyclerView.setAdapter(mSelectedImageAdapter);

        new LocalMediaLoader(SelectAlbumBeforeEditActivity.this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<Album> albums) {
                mAlbumsList.addAll(albums);

                getPhotoCountRequest();

                switch (MDGroundApplication.mChoosedProductType) {
                    case MagazineAlbum:
                    case ArtAlbum:
                    case Calendar:
                        mMaxSelectImageNum = SelectImageUtil.mTemplateImage.size();
                        break;
                    case Postcard:
                    case Poker:
                    case Puzzle:
                    case MagicCup:
                    case LOMOCard:
                        getPhotoTemplateListRequest();  // 模版图片
                        break;
                    default:
                        mMaxSelectImageNum = SelectImageUtil.getMaxSelectImageNum(MDGroundApplication.mChoosedProductType);
                        changeTips();
                        break;
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }

    private void changeTips() {
        if (SelectImageUtil.mAlreadySelectImage.size() > 0) {
            String tips = getString(R.string.choose_image_tips, SelectImageUtil.mAlreadySelectImage.size(), mMaxSelectImageNum);

            mDataBinding.tvChooseTips.setText(Html.fromHtml(tips));

            mDataBinding.tvPleaseChoose.setVisibility(View.INVISIBLE);
            mDataBinding.tvChooseTips.setVisibility(View.VISIBLE);
            mDataBinding.btnNextStep.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.tvPleaseChoose.setText(getString(R.string.please_choose_image, mMaxSelectImageNum));

            mDataBinding.tvPleaseChoose.setVisibility(View.VISIBLE);
            mDataBinding.tvChooseTips.setVisibility(View.INVISIBLE);
            mDataBinding.btnNextStep.setVisibility(View.INVISIBLE);
        }
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

                    // 个人相册
                    Album personalAlbum = new Album();
                    personalAlbum.setName(getString(R.string.personal_album));
                    mAlbumsList.add(personalAlbum);

                    // 共享相册
                    Album shareAlbum = new Album();
                    shareAlbum.setName(getString(R.string.share_album));
                    mAlbumsList.add(shareAlbum);

                    for (MDImage mdImage : tempImagesList) {
                        List<MDImage> images = new ArrayList<MDImage>();
                        images.add(mdImage);

                        Album album = null;

                        if (mdImage.isShared()) {
                            album = mAlbumsList.get(mAlbumsList.size() - 1);
                        } else {
                            album = mAlbumsList.get(mAlbumsList.size() - 2);
                        }

                        album.setImages(images);
                        album.setImageNum(mdImage.getPhotoCount());
                    }

                    mAlbumAdapter.bindAlbum(mAlbumsList);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.dismiss();
            }
        });
    }

    private void getPhotoTemplateListRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoTemplateList(MDGroundApplication.mChoosedMeasurement.getTypeDescID(), new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ArrayList<Template> templateArrayList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                });

                MDGroundApplication.mChoosedTemplate = templateArrayList.get(0);

                mMaxSelectImageNum = MDGroundApplication.mChoosedTemplate.getPageCount();

                changeTips();

                getPhotoTemplateAttachListRequest(MDGroundApplication.mChoosedTemplate.getTemplateID());

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void getPhotoTemplateAttachListRequest(int templateID) {
        GlobalRestful.getInstance().GetPhotoTemplateAttachList(templateID, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                SelectImageUtil.mTemplateImage.clear();

                SelectImageUtil.mTemplateImage = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                });

                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    //region ACTION
    public void nextStepAction(View view) {
        NavUtils.toPhotoEditActivity(view.getContext());
    }
    //endregion

    //region ADAPTER
    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

        private List<Album> mAlbumsList = new ArrayList<>();

        public void bindAlbum(List<Album> albumList) {
            this.mAlbumsList.clear();
            this.mAlbumsList.addAll(albumList);
            notifyDataSetChanged();
        }

        @Override
        public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_album_before_edit, parent, false);
            AlbumAdapter.ViewHolder holder = new AlbumAdapter.ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(AlbumAdapter.ViewHolder holder, final int position) {
            final Album album = mAlbumsList.get(position);

            holder.viewDataBinding.setAlbum(album);
            holder.viewDataBinding.setViewHolder(holder);
        }

        @Override
        public int getItemCount() {
            return mAlbumsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ItemSelectAlbumBeforeEditBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void toSelectImageActivityAction(View view) {
                Album album = mAlbumsList.get(getAdapterPosition());

                Intent intent = new Intent(SelectAlbumBeforeEditActivity.this, SelectImageBeforeEditActivity.class);
                intent.putExtra(Constants.KEY_ALBUM, album);
                intent.putExtra(Constants.KEY_MAX_IMAGE_NUM, mMaxSelectImageNum);
                startActivity(intent);
            }
        }
    }
    //endregion
}
