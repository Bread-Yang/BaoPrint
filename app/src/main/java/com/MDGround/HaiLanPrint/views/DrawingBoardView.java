package com.MDGround.HaiLanPrint.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

/**
 * Created by yoghourt on 7/12/16.
 */

public class DrawingBoardView extends View {

    private static final int MODE_UP = 0;
    private static final int MODE_DRAG = 1;
    private static final int MODE_ZOOM = 2;

    private Context mContext;

    private ScrollView mScrollView;

    private Bitmap mPhotoBmp, mMouldBmp, mOutputBmp;

    private Rect mSrcRect, mDestRect;

    private PointF mStartPoint = new PointF();
    private Matrix mMatrix = new Matrix();

    private float mViewHeight, mViewWidth;
    private float mStartDistance;
    private float mOldRotation;
    private float mDx, mDy;
    private float mRate;
    private float mScale;
    private int mMode = MODE_UP;
    private boolean mIsTouch, mIsSelected;

    public DrawingBoardView(Context context) {
        super(context);
        mContext = context;
    }

    public DrawingBoardView(Context context, float width, float height,
                            Bitmap mouldBmp, Bitmap photoBmp, Matrix matrix, float rate) {
        super(context);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMould(width, height, mouldBmp);
        setPhoto(photoBmp, matrix, rate);
    }

    public void setMould(float width, float height, Bitmap mouldBmp) {
        if (mouldBmp != null) {
            this.mViewWidth = width;
            this.mViewHeight = height;
            mMouldBmp = mouldBmp;
            if (mMouldBmp != null) {
                this.mSrcRect = new Rect(0, 0, mMouldBmp.getWidth(), mMouldBmp.getHeight());
            }
            this.mDestRect = new Rect(0, 0, (int) width, (int) height);
            try {
                mOutputBmp = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_4444);
            } catch (OutOfMemoryError e) {
//                BitMapUtil.oom();
                try {
                    mOutputBmp = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_4444);
                } catch (OutOfMemoryError e2) {
//                    BitMapUtil.oom();
                    mOutputBmp = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_4444);
                }
            }
        }
    }

    public void setPhoto(Bitmap photoBmp, Matrix matrix, float rate) {
        if (photoBmp != null) {
            Matrix photoMatrix = new Matrix();
            mRate = rate;
            if (matrix == null) {
                matrix = new Matrix();
            }
            mMatrix = matrix;

            int photoBmpWidth = photoBmp.getWidth();
            int photoBmpHeight = photoBmp.getHeight();
            mScale = mViewWidth / ((float) photoBmpWidth) > mViewHeight / ((float) photoBmpHeight)
                    ? mViewWidth / ((float) photoBmpWidth)
                    : mViewHeight / ((float) photoBmpHeight);
            if (mScale != 0.0f) {
                Bitmap scalePhotoBmp;
                photoMatrix.setScale(mScale, mScale);
                try {
                    scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
                } catch (OutOfMemoryError e) {
//                    BitMapUtil.oom();
                    try {
                        scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
                    } catch (OutOfMemoryError e2) {
//                        BitMapUtil.oom();
                        scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
                    }
                }
                if (scalePhotoBmp != photoBmp) {
                    photoBmp.recycle();
                }
                mPhotoBmp = scalePhotoBmp;
                float photoWidth = (float) scalePhotoBmp.getWidth();
                float photoHeigh = (float) scalePhotoBmp.getHeight();
                mDx = (this.mViewWidth - photoWidth) / 2.0f;
                mDy = (this.mViewHeight - photoHeigh) / 2.0f;
                mMatrix.preTranslate(mDx, mDy);
            }
        }
    }

    private void compositePicture() {
        Canvas canvas = new Canvas(mOutputBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.parseColor("#BAB399"));
        if (!(mMouldBmp == null || mMouldBmp.isRecycled())) {
            /**
             * Rect src: 是对图片进行裁截，若是空null则显示整个图片
             * RectF dst：是图片在Canvas画布中显示的区域，大于src则把src的裁截区放大，小于src则把src的裁截区缩小。
             */
            canvas.drawBitmap(mMouldBmp, this.mSrcRect, this.mDestRect, paint);
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); // 取两层绘制交集。显示上层(src)。
        paint.setDither(true);
        if (mPhotoBmp != null && !mPhotoBmp.isRecycled()) {
            canvas.drawBitmap(mPhotoBmp, mMatrix, paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mViewWidth != 0.0f && mViewHeight != 0.0f && mMouldBmp != null && mPhotoBmp != null) {
            float[] values = new float[9];
            mMatrix.getValues(values);
            compositePicture();
            canvas.drawBitmap(mOutputBmp, 0.0f, 0.0f, null);
            float resizeSacle = (float) Math.sqrt((double) ((values[0] * values[0]) + (values[3] * values[3])));
            if (mRate < resizeSacle / mScale) {
                drawPixelDeficiency(canvas);
            }
            if (isFocused() && mIsSelected) {
                drawWarningLine(canvas);
            }
        }
    }

    public PointF matrixCalculator() {
        Matrix matrix = getIMatrix();

        float[] values = new float[9];
        matrix.getValues(values);
        float sinO = values[3];
        float cosO = values[0];
        float scaleM = (float) Math.sqrt((double) ((sinO * sinO) + (cosO * cosO)));
        sinO /= scaleM;
        cosO /= scaleM;
        float l = (float) Math.sqrt((double) ((mViewWidth * mViewWidth) + (mViewHeight * mViewHeight)));
        float sinA = mViewWidth / l;
        float cosA = mViewHeight / l;
        float x1 = (l * ((sinA * cosO) - (cosA * sinO))) / 2.0f;
        float y1 = (l * ((cosA * cosO) + (sinA * sinO))) / 2.0f;
        return new PointF((mViewWidth / 2.0f) - (scaleM * x1), (mViewHeight / 2.0f) - (scaleM * y1));
    }

    // 像素不足
    private void drawPixelDeficiency(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(mContext, R.color.colorRed));
        textPaint.setTextSize(ViewUtils.dp2px(18));

        String text = mContext.getString(R.string.pixel_deficiency);
        float textWidth = textPaint.measureText(text);

        if (textWidth > mViewWidth) {
            textPaint.setTextSize(mViewWidth / 5);
            textWidth = textPaint.measureText(text);
        }

        float textHeigh = textPaint.getFontMetricsInt(null);

        canvas.drawText(text, (mViewWidth - textWidth) / 2.0f,
                ((mViewHeight - textHeigh) / 2.0f) - textPaint.getFontMetrics().top,
                textPaint);
    }

    private void drawWarningLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.color_0076fe));

        float lineWidth = ViewUtils.dp2px(1.0f);
        paint.setStrokeWidth(lineWidth);

        float startPosition = lineWidth / 2.0f;

        // 左线
        canvas.drawLine(startPosition, startPosition, startPosition, mViewHeight - startPosition, paint);

        // 上线
        canvas.drawLine(startPosition, startPosition, mViewWidth - startPosition, startPosition, paint);

        // 右线
        canvas.drawLine(mViewWidth - startPosition, startPosition, mViewWidth - startPosition, mViewHeight - startPosition, paint);

        // 下线
        canvas.drawLine(startPosition, mViewHeight - startPosition, mViewWidth - startPosition, mViewHeight - startPosition, paint);
    }

    public void setmouldPic(Bitmap bmp) {
        mMouldBmp = bmp;
        invalidate();
    }

    public void setPhotoPic(Bitmap bmp) {
        mPhotoBmp = bmp;
        invalidate();
    }

    public void setMatrix(float pos1, float pos2, float pos3, float pos4, float pos5, float pos6) {
        float[] values = new float[9];
        mMatrix.getValues(values);
        values[0] = pos1;
        values[MODE_DRAG] = pos2;
        values[MODE_ZOOM] = pos3;
        values[3] = pos4;
        values[4] = pos5;
        values[5] = pos6;
        mMatrix.setValues(values);
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    public Matrix getIMatrix() {
        Matrix matrix = new Matrix(mMatrix);
        matrix.preTranslate(-mDx, -mDy);
        return matrix;
    }

    public void translate(String dx, String dy) {
        mMatrix.postTranslate(Float.parseFloat(dx), Float.parseFloat(dy));
    }

    public void bigger() {
        mMatrix.postScale(1.1f, 1.1f, getWidth() / 2, getHeight() / 2);
        invalidate();
    }

    public void littler() {
        mMatrix.postScale(0.9f, 0.9f, getWidth() / 2, getHeight() / 2);
        invalidate();
    }

    public void rotate() {
        mMatrix.postRotate(90.0f, getWidth() / 2, getHeight() / 2);
        invalidate();
    }

    // 翻转图像
    public void reverse() {
        mMatrix.postScale(-1.0f, 1.0f);
        mMatrix.postTranslate((float) getWidth(), 0.0f);
        invalidate();
    }

    public void setOnTouchListener(ScrollView scrollView) {
        mScrollView = scrollView;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mIsSelected = false;
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mScrollView != null) {
                mScrollView.requestDisallowInterceptTouchEvent(false); // 允许ScrollView拦截事件
            }
            this.mIsTouch = false;
        }

        if (x > 0.0f && x < mViewWidth && y > 0.0f && y < mViewHeight) {
            touch(event, x, y);

            if (event.getAction() != MotionEvent.ACTION_UP) {
                if (mScrollView != null) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                mIsTouch = true;
            } else {
                if (mScrollView != null) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                }
                mIsTouch = false;
            }
        }

        invalidate();

        return super.onTouchEvent(event);
    }

    private void touch(MotionEvent event, float x, float y) {
        mIsSelected = true;

        Matrix currentMatrix = new Matrix();
        PointF midPoint = new PointF();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mMode = MODE_DRAG;
                currentMatrix.set(mMatrix);
                mStartPoint.set(x, y);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mMode = MODE_ZOOM;
                mStartDistance = distance(event);
                if (mStartDistance > 10.0f) {
                    midPoint = mid(event);
                }
                mOldRotation = rotation(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mMode = MODE_UP;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_DRAG) {
                    float dx = event.getX() - mStartPoint.x;
                    float dy = event.getY() - mStartPoint.y;
                    mMatrix.set(currentMatrix);
                    mMatrix.postTranslate(dx, dy);
                } else if (mMode == MODE_ZOOM) {
                    float endDistance = distance(event);

                    if (endDistance > 10.0f) {
                        float scale = endDistance / mStartDistance;

                        mMatrix.set(currentMatrix);
                        mMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        mMatrix.postRotate(rotation(event) - mOldRotation, midPoint.x, midPoint.y);
                    }
                }
                break;
        }
    }

    private float distance(MotionEvent event) {
        float dx = event.getX(MODE_DRAG) - event.getX(0);
        float dy = event.getY(MODE_DRAG) - event.getY(0);
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    private PointF mid(MotionEvent event) {
        return new PointF((event.getX(MODE_DRAG) + event.getX(0)) / 2.0f,
                (event.getY(MODE_DRAG) + event.getY(0)) / 2.0f);
    }

    private float rotation(MotionEvent event) {
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(0) - event.getY(MODE_DRAG)),
                (double) (event.getX(0) - event.getX(MODE_DRAG))));
    }

    public void clear() {
        if (!(mOutputBmp == null || mOutputBmp.isRecycled())) {
            mOutputBmp.recycle();
            mOutputBmp = null;
        }
        if (mPhotoBmp != null && !mPhotoBmp.isRecycled()) {
            mPhotoBmp.recycle();
            mPhotoBmp = null;
        }
    }
}
