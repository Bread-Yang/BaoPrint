package com.MDGround.HaiLanPrint.views;

import android.content.Context;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.MDGround.HaiLanPrint.views.gesture.RotateGestureDetector;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by yoghourt on 5/19/16.
 */

public class BaoGPUImage extends GPUImageView {

    private GPUImageTransformFilter mTransformFilter;

    public GPUImageBrightnessFilter mBrightnessFilter;

    private float mScaleFactor = 1.0f;  // 放大缩小倍数

    private float mRotationDegrees = 0.f; // 旋转倍数

    private ScaleGestureDetector mScaleDetector;

    private RotateGestureDetector mRotateDetector;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor(); // scale change since previous event
//            KLog.e("mScaleSpan : " + mScaleFactor);

            float[] transform = new float[16];
            Matrix.setIdentityM(transform, 0);
            Matrix.setRotateM(transform, 0, mRotationDegrees, 0, 0, 1.0f);
            if (mScaleFactor < 0) {
                mScaleFactor = 1;
            }
            Matrix.scaleM(transform, 0, mScaleFactor, mScaleFactor, 1.0f);

            mTransformFilter.setTransform3D(transform);
            requestRender();
            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees += detector.getRotationDegreesDelta();
//            KLog.e("mRotationDegrees : " + mRotationDegrees);

            return true;
        }
    }

    public BaoGPUImage(Context context) {
        super(context);
        init(context);
    }

    public BaoGPUImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setScaleType(GPUImage.ScaleType.CENTER_INSIDE);

        mTransformFilter = new GPUImageTransformFilter();
        mBrightnessFilter = new GPUImageBrightnessFilter();
//        mBrightnessFilter.setBrightness(0);

        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(mBrightnessFilter);
        filterGroup.addFilter(mTransformFilter);

        setFilter(filterGroup);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mRotateDetector = new RotateGestureDetector(context, new RotateListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);

        return true;
    }
}
