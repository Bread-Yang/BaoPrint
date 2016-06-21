package com.MDGround.HaiLanPrint.activity.template;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityTemplateStartCreateBinding;
import com.MDGround.HaiLanPrint.databinding.ItemTemplateStartCreateBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;

/**
 * Created by yoghourt on 5/11/16.
 */
public class TemplateStartCreateActivity extends ToolbarActivity<ActivityTemplateStartCreateBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_template_start_create;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
        mDataBinding.setTemplate(MDGroundApplication.mChoosedTemplate);

        if (SelectImageUtils.mTemplateImage.size() > 0) {
            setCurrentPageTips(1);
        } else {
            setCurrentPageTips(0);
        }

        mDataBinding.viewPager.setAdapter(new TemplateStartCreateAdapter());
    }

    @Override
    protected void setListener() {
        mDataBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPageTips(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //region ACTION

    private void setCurrentPageTips(int currentIndex) {
        tvTitle.setText(getString(R.string.current_page_index, currentIndex, SelectImageUtils.mTemplateImage.size()));
    }

    public void nextStepAction(View view) {
        NavUtils.toSelectAlbumActivity(this);
    }
    //endregion

    private class TemplateStartCreateAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return SelectImageUtils.mTemplateImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ItemTemplateStartCreateBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext())
                    , R.layout.item_template_start_create, container, false);
            MDImage mdImage = SelectImageUtils.mTemplateImage.get(position);
            dataBinding.setMdImage(mdImage);

            View root = dataBinding.getRoot();

            container.addView(root);

            return root;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

}
