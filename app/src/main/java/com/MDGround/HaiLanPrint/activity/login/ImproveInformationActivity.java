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
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.dialog.BirthdayDatePickerDialog;

import org.joda.time.DateTime;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImproveInformationActivity extends ToolbarActivity<ActivityImproveInformationBinding>
        implements DatePickerDialog.OnDateSetListener {

    private User mUser;

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
    public void chooseBirthdayAction(View view) {
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

        String childName = mDataBinding.cetChildName.getText().toString();
        mUser.setChildName(childName);

        String childSchool = mDataBinding.cetChildSchool.getText().toString();
        mUser.setChildSchool(childSchool);

        String childClass = mDataBinding.cetChildClass.getText().toString();
        mUser.setChildClass(childClass);

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
        mUser.setChildDOB(DateUtils.getServerDateStringByDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0, 0).toDate()));
        mDataBinding.tvChildBirthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }
}

