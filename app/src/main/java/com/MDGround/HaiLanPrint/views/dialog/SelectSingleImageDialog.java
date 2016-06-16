package com.MDGround.HaiLanPrint.views.dialog;

import android.app.Activity;
import android.app.Dialog;
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
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.DialogSelectSingleImageBinding;

import java.io.File;

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

    public SelectSingleImageDialog(Activity activity, int themeResId) {
        super(activity, themeResId);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_single_image, null, false);

        setContentView(mDataBinding.getRoot());

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
	 */
    private void camera() {
        dismiss();
        if (isSdCardMounted()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String path = Environment.getExternalStorageDirectory().toString() + Constants.PHOTO_FILE;
            File paht1 = new File(path);
            if (!paht1.exists()) {
                paht1.mkdir();
            }
            File file = new File(paht1, Constants.PHOTO_NAME);
            Uri outputFileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
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
