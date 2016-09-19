package com.kw.app.chinesemedicine.base;

import com.wty.app.library.base.MainApplication;

import cn.bmob.newim.BmobIM;

public class CMApplication extends MainApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化Bmob功能
		if(getApplicationInfo().packageName.equals(getMyProcessName())){
			//im初始化
			BmobIM.init(this);
			//注册消息接收器
			BmobIM.registerDefaultMessageHandler(new BmobMessageHandler(this));
		}

	}

}
