package com.yideguan.imageprint.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.yideguan.imageprint.enumobject.restfuls.PlatformType;
import com.yideguan.imageprint.restfuls.bean.Device;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class DeviceUtil {

	private static String filePath = Tools.getAppPath() + File.separator + "device";
//	private static String filePath = Tools.getAppPath() + File.separator + "device_ua";

	public static int getDeviceId() {
		if (ToolFile.isExsit(filePath)) {
			try {
				String dataStr = ToolFile.readFileByLines(filePath);
				return Integer.parseInt(dataStr.trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public static void setDeviceId(int deviceId) {
		try {
			if (!ToolFile.isExsit(filePath)) {
				File file = new File(filePath);
				file.createNewFile();
				ToolFile.write(file, String.valueOf(deviceId), System.getProperty("file.encoding"));
			} else {
				ToolFile.write(filePath, String.valueOf(deviceId).getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 判断是平板还是手机
	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static String getLanguage(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		return locale.getLanguage();
	}

	public static Device getDeviceInfo(Context context) {
		Device device = new Device();
		boolean isPad = DeviceUtil.isPad(context);
		if (isPad) {
			device.setPlatform(PlatformType.ANDROID_PAD.value());
		} else {
			device.setPlatform(PlatformType.ANDROID_PHONE.value());
		}
		// 设置android版本号
		device.setPlatformVersion(android.os.Build.VERSION.RELEASE);
		// 型号
		device.setDeviceModel(android.os.Build.MODEL);
		
		return device;
	}
	
	/**
	 * 获取平台类型
	 * @param context
	 * @return
	 */
	public static int getPlatformType(Context context) {
		boolean isPad = DeviceUtil.isPad(context);
		if (isPad) {
			return PlatformType.ANDROID_PAD.value();
		} else {
			return PlatformType.ANDROID_PHONE.value();
		}
	}
}
