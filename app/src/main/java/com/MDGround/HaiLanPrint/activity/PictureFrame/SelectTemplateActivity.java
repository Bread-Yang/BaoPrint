package com.MDGround.HaiLanPrint.activity.pictureframe;

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
import com.MDGround.HaiLanPrint.activity.calendar.CalendarTemplateDetailActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivitySelectTemplateBinding;
import com.MDGround.HaiLanPrint.databinding.ItemSelectTemplateBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.itemdecoration.GridSpacingItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

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

    private SelectTemplateAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_template;
    }

    @Override
    protected void initData() {
        if (MDGroundApplication.mChoosedProductType == ProductType.PictureFrame) {
            mDataBinding.tabLayout.setVisibility(View.GONE);
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

        getPhotoTemplateListByTypeRequest();
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
                mAllTemplateArrayList = response.body().getContent(new TypeToken<ArrayList<com.MDGround.HaiLanPrint.models.Template>>() {
                });

                mShowTemplateArrayList.addAll(mAllTemplateArrayList);

                mAdapter.notifyDataSetChanged();
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
            Template template = mShowTemplateArrayList.get(position);
            holder.viewDataBinding.setTemplate(template);
            holder.viewDataBinding.setViewHolder(holder);

            GlobalRestful.getInstance().GetPhotoTemplateAttachList(template.getTemplateID(), new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    ArrayList<MDImage> templateImageArrayList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });

                    if (templateImageArrayList.size() > 0) {
                        GlideUtil.loadImageByMDImage(holder.viewDataBinding.ivImage, templateImageArrayList.get(0));
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
                    case Calendar:
                        Intent intent = new Intent(SelectTemplateActivity.this, CalendarTemplateDetailActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        NavUtils.toSelectAlbumActivity(view.getContext());
                }
            }
        }
    }
    //endregion
}
