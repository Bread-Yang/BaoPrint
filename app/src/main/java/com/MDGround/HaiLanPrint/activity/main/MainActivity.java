package com.MDGround.HaiLanPrint.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.BR;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudOverviewActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChooseInchActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initData();
    }

    private void initData() {
        mDataBinding.viewPager.setAdapter(new MainAdapter());
    }

    public void toCloudActivityAction(View view) {
        Intent intent = new Intent(this, CloudOverviewActivity.class);
        startActivity(intent);
    }

    public class MainBindingEventHandler {

        public void toPhotoPrintChooseInchActivityAction(View view) {
            Intent intent = new Intent(MainActivity.this, PrintPhotoChooseInchActivity.class);
            startActivity(intent);
        }

        public void toMagicCupActivityAction(View view) {

        }

        public void toLomoCardActivityAction(View view) {

        }

        public void toEngravingActivityAction(View view) {

        }

    }

    //region ADAPTER
    private class MainAdapter extends PagerAdapter {

        private MainBindingEventHandler eventHandler;

        public MainAdapter() {
            eventHandler = new MainBindingEventHandler();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ViewDataBinding dataBinding = null;

            if (position == 0) {
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext())
                        , R.layout.item_main_pager1, container, false);
            } else {
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext())
                        , R.layout.item_main_pager2, container, false);
            }
            dataBinding.setVariable(BR.handlers, eventHandler);

            View root = dataBinding.getRoot();
            container.addView(root);
            return root;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
    //endregion

}
