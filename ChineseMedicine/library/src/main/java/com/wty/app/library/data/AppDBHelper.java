package com.wty.app.library.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wty.app.library.utils.CommonUtil;
import com.wty.app.library.utils.PreferenceUtil;

@SuppressLint("NewApi")
public class AppDBHelper extends SQLiteOpenHelper  {

	private Context context;

	public AppDBHelper(Context context) {
		super(context, CommonUtil.getApplicationName(context) + "_" + PreferenceUtil.getInstance().getLastAccount(), null, CommonUtil.getDbVersion(context));
		this.context=context;
		getWritableDatabase();
	}

    @Override
	public void onCreate(SQLiteDatabase db) {
	}

    /**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@SuppressLint("NewApi")
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//删除旧数据库
		for (String databaseName : context.databaseList()) {
			context.deleteDatabase(databaseName);
		}
	}

	/**
	 * 添加操作
	 *
	 * @param insertSql
	 *            对应插入字段 如：insert into person(name,age) values(?,?)
	 * @param obj
	 *            对应值 如： new Object[]{person.getName(),person.getAge()};
	 * @return
	 */
	public boolean save(String insertSql, Object obj[]) {
		try {
			getWritableDatabase().execSQL(insertSql, obj);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 添加操作
	 * @param tableName 表名
	 * @param values 集合对象
	 * @return
	 */
	public boolean save(String tableName, ContentValues values) {
		try {
            getWritableDatabase().insert(tableName, null, values);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 更新操作
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	@SuppressLint("NewApi")
	public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		try {
			int a = getWritableDatabase().update(table, values, whereClause, whereArgs);
			return a > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除
	 * @param deleteSql 对应跟新字段 如： "where personid=?"
	 * @param obj [] 对应值 如： new Object[]{person.getPersonid()};
	 * @return
	 */
	public boolean delete(String table, String deleteSql, String obj[]) {
		try {
            getWritableDatabase().delete(table, deleteSql, obj);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 查询操作
	 * @param findSql 对应查询字段 如： select * from person limit ?,?
	 * @param obj
	 *            对应值 如： new
	 *            String[]{String.valueOf(fristResult),String.valueOf(
	 *            maxResult)}
	 * @return
	 */
	public Cursor find(String findSql, String obj[]) {
		try {
			Cursor cursor = getReadableDatabase().rawQuery(findSql, obj);
			return cursor;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建数据库表
	 * @param createTableSql 对应的创建语句
       CREATE TABLE IF NOT EXISTS 表名
		  ( `id` integer primary key autoincrement,
			`accountno` VARCHAR,
			`address` VARCHAR,
			`birthday` VARCHAR,
			`city` VARCHAR,
			`country` VARCHAR,
			`qq` VARCHAR,
			`remark` VARCHAR,
			`sex` VARCHAR,
			`loginSuccess` INT )
	 */
	public boolean creatTable(String createTableSql) {
		try {
            getWritableDatabase().execSQL(createTableSql);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除表
	 * @param tableName 表名
	 */
	public boolean deleteTable(String tableName) {
		try {
            getWritableDatabase().execSQL("DROP TABLE IF EXISTS  " + tableName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断表是否存在
	 * @param tablename
	 * @return
	 */
	public boolean isTableExits(String tablename) {
		Cursor cursor = null;
		try {
			String str = "select count(*) xcount  from  " + tablename;
			cursor = getReadableDatabase().rawQuery(str, null);
			if (null != cursor && cursor.moveToFirst()) {
				return true;
			}
		} catch (Exception ex) {
			return false;
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 关闭 获取SQLite数据库连接
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase getConnection() {
		return getWritableDatabase();
	}

	/**
	 * 执行sql语句
	 * @param sql
	 **/
	public boolean execSQL(String sql) {
		try {
            getWritableDatabase().execSQL(sql);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
