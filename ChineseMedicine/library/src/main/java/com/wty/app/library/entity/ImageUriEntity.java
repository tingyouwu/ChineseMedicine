package com.wty.app.library.entity;

import android.text.TextUtils;
import java.io.Serializable;

/**
 * @Decription 图片实体
 * @author wty
 **/
public class ImageUriEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final int TYPE_NORMAL = 0;//正常的图片
	public static final int TYPE_ADD = 1;//+号图片
	public static final int TYPE_DELETE = 2;//-号图片

	public int type;
	public String uri;//本地图片路径
	public String uploadUrl;//服务端路径

	public ImageUriEntity(String uri) {
		this.type = TYPE_NORMAL;
		this.uri = uri;
	}

	public ImageUriEntity(int type, String uri) {
		this.type = type;
		this.uri = uri;
	}

	public ImageUriEntity(int type, String uri,String uploadUrl) {
		this.type = type;
		this.uri = uri;
		this.uploadUrl = uploadUrl;
	}
	
	public void setUploadUrl(String uploadUrl){
		this.uploadUrl = uploadUrl;
	}

    public boolean isEmpty(){
        return TextUtils.isEmpty(uri) && TextUtils.isEmpty(uploadUrl);
    }
}