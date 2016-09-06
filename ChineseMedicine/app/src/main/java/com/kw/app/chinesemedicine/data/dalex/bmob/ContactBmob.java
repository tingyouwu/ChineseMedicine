package com.kw.app.chinesemedicine.data.dalex.bmob;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobObjectDao;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Decription 朋友圈动态(bmob)
 * @author wty
 */
public class ContactBmob extends BaseBmobObject {

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

	public static ContactBmob get(){
		return BmobObjectDao.getDao(ContactBmob.class);
	}

	public void save(final List<ContactBmob> list){
		List<ContactDALEx> localdalex = new ArrayList<ContactDALEx>();
		for(ContactBmob bmob:list){
			ContactDALEx dalex = new ContactDALEx();
			dalex.setAnnotationField(bmob.getAnnotationFieldValue());
			dalex.setContactid(bmob.getObjectId());
			localdalex.add(dalex);
		}
		ContactDALEx.get().saveOrUpdate(localdalex);
	}

	public List<ContactDALEx> saveReturn(final List<ContactBmob> list){
		List<ContactDALEx> localdalex = new ArrayList<ContactDALEx>();
		for(ContactBmob bmob:list){
			ContactDALEx dalex = new ContactDALEx();
			dalex.setAnnotationField(bmob.getAnnotationFieldValue());
			dalex.setContactid(bmob.getObjectId());
			localdalex.add(dalex);
		}
		ContactDALEx.get().saveOrUpdate(localdalex);
		return localdalex;
	}
}
