package com.wty.app.library.base;

import com.wty.app.library.data.AppDBHelper;
import com.wty.app.library.data.annotation.SqliteAnnotationCache;
import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.utils.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wty
 * 应用所有的缓存对象，用于改善速度
 **/
public class AppCache {

	private static volatile AppCache sInstance = null;

	/** Orm Annotation中字段、Table的缓存，注入时用到 */
	private SqliteAnnotationCache sqliteAnnotationCache;

	/** LocalDB 数据库注册表
	 *  用于多账号
	 **/
	private Map<String,AppDBHelper> localDBMap = new HashMap<String,AppDBHelper>();

	public static AppCache getInstance() {
		if (sInstance == null) {
			synchronized (AppCache.class) {
				if (sInstance == null) {
					sInstance = new AppCache();
				}
			}
		}
		return sInstance;
	}

	/**
	 * 功能描述：根据帐号来获取数据库
	 **/
	public AppDBHelper getDBFromUserAccunt(String account){
		AppDBHelper db = localDBMap.get(account);
		if(db == null){
			synchronized (AppCache.class){
				if(db == null){
					db = new AppDBHelper(MainApplication.getInstance());
					localDBMap.put(account, db);
				}
			}
		}
		return db;
	}

	/**
	 * 功能描述：获取数据库
	 **/
	public AppDBHelper getDBFromUserAccunt(){
		String account = PreferenceUtil.getInstance().getLastName();
		return getDBFromUserAccunt(account);
	}

	/**
	 * 功能描述：关闭所有数据库
	 **/
	public void closeAllDB(){
		try {
			for(AppDBHelper db:localDBMap.values()){
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能描述：退出应用调用该方法，用于清除所有缓存对象
	 **/
	public void clearCache(){
		if(sqliteAnnotationCache != null)sqliteAnnotationCache.clear();//清掉数据库缓存对象
		SqliteDao.clear();
	}

	public SqliteAnnotationCache getSqliteAnnotationCache() {
		if(sqliteAnnotationCache == null){
			synchronized (this){
				if(sqliteAnnotationCache == null){
					sqliteAnnotationCache = new SqliteAnnotationCache();
				}
			}
		}
		return sqliteAnnotationCache;
	}

}
