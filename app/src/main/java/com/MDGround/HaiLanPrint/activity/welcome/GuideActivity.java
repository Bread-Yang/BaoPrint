package com.MDGround.HaiLanPrint.activity.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.PreferenceUtils;
import com.MDGround.HaiLanPrint.views.SimpleGuideBanner;
import com.flyco.banner.anim.select.ZoomInEnter;

import java.util.ArrayList;

/**
 * Created by yoghourt on 6/20/16.
 */

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        PreferenceUtils.setPrefBoolean(Constants.KEY_IS_FIRST_LAUNCH, false);

        ArrayList<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.guide_1);
        imageList.add(R.drawable.guide_2);
        imageList.add(R.drawable.guide_3);

        SimpleGuideBanner sgb = (SimpleGuideBanner) findViewById(R.id.sgb);

        sgb.setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
//                .setTransformerClass(transformerClass)
                .barPadding(0, 10, 0, 10)
                .setSource(imageList)
                .startScroll();

        sgb.setOnJumpClickListener(new SimpleGuideBanner.OnJumpClickListener() {
            @Override
            public void onJumpClick() {
                NavUtils.toLoginActivity(GuideActivity.this);
            }
        });
    }
}
