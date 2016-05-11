package com.MDGround.HaiLanPrint.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * 生日日期选择，不能超过当前时间
 * 
 * @author Vincent
 * 
 */
public class BirthdayDatePickerDialog extends DatePickerDialog {

	public BirthdayDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
	}

	public BirthdayDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date date = new Date();
		if (calendar.getTime().getTime() >= date.getTime()) {
			calendar.setTime(date);
			super.onDateChanged(view, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		} else {
			super.onDateChanged(view, year, month, day);
		}

	}

}
