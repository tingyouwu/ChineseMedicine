package com.wty.app.library.base;

import android.content.Context;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class AppContext implements Serializable {

	private WeakReference<Context> context;
	private static AppContext instance = null;

	public static synchronized AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	/**
	 * 获取 系统上下文
	 */
	public static Context getContext() {
		if (getInstance().context == null) {
			return null;
		}
		return getInstance().context.get();
	}

	/**
	 * 设置 系统上下文
	 */
	public static void setContext(Context context) {
        if(getInstance().context!=null){
            getInstance().context.clear();
        }
		getInstance().context = new WeakReference<Context>(context);
	}

}
