package com.MDGround.HaiLanPrint.views.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

/**
 * Created by yoghourt on 5/16/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    public DividerItemDecoration() {
        mDrawable = MDGroundApplication.mInstance.getResources().getDrawable(R.drawable.shape_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int left = ViewUtils.dp2px(16);
        final int right = parent.getWidth() - left;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicWidth());
    }
}
