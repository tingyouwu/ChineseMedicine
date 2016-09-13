package com.kw.app.chinesemedicine.base;

import com.wty.app.library.base.AppConstant;
import com.wty.app.library.base.MainApplication;

import cn.bmob.v3.Bmob;

public class CMApplication extends MainApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化Bmob功能
		Bmob.initialize(this, AppConstant.Bmob_ApplicationId);

	}

}
