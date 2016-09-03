package com.kw.app.chinesemedicine.base;

import android.content.Context;

import com.wty.app.library.base.AppConstant;
import com.wty.app.library.base.MainApplication;

import cn.bmob.v3.Bmob;

public class CMApplication extends MainApplication {

	private static Context mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this.getApplicationContext();
		//初始化Bmob功能
		Bmob.initialize(this, AppConstant.Bmob_ApplicationId);

	}

	/**
	 * 功能描述：获得一个全局的application对象
	 **/
	public static Context getInstance(){
		return mApplication;
	}

}
