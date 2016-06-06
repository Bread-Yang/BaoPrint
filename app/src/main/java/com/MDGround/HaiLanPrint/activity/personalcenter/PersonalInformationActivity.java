package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalInformationBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.views.SelectSingleImageDialog;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/30/16.
 */

public class PersonalInformationActivity extends ToolbarActivity<ActivityPersonalInformationBinding> {

    private SelectSingleImageDialog mSelectSingleImageDialog;

    private ArrayList<String> mUploadImageLocalPathList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void initData() {
        mSelectSingleImageDialog = new SelectSingleImageDialog(this);

        User user = MDGroundApplication.mLoginUser;

        // 用户头像
        MDImage mdImage = new MDImage();
        mdImage.setPhotoID(user.getPhotoID());
        mdImage.setPhotoSID(user.getPhotoSID());
        GlideUtil.loadImageByMDImage(mDataBinding.civAvatar, mdImage);

        // 昵称
        mDataBinding.tvNickname.setText(user.getUserNickName());
        mDataBinding.tvPhone.setText(user.getPhone());
        mDataBinding.tvAccountName.setText(user.getUserName());
    }

    @Override
    protected void setListener() {

    }

    private void uploadImageRequest(final int upload_image_index) {
        if (upload_image_index < mUploadImageLocalPathList.size()) {

            String imagePath = mUploadImageLocalPathList.get(upload_image_index);

            File file = new File(imagePath);

            KLog.e("localMedia.getImageLocalPath() : " + imagePath);

            FileRestful.getInstance().UploadCloudPhoto(false, file, null, new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    int nextUploadIndex = upload_image_index + 1;
                    uploadImageRequest(nextUploadIndex);
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                }
            });
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SelectSingleImageDialog.PHOTO_REQUEST_GALLERY) {// 从相册返回的数据
                Uri uri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                KLog.e("picturePath : " + picturePath);
            } else if (requestCode == SelectSingleImageDialog.PHOTO_REQUEST_CAREMA) {// 从相机返回的数据
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
            }
        }
    }

    //region ACTION
    public void selectSingleImageAction(View view) {
        mSelectSingleImageDialog.show();
    }
    //endregion
}
