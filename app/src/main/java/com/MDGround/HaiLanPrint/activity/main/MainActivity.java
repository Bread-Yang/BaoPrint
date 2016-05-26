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
import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudOverviewActivity;
import com.MDGround.HaiLanPrint.activity.lomocard.LomoCardChooseNumActivity;
import com.MDGround.HaiLanPrint.activity.magiccup.MagicCupChooseColorActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.postcard.PostcardStartActivity;
import com.MDGround.HaiLanPrint.activity.puzzle.PuzzleStartActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityMainBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MDImage> mImagesList = new ArrayList<MDImage>();

    private ActivityMainBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initData();
    }

    private void initData() {
        GlobalRestful.getInstance().GetBannerPhotoList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    ArrayList<MDImage> tempImagesList = response.body().getContent(new TypeToken<ArrayList<MDImage>>() {
                    });

                    mImagesList.addAll(tempImagesList);

                    mDataBinding.simpleImageBanner
                            .setSource(mImagesList).startScroll();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });

        mDataBinding.viewPager.setAdapter(new MainAdapter());
    }

    //region ACTION
    public void toCloudActivityAction(View view) {
        Intent intent = new Intent(this, CloudOverviewActivity.class);
        startActivity(intent);
    }


    public class MainBindingEventHandler {

        public void toPhotoPrintChooseInchActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.PrintPhoto;

            Intent intent = new Intent(MainActivity.this, PrintPhotoChooseInchActivity.class);
            startActivity(intent);
        }

        public void toMagicCupActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.MagicCup;

            Intent intent = new Intent(MainActivity.this, MagicCupChooseColorActivity.class);
            startActivity(intent);
        }

        public void toLomoCardActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.LOMOCard;

            Intent intent = new Intent(MainActivity.this, LomoCardChooseNumActivity.class);
            startActivity(intent);
        }

        public void toEngravingActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.Engraving;
        }

        public void toPuzzleActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.Puzzle;

            Intent intent = new Intent(MainActivity.this, PuzzleStartActivity.class);
            startActivity(intent);
        }

        public void toPostcarActivityAction(View view) {
            MDGroundApplication.mSelectProductType = ProductType.Postcard;

            Intent intent = new Intent(MainActivity.this, PostcardStartActivity.class);
            startActivity(intent);
        }
    }
    //endregion

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
