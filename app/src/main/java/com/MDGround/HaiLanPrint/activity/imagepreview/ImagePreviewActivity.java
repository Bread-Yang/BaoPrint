package com.MDGround.HaiLanPrint.activity.imagepreview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityImagePreviewBinding;
import com.MDGround.HaiLanPrint.models.MDImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoghourt on 2016-08-4.
 */

public class ImagePreviewActivity extends ToolbarActivity<ActivityImagePreviewBinding> {

    private List<MDImage> mAllPreviewImages = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initData() {
        tvTitle.setText("");
        mToolbar.setBackgroundResource(R.color.image_overlay2);
        mAllPreviewImages = getIntent().getParcelableArrayListExtra(Constants.KEY_PREVIEW_IMAGE_LIST);
        int imagePosition = getIntent().getIntExtra(Constants.KEY_PREVIEW_IMAGE_POSITION, 0);

        mDataBinding.previewViewPager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        mDataBinding.previewViewPager.setCurrentItem(imagePosition);
    }

    @Override
    protected void setListener() {

    }

    private class SimpleFragmentAdapter extends FragmentPagerAdapter {
        public SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImagePreviewFragment.getInstance(mAllPreviewImages.get(position));
        }

        @Override
        public int getCount() {
            return mAllPreviewImages.size();
        }
    }
}
