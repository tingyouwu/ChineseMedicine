package com.kw.app.chinesemedicine.data.dalex.local;

import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;
import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.data.dalex.SqliteBaseDALEx;

/**
 * @Decription 朋友圈动态
 * @author wty
 */
public class DynamicDALEx extends SqliteBaseDALEx {
	
	private static final long serialVersionUID = 1L;
	public static final int No_Picture = 0;
	public static final int OnlyOne_Picture = 1;
	public static final int Multi_Picture = 2;

	@DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
	private String dynamicid;//动态id

	@DatabaseField(Type = FieldType.VARCHAR)
	private String content;//填写内容

	@DatabaseField(Type = FieldType.VARCHAR)
	private String images;//图片

	@DatabaseField(Type = FieldType.INT)
	private int creatby;//创建人id

	@DatabaseField(Type = FieldType.VARCHAR)
	private String sendname; // 创建人名字

	@DatabaseField(Type= FieldType.VARCHAR)
	private String logourl;//创建人的头像

	@DatabaseField(Type = FieldType.VARCHAR)
	private String createtime; // 创建时间

	@DatabaseField(Type = FieldType.REAL)
	private float singlesize;//单张图宽高比例  宽/高

	public static DynamicDALEx get() {
		return SqliteDao.getDao(DynamicDALEx.class);
	}

	public String getDynamicid() {
		return dynamicid;
	}

	public void setDynamicid(String dynamicid) {
		this.dynamicid = dynamicid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getCreatby() {
		return creatby;
	}

	public void setCreatby(int creatby) {
		this.creatby = creatby;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public float getSinglesize() {
		return singlesize;
	}

	public void setSinglesize(float singlesize) {
		this.singlesize = singlesize;
	}
}
