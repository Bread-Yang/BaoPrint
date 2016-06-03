package com.MDGround.HaiLanPrint.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.MDGround.HaiLanPrint.R;


/**
 * 提示消息
 * 
 * @author Vincent
 * 
 */
public class NotifyDialog extends Dialog implements View.OnClickListener {

	private TextView tvTitle;
	private TextView tvContent;
	private TextView tvSure;
	private TextView tvCancle;
	private OnSureClickListener listener;

	public static interface OnSureClickListener {
		public void onSureClick();
	}
	

	public NotifyDialog(Context context) {
		this(context, R.style.customDialogStyle);
	}

	public NotifyDialog(Context context, int theme) {
		super(context, theme);
	}

	protected NotifyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LinearLayout.inflate(getContext(), R.layout.dialog_notify, null);
		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		tvContent = (TextView) view.findViewById(R.id.tvContent);
		tvSure = (TextView) view.findViewById(R.id.tvSure);
		tvCancle = (TextView) view.findViewById(R.id.tvCancle);
		tvSure.setOnClickListener(this);
		tvCancle.setOnClickListener(this);
		setContentView(view);

		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvSure:
			if (this.listener != null) {
				this.listener.onSureClick();
			} else {
				dismiss();
			}
			break;
		case R.id.tvCancle:
			dismiss();
			break;

		default:
			break;
		}

	}

	public void setOnSureClickListener(OnSureClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void setTitle(CharSequence title) {
		if (this.tvTitle == null) {
			return;
		}
		this.tvTitle.setText(title);
	}

	public void setTvContent(CharSequence message) {
		if (tvContent == null) {
			return;
		}
		this.tvContent.setText(message);
	}

	public void setTvSure(CharSequence string) {
		if (tvSure == null) {
			return;
		}
		this.tvSure.setText(string);
	}
}
