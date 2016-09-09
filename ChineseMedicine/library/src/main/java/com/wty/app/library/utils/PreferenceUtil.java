package com.wty.app.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author wty
 * ShareP 工具类  请在Application oncreate初始化
 **/
public class PreferenceUtil {

	private static volatile PreferenceUtil sInstance = null;

	private static String PREFERENCES_NAME = "Preferences";//preference名字
	public static String LastPassword = "lastPassword";//登陆密码
	public static String LastName = "lastname";//登陆名字
	public static String IsAutoLogin = "IsAutoLogin";//是否自动登陆

	private SharedPreferences mSharedPreferences;

	/**
	 * 单例模式，获取instance实例
	 * @return
	 */
	public synchronized static PreferenceUtil getInstance() {
		if (sInstance == null) {
			throw new RuntimeException("please init first!");
		}

		return sInstance;
	}

	/**
	 * context用AppContext
	 **/
	public static void init(Context context) {
		if (sInstance == null) {
			synchronized (PreferenceUtil.class) {
				if (sInstance == null) {
					sInstance = new PreferenceUtil(context);
				}
			}
		}
	}

	private PreferenceUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	/**
	 *	功能描述:保存到sharedPreferences
	 */
	public void writePreferences(String key,String value){
		mSharedPreferences.edit().putString(key, value).commit();// 提交修改;
	}

	public void writePreferences(String key,Boolean value){
		mSharedPreferences.edit().putBoolean(key, value).commit();// 提交修改;
	}

	public String getLastPassword(){
        return mSharedPreferences.getString(LastPassword, null);
	}


	public boolean isAutoLogin(){
	    return mSharedPreferences.getBoolean(IsAutoLogin, false);
	}

	public String getLastName(){
		return mSharedPreferences.getString(LastName, null);
	}
}
