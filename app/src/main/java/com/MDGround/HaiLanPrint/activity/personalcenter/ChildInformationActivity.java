package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityChildInformationBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.models.UserKid;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.views.dialog.BirthdayDatePickerDialog;
import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-13.
 */

public class ChildInformationActivity extends ToolbarActivity<ActivityChildInformationBinding>
        implements DatePickerDialog.OnDateSetListener, OnFocusChangeListener {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_child_information;
    }

    @Override
    protected void initData() {
        tvRight.setText(R.string.save);
        tvRight.setVisibility(View.VISIBLE);
        List<UserKid> userKids = MDGroundApplication.mLoginUser.getUserKidList();
        if(userKids!=null){
            if (userKids.size() >0) {
                int lastKid = userKids.size() - 1;
                UserKid userKid = userKids.get(lastKid);
                if(userKid.getDOB()!=null){
                mDataBinding.etChildName.setText(userKid.getName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    KLog.e("DOB="+ userKid.getDOB());
                    date = format.parse(userKid.getDOB());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String DOB = DateUtils.getDateStringBySpecificFormat(date, new SimpleDateFormat("yyyy-MM-dd"));
                mDataBinding.etChildBirth.setText(DOB);
                mDataBinding.etSchool.setText(userKid.getSchool());
                mDataBinding.etClass.setText(userKid.Class);
                mDataBinding.etChildName.setOnFocusChangeListener(this);
                mDataBinding.etSchool.setOnFocusChangeListener(this);
                mDataBinding.etClass.setOnFocusChangeListener(this);}
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDataBinding.etChildBirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText _v=(EditText)v;
        if (!hasFocus) {// 失去焦点
            _v.setHint(_v.getTag().toString());
        } else {
            String hint=_v.getHint().toString();
            _v.setTag(hint);
            _v.setHint("");
        }
    }

    @Override
    protected void setListener() {

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mChildName = mDataBinding.etChildName.getText().toString();
                String mChildBirth = mDataBinding.etChildBirth.getText().toString();
                String mChildSchool = mDataBinding.etSchool.getText().toString();
                String mChildClass = mDataBinding.etClass.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                String updateDate = DateUtils.getServerDateStringByDate(date);
                User user = MDGroundApplication.mLoginUser;
                if(user.getUserKidList()==null){
                    List<UserKid> userKidList=new ArrayList<UserKid>();
                     user.setUserKidList(userKidList);
                }
                List<UserKid> userKids = user.getUserKidList();
                UserKid kid = new UserKid();
                kid.setUserID(user.getUserID());
                kid.setUpdatedTime(updateDate);
                kid.setName(mChildName);
                kid.setClass(mChildClass);
                kid.setSchool(mChildSchool);
                kid.setDOB(mChildBirth);
                userKids.add(kid);

                GlobalRestful.getInstance().SaveUserInfo(user, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (ResponseCode.isSuccess(response.body())) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            }
        });
    }

    //region ACTION
    public void choseChildBirthDay(View view) {
        Calendar calendar = Calendar.getInstance();
        new BirthdayDatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    //endregion
}
