package com.kw.app.chinesemedicine.base;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobAnnotationCache;
import com.kw.app.chinesemedicine.data.annotation.bmob.BmobObjectDao;

/**
 * @author wty
 * 缓存对象，用于改善速度
 **/
public class BmobCache {

	private static volatile BmobCache sInstance = null;

	/** Bmob对象中字段 缓存，注入时用到 */
	private BmobAnnotationCache bmobAnnotationCache;

	public static BmobCache getInstance() {
		if (sInstance == null) {
			synchronized (BmobCache.class) {
				if (sInstance == null) {
					sInstance = new BmobCache();
				}
			}
		}
		return sInstance;
	}

	/**
	 * 功能描述：退出应用调用该方法，用于清除所有缓存对象
	 **/
	public void clearCache(){
		if(bmobAnnotationCache != null)bmobAnnotationCache.clear();//清除掉bmob缓存对象
		BmobObjectDao.clear();
	}

	public BmobAnnotationCache getBmobAnnotationCache() {
		if(bmobAnnotationCache == null)
			synchronized (this){
				if(bmobAnnotationCache == null){
					bmobAnnotationCache = new BmobAnnotationCache();
				}
			}
		return bmobAnnotationCache;
	}

}
