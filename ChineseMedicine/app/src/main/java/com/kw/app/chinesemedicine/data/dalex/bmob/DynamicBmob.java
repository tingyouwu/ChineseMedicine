package com.kw.app.chinesemedicine.data.dalex.bmob;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobObjectDao;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Decription 朋友圈动态(bmob)
 * @author wty
 */
public class DynamicBmob extends BaseBmobObject {

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

	public static DynamicBmob get(){
		return BmobObjectDao.getDao(DynamicBmob.class);
	}

	public void save(final List<DynamicBmob> list){
		List<DynamicDALEx> localdalex = new ArrayList<DynamicDALEx>();
		for(DynamicBmob bmob:list){
			DynamicDALEx dalex = new DynamicDALEx();
			dalex.setAnnotationField(bmob.getAnnotationFieldValue());
			dalex.setDynamicid(bmob.getObjectId());
			localdalex.add(dalex);
		}
		DynamicDALEx.get().saveOrUpdate(localdalex);
	}

	public List<DynamicDALEx> saveReturn(final List<DynamicBmob> list){
		List<DynamicDALEx> localdalex = new ArrayList<DynamicDALEx>();
		for(DynamicBmob bmob:list){
			DynamicDALEx dalex = new DynamicDALEx();
			dalex.setAnnotationField(bmob.getAnnotationFieldValue());
			dalex.setDynamicid(bmob.getObjectId());
			localdalex.add(dalex);
		}
		DynamicDALEx.get().saveOrUpdate(localdalex);
		return localdalex;
	}
}
