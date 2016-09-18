package com.kw.app.chinesemedicine.base;

import com.wty.app.library.base.AppConstant;
import com.wty.app.library.base.MainApplication;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

public class CMApplication extends MainApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化Bmob功能
		if(getApplicationInfo().packageName.equals(getMyProcessName())){
			Bmob.initialize(this, AppConstant.Bmob_ApplicationId);
			//im初始化
			BmobIM.init(this);
			//注册消息接收器
			BmobIM.registerDefaultMessageHandler(new BmobMessageHandler(this));
		}

	}

}
