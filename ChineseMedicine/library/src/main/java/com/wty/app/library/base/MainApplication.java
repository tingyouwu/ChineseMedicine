package com.wty.app.library.base;

import android.app.Application;
import android.content.Context;

import com.wty.app.library.utils.AppLogUtil;
import com.wty.app.library.utils.PreferenceUtil;

public class MainApplication extends Application {

	private static Context mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this.getApplicationContext();
		PreferenceUtil.init(this);
		AppLogUtil.init();
	}

	/**
	 * 功能描述：获得一个全局的application对象
	 **/
	public static Context getInstance(){
		return mApplication;
	}

}
