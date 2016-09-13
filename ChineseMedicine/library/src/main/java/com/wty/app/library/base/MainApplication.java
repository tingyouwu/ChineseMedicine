package com.wty.app.library.base;

import android.app.Application;
import android.content.Context;

import com.wty.app.library.utils.AppLogUtil;
import com.wty.app.library.utils.PreferenceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainApplication extends Application {

	private static Context mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this.getApplicationContext();
		PreferenceUtil.init(this);
		AppLogUtil.init("老中医");
	}

	/**
	 * 功能描述：获得一个全局的application对象
	 **/
	public static Context getInstance(){
		return mApplication;
	}

	/**
	 * 获取当前运行的进程名
	 * @return
	 */
	public static String getMyProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
