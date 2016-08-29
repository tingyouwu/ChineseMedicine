package com.wty.app.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class CommonUtil {

	/**
	 * 功能描述：获取当前数据库版本
	 **/
	public static int getDbVersion(Context context){
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getInt("dbversion");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 *	功能描述：获取版本号(内部识别号)
	 **/
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 *	功能描述： 获取版本号
	 **/
	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取应用名称
	 * @param context
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * @Decription 隐藏或者显示输入法
	 **/
	public static void keyboardControl(Context context,boolean show,EditText editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(show){
			//显示键盘
			imm.showSoftInput(editText, 0);
		}else{
			//隐藏键盘
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
