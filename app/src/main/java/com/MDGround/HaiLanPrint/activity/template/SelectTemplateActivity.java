package com.MDGround.HaiLanPrint.activity.template;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectTemplateBinding;
import com.MDGround.HaiLanPrint.databinding.ItemSelectTemplateBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/30/16.
 */

public class SelectTemplateActivity extends ToolbarActivity<ActivitySelectTemplateBinding> {

    private final int mCountPerLine = 2; // 每行显示2个

    private ArrayList<Template> mAllTemplateArrayList = new ArrayList<>();

    private ArrayList<Template> mShowTemplateArrayList = new ArrayList<>();

    private HashMap<Integer, ArrayList<MDImage>> mTemplateAttachListHashMap = new HashMap<>();

    private SelectTemplateAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_template;
    }

    @Override
    protected void initData() {
        if (MDGroundApplication.mChoosedProductType == ProductType.PictureFrame) {
            mDataBinding.tabLayout.setVisibility(View.GONE);
            tvTitle.setText(R.string.choose_frame);
        } else {
            String[] titles = getResources().getStringArray(R.array.template_class_array);

            for (String title : titles) {
                mDataBinding.tabLayout.addTab(mDataBinding.tabLayout.newTab().setText(title));
            }
        }

        mDataBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(mCountPerLine, ViewUtils.dp2px(10), true));
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, mCountPerLine));

        mAdapter = new SelectTemplateAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        if (MDGroundApplication.mChoosedProductType == ProductType.PictureFrame) {
            getPhotoTemplateListByTypeRequest();
        } else {
            getPhotoTemplateListRequest();
        }

    }

    @Override
    protected void setListener() {
        mDataBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentSelectedTabIndex = tab.getPosition();

                mShowTemplateArrayList.clear();
                if (currentSelectedTabIndex == 0) {
                    mShowTemplateArrayList.addAll(mAllTemplateArrayList);
                } else {
                    for (Template template : mAllTemplateArrayList) {
                        if (template.getParentID() == currentSelectedTabIndex) {
                            mShowTemplateArrayList.add(template);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //region SERVER
    private void getPhotoTemplateListByTypeRequest() {
        GlobalRestful.getInstance().GetPhotoTemplateListByType(MDGroundApplication.mChoosedProductType, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mAllTemplateArrayList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                });

                mShowTemplateArrayList.addAll(mAllTemplateArrayList);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void getPhotoTemplateListRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoTemplateList(MDGroundApplication.mChoosedMeasurement.getTypeDescID(),
                new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ViewUtils.dismiss();
                        if (ResponseCode.isSuccess(response.body())) {
                            mAllTemplateArrayList = response.body().getContent(new TypeToken<ArrayList<Template>>() {
                            });

                            mShowTemplateArrayList.addAll(mAllTemplateArrayList);

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        ViewUtils.dismiss();
                    }
                });
    }

    private void getPhotoTemplateAttachListRequest(int templateID) {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().GetPhotoTemplateAttachList(templateID, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                SelectImageUtils.mTemplateImage.clear();

                SelectImageUtils.mTemplateImage = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                });

                Intent intent = new Intent(SelectTemplateActivity.this, TemplateStartCreateActivity.class);
                startActivity(intent);
                ViewUtils.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    //endregion

    //region ADAPTER
    public class SelectTemplateAdapter extends RecyclerView.Adapter<SelectTemplateAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_select_template, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Template template = mShowTemplateArrayList.get(position);
            holder.viewDataBinding.setTemplate(template);
            holder.viewDataBinding.setViewHolder(holder);

            GlobalRestful.getInstance().GetPhotoTemplateAttachList(template.getTemplateID(), new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    ArrayList<MDImage> templateImageArrayList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });
                    mTemplateAttachListHashMap.put(template.getTemplateID(), templateImageArrayList);

                    if (templateImageArrayList.size() > 0) {
                        GlideUtil.loadImageByMDImage(holder.viewDataBinding.ivImage, templateImageArrayList.get(0), true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mShowTemplateArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ItemSelectTemplateBinding viewDataBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                viewDataBinding = DataBindingUtil.bind(itemView);
            }

            public void onTemplateImageClickAction(View view) {
                MDGroundApplication.mChoosedTemplate = mShowTemplateArrayList.get(getAdapterPosition());

                switch (MDGroundApplication.mChoosedProductType) {
                    case MagazineAlbum:
                    case ArtAlbum:
                    case Calendar:
                        getPhotoTemplateAttachListRequest(MDGroundApplication.mChoosedTemplate.getTemplateID());
                        break;
                    default:
                        NavUtils.toSelectAlbumActivity(view.getContext());
                }
            }
        }
    }
    //endregion
}
