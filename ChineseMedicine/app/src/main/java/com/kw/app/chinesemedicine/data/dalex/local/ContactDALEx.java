package com.kw.app.chinesemedicine.data.dalex.local;

import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;
import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.data.dalex.SqliteBaseDALEx;

/**
 * @Decription 联系人通讯录
 * @author wty
 */
public class ContactDALEx extends SqliteBaseDALEx {

	@DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
	private String contactid;//动态id

	@DatabaseField(Type = FieldType.VARCHAR)
	private String username; // 姓名

	@DatabaseField(Type = FieldType.VARCHAR)
	private String mobilephone; // 手机号码

	@DatabaseField(Type = FieldType.VARCHAR)
	private String logourl; // 头像

	@DatabaseField(Type = FieldType.VARCHAR)
	private String sex; // 性别

	@DatabaseField(Type = FieldType.VARCHAR)
	private String pinyin; // 名字拼音

	@DatabaseField(Type = FieldType.VARCHAR)
	private String tel; // 固定电话

	@DatabaseField(Type = FieldType.VARCHAR)
	private String email; // 电子邮箱

	@DatabaseField(Type = FieldType.INT)
	private int age;//年龄

	public static ContactDALEx get() {
		return SqliteDao.getDao(ContactDALEx.class);
	}

	public String getContactid() {
		return contactid;
	}

	public void setContactid(String contactid) {
		this.contactid = contactid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
