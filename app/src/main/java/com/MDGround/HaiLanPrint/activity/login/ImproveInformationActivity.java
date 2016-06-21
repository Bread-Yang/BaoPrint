package com.MDGround.HaiLanPrint.activity.login;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityImproveInformationBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.dialog.BirthdayDatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImproveInformationActivity extends ToolbarActivity<ActivityImproveInformationBinding>
        implements DatePickerDialog.OnDateSetListener {

    private User mUser;
    private boolean mIsFirstKidBirth=true;

    @Override
    public int getContentLayout() {
        return R.layout.activity_improve_information;
    }

    @Override
    protected void initData() {
        mToolbar.setNavigationIcon(null);

        mUser = (User) getIntent().getSerializableExtra(Constants.KEY_NEW_USER);
    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void chooseFirstKidBirthdayAction(View view) {
        mIsFirstKidBirth=true;
        Calendar calendar = Calendar.getInstance();
        new BirthdayDatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
     public void chooseSecondKidBirthdayAction(View view){
         mIsFirstKidBirth=false;
         Calendar calendar = Calendar.getInstance();
         new BirthdayDatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                 calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
     }
    public void finishAction(View view) {
        String name = mDataBinding.cetName.getText().toString();
        if (StringUtil.isEmpty(name)) {
            Toast.makeText(this, R.string.input_name, Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setUserName(name);

        String childName1 = mDataBinding.cetChildName1.getText().toString();
        mUser.setKidName1(childName1);

        String childName2=mDataBinding.cetChildName2.getText().toString();
        mUser.setKidName2(childName2);

        String childSchool1 = mDataBinding.cetChildSchool1.getText().toString();
        mUser.setKidSchool1(childSchool1);

        String childSchool2=mDataBinding.cetChildSchool2.getText().toString();
        mUser.setKidSchool2(childSchool2);

        String childClass1 = mDataBinding.cetChildClass1.getText().toString();
        mUser.setKidClass1(childClass1);

        String  chilidClass2=mDataBinding.cetChildClass2.getText().toString();
        mUser.setKidClass2(chilidClass2);

        String childBirth1=mDataBinding.tvChildBirthday1.getText().toString();
        String childBirth2=mDataBinding.tvChildBirthday2.getText().toString();
        SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("".equals(childBirth1)){
            childBirth1=null;
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1=simpleDateFormat.parse(childBirth1);
                childBirth1=formats.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if("".equals(childBirth2)){
            childBirth2=null;
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1=simpleDateFormat.parse(childBirth2);
                childBirth2=formats.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
          mUser.setKidDOB1(childBirth1);
          mUser.setKidDOB2(childBirth2);

        GlobalRestful.getInstance()
                .RegisterUser(mUser, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.body().getCode() == ResponseCode.Normal.getValue()) {
                            ViewUtils.toast(R.string.sign_up_success);
                            finish();
                        } else {
                            ViewUtils.toast(response.body().getMessage());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {

                    }
                });
    }
    //endregion

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(mIsFirstKidBirth){
//            mUser.setKidDOB1(DateUtils.getServerDateStringByDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0, 0).toDate()));
            mDataBinding.tvChildBirthday1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }else {
//            mUser.setKidDOB2(DateUtils.getServerDateStringByDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0, 0).toDate()));
            mDataBinding.tvChildBirthday2.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }

    }
}

