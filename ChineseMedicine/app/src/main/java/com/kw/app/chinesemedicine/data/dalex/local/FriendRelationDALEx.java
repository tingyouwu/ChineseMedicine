package com.kw.app.chinesemedicine.data.dalex.local;

import com.wty.app.library.data.QueryBuilder;
import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;
import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.data.dalex.SqliteBaseDALEx;

import java.util.List;

/**
 * 好友关系表
 * @author wty
 */
public class FriendRelationDALEx extends SqliteBaseDALEx {

	public static final String FRIENDID = "friendid";
	public static final String STATUS = "status";
	public static final String UPDATEAT = "updateAt";

	@DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
	private String relationid;

	@DatabaseField(Type = FieldType.VARCHAR)
	private String friendid; // 用户名

	@DatabaseField(Type = FieldType.INT)
	private int status;//是否已经删除  1表示还是好友  0表示已经删除关系

	@DatabaseField(Type = FieldType.VARCHAR)
	private String updateAt;//修改时间

	public static FriendRelationDALEx get() {
		return SqliteDao.getDao(FriendRelationDALEx.class);
	}

	/**
	 * 获取最新修改时间的一条数据
	 **/
	public FriendRelationDALEx  getNewestRelation(){
		List<FriendRelationDALEx> list = findList(new QueryBuilder().selectAll().from(TABLE_NAME).orderBy(desc(datetimeColumn(UPDATEAT))).build());
		if(list.size()==0){
			//本地一条数据都没有
			return null;
		}else{
			return list.get(0);
		}
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getRelationid() {
		return relationid;
	}

	public void setRelationid(String relationid) {
		this.relationid = relationid;
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
