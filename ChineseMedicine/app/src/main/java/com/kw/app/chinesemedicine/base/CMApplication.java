package com.kw.app.chinesemedicine.base;

import com.wty.app.library.base.MainApplication;

import cn.bmob.newim.BmobIM;
import io.rong.imlib.RongIMClient;

public class CMApplication extends MainApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		//运行后您的 App 后您会发现以下三个进程：
		// 1、您的应用进程；
		// 2、您的应用进程: ipc，这是融云的通信进程；
		// 3、io.rong.push，这是融云的推送进程。

		//初始化Bmob功能
		if(getApplicationInfo().packageName.equals(getMyProcessName())){
			//im初始化
			BmobIM.init(this);
			//注册消息接收器
//			BmobIM.registerDefaultMessageHandler(new BmobMessageHandler(this));
		}

		/**
		 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
		 * io.rong.push 为融云 push 进程名称，不可修改。
		 */
		if(getApplicationInfo().packageName.equals(getMyProcessName()) || "io.rong.push".equals(getMyProcessName())){
			RongIMClient.init(this);
			RongManager.init(this);
		}

	}

}
