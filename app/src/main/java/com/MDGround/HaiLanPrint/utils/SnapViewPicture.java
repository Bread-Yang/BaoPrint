package com.MDGround.HaiLanPrint.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SnapViewPicture {

	public static String snapViewReturnLocalPath(View snapView) {
		if (snapView instanceof ScrollView) {
			snapView = ((ScrollView) snapView).getChildAt(0);
		}
		ByteArrayOutputStream baos = null;
		Bitmap bitmap = null;

		try {
			bitmap = Bitmap.createBitmap(snapView.getWidth(),
					snapView.getHeight(), Bitmap.Config.RGB_565);
			snapView.draw(new Canvas(bitmap));
			
			String filename = "smartKitchenShare.jpg";

			File sd = Environment.getExternalStorageDirectory();
			File dest = new File(sd, filename);
			try {
				FileOutputStream out = new FileOutputStream(dest);
				bitmap.compress(Bitmap.CompressFormat.PNG, 45, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dest.getPath();
//			baos = new ByteArrayOutputStream();
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		} finally {
			try {
				/** no need to close, actually do nothing */
				if (null != baos)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	public static Bitmap snapViewReturnBitmap(View snapView) {
		if (snapView instanceof ScrollView) {
			snapView = ((ScrollView) snapView).getChildAt(0);
		}
		ByteArrayOutputStream baos = null;
		Bitmap bitmap = null;

		bitmap = Bitmap.createBitmap(snapView.getWidth(),
				snapView.getHeight(), Bitmap.Config.RGB_565);
		snapView.draw(new Canvas(bitmap));
		return bitmap;
	}
}
