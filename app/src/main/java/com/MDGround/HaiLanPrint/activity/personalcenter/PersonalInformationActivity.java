package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalInformationBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.dialog.SelectSingleImageDialog;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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
        mSelectSingleImageDialog = new SelectSingleImageDialog(PersonalInformationActivity.this);

        User user = MDGroundApplication.mLoginUser;

        // 用户头像
       MDImage mdImage= new MDImage();;
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
                String pro[] = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, pro, null, null, null);
                int Column_index = cursor.getColumnIndexOrThrow(pro[0]);
                cursor.moveToFirst();
                String Picturepath = cursor.getString(Column_index);
                KLog.e("picturePath" + Picturepath);
                uploadAvatar(Picturepath);
            } else if (requestCode == SelectSingleImageDialog.PHOTO_REQUEST_CAREMA) {// 从相机返回的数据
                KLog.e("相机返回数据");
                String Picturepath= Environment.getExternalStorageDirectory()+"/textphoto.jpg";
                uploadAvatar(Picturepath);
            }

        }

    }
    //region Server
    public void uploadAvatar(String Picturepath){
        if (Picturepath != null) {
            ViewUtils.loading(this);
            File file = new File(Picturepath);
            int UserID = MDGroundApplication.mLoginUser.getUserID();
            User userInfo = MDGroundApplication.mLoginUser;
            Date date = new Date(System.currentTimeMillis());
            String updatedTime = DateUtils.getServerDateStringByDate(date);
            userInfo.setUpdatedTime(updatedTime);
//                final String finalPicturepath = Picturepath;
            FileRestful.getInstance().SaveUserPhoto(UserID, file, userInfo, null, new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (ResponseCode.isSuccess(response.body())) {
                        KLog.e("返回来的数据" + response.body().getContent());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getContent());
                            String jsonStr = jsonObject.toString();
                            User user = StringUtil.getInstanceByJsonString(jsonStr, User.class);
                            KLog.e("userID是" + user.getPhotoSID());
                            MDGroundApplication.mLoginUser = user;
                            MDImage mdImage=new MDImage();
//                                mdImage.setImageLocalPath(finalPicturepath);
                            mdImage.setPhotoID(MDGroundApplication.mLoginUser.getPhotoID());
                            mdImage.setPhotoSID(MDGroundApplication.mLoginUser.getPhotoSID());
                            GlideUtil.loadImageByMDImage(mDataBinding.civAvatar, mdImage);
                            //GlideUtil.loadImageByPhotoSID(mDataBinding.civAvatar,user.getPhotoSID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    ViewUtils.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {

                }
            });
        }
    }
    //enregion
    //region ACTION
    public void selectSingleImageAction(View view) {
        mSelectSingleImageDialog.show();
    }
    //endregion

    //region ACTION
    public void toChangeNameAction(View view){
        Intent intent=new Intent(this,ChangeNameActivity.class);
        startActivity(intent);
    }
    //endregion
}
