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
import com.MDGround.HaiLanPrint.enumobject.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.artalbum.ArtAlbumChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.calendar.CalendarChooseOrientationActivity;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudOverviewActivity;
import com.MDGround.HaiLanPrint.activity.engraving.EngravingChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.lomocard.LomoCardChooseNumActivity;
import com.MDGround.HaiLanPrint.activity.magazinealbum.MagazineAlbumChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.magiccup.MagicCupChooseColorActivity;
import com.MDGround.HaiLanPrint.activity.personalcenter.PersonalCenterActivity;
import com.MDGround.HaiLanPrint.activity.phoneshell.PhoneShellStartActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.pictureframe.PictureFrameStartActivity;
import com.MDGround.HaiLanPrint.activity.poker.PokerChooseInchActivity;
import com.MDGround.HaiLanPrint.activity.postcard.PostcardStartActivity;
import com.MDGround.HaiLanPrint.activity.puzzle.PuzzleStartActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityMainBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.SelectImageUtils;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

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
        setListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        KLog.e("onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectImageUtils.sAlreadySelectImage.clear(); // 清空之前选中的图片
        SelectImageUtils.sTemplateImage.clear(); // 清空之前的模板
        MDGroundApplication.sInstance.resetData();
    }

    private void initData() {
        getPhotoTypeExplainListRequest();
        getBannerPhotoListRequest();

        mDataBinding.viewPager.setAdapter(new MainAdapter());
    }

    private void setListener() {
        mDataBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mDataBinding.ivPager1.setImageResource(R.drawable.main_page_highlight);
                    mDataBinding.ivPager2.setImageResource(R.drawable.main_page_normal);
                } else {
                    mDataBinding.ivPager1.setImageResource(R.drawable.main_page_normal);
                    mDataBinding.ivPager2.setImageResource(R.drawable.main_page_highlight);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //region ACTION
    public void toCloudActivityAction(View view) {
        Intent intent = new Intent(this, CloudOverviewActivity.class);
        startActivity(intent);
    }

    public void toPersonalCenterActivityAction(View view) {
        Intent intent = new Intent(this, PersonalCenterActivity.class);
        startActivity(intent);
    }

    public class MainBindingEventHandler {

        // pager 1
        public void toPhotoPrintChooseInchActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.PrintPhoto);

            Intent intent = new Intent(MainActivity.this, PrintPhotoChooseInchActivity.class);
            startActivity(intent);
        }

        public void toPostcardActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.Postcard);

            Intent intent = new Intent(MainActivity.this, PostcardStartActivity.class);
            startActivity(intent);
        }

        public void toMagazineAlbumActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.MagazineAlbum);

            Intent intent = new Intent(MainActivity.this, MagazineAlbumChooseInchActivity.class);
            startActivity(intent);
        }

        public void toArtAlbumActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.ArtAlbum);

            Intent intent = new Intent(MainActivity.this, ArtAlbumChooseInchActivity.class);
            startActivity(intent);
        }

        public void toPictureFrameActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.PictureFrame);

            Intent intent = new Intent(MainActivity.this, PictureFrameStartActivity.class);
            startActivity(intent);
        }

        public void toCalendarActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.Calendar);

            Intent intent = new Intent(MainActivity.this, CalendarChooseOrientationActivity.class);
            startActivity(intent);
        }

        // pager 2
        public void toPhoneShellActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.PhoneShell);

            Intent intent = new Intent(MainActivity.this, PhoneShellStartActivity.class);
            startActivity(intent);
        }

        public void toPokerActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.Poker);

            Intent intent = new Intent(MainActivity.this, PokerChooseInchActivity.class);
            startActivity(intent);
        }

        public void toPuzzleActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.Puzzle);

            Intent intent = new Intent(MainActivity.this, PuzzleStartActivity.class);
            startActivity(intent);
        }

        public void toMagicCupActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.MagicCup);

            Intent intent = new Intent(MainActivity.this, MagicCupChooseColorActivity.class);
            startActivity(intent);
        }

        public void toLomoCardActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.LOMOCard);

            Intent intent = new Intent(MainActivity.this, LomoCardChooseNumActivity.class);
            startActivity(intent);
        }

        public void toEngravingActivityAction(View view) {
            MDGroundApplication.sInstance.setChoosedProductType(ProductType.Engraving);

            Intent intent = new Intent(MainActivity.this, EngravingChooseInchActivity.class);
            startActivity(intent);
        }
    }
    //endregion

    //region SERVER
    private void getPhotoTypeExplainListRequest() {
        GlobalRestful.getInstance().GetPhotoTypeExplainList(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ArrayList<PhotoTypeExplain> photoTypeExplainArrayList = response.body().getContent(new TypeToken<ArrayList<PhotoTypeExplain>>() {
                });

                MDGroundApplication.sInstance.setPhotoTypeExplainArrayList(photoTypeExplainArrayList);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void getBannerPhotoListRequest() {
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
