package com.MDGround.HaiLanPrint.activity.puzzle;

import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPuzzleStartBinding;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PuzzleStartActivity extends ToolbarActivity<ActivityPuzzleStartBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_puzzle_start;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public void nextStepAction(View view) {
        NavUtils.toSelectAlbumActivity(view.getContext(), ProductType.Puzzle);
    }
}
