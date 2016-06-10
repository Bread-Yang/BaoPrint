package com.MDGround.HaiLanPrint.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.DialogSelectSingleImageBinding;

/**
 * Created by yoghourt on 5/25/16.
 */

public class SelectSingleImageDialog extends Dialog {

    public static final int PHOTO_REQUEST_GALLERY = 1;// 相册
    public static final int PHOTO_REQUEST_CAREMA = 2;// 拍照
    public static final int PHOTO_REQUEST_CUT = 3;// 剪切

    private DialogSelectSingleImageBinding mDataBinding;

    private Activity mActivity;

    private Uri mImageUri;

    public SelectSingleImageDialog(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_single_image, null, false);

        setContentView(mDataBinding.getRoot());

        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
        window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
        window.setAttributes(wlp);

        mDataBinding.btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });

        mDataBinding.btnLocalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });

        mDataBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /*
     * 从相册获取
	 */
    private void gallery() {
        dismiss();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        mActivity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
	 * 从相机获取
	 */
    private void camera() {
        dismiss();
        if (isSdCardMounted()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            mImageUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageUri);
            /** ----------------- */
            mActivity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        } else {
            Toast.makeText(mActivity, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isSdCardMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }
}
