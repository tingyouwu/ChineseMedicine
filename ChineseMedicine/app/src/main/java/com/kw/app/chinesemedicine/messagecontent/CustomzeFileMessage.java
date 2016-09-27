package com.kw.app.chinesemedicine.messagecontent;

import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import io.rong.message.MediaMessageContent;

/**
 * @author wty
 * 自定义  文件信息
 **/

@MessageTag(
		value = "app:customFileMsg",
		flag = MessageTag.NONE
)
public class CustomzeFileMessage extends MediaMessageContent {

	private static final String TAG = "CustomzeFileMessage";
	private String mName;
	private long mSize;
	private String mType;
	public static final Creator<CustomzeFileMessage> CREATOR = new Creator() {
		public CustomzeFileMessage createFromParcel(Parcel source) {
			return new CustomzeFileMessage(source);
		}

		public CustomzeFileMessage[] newArray(int size) {
			return new CustomzeFileMessage[size];
		}
	};

	public String getName() {
		return this.mName;
	}

	public void setName(String Name) {
		this.mName = Name;
	}

	public long getSize() {
		return this.mSize;
	}

	public void setSize(long size) {
		this.mSize = size;
	}

	public String getType() {
		return this.mType;
	}

	public void setType(String type) {
		if(!TextUtils.isEmpty(type)) {
			this.mType = type;
		} else {
			this.mType = "bin";
		}

	}

	public Uri getFileUrl() {
		return this.getMediaUrl();
	}

	public void setFileUrl(Uri uri) {
		this.setMediaUrl(uri);
	}

	public byte[] encode() {
		JSONObject jsonObj = new JSONObject();

		try {
			if(!TextUtils.isEmpty(this.mName)) {
				jsonObj.put("name", this.mName);
			}

			jsonObj.put("size", this.mSize);
			if(!TextUtils.isEmpty(this.mType)) {
				jsonObj.put("type", this.mType);
			}

			if(this.getLocalPath() != null) {
				jsonObj.put("localPath", this.getLocalPath().toString());
			}

			if(this.getMediaUrl() != null) {
				jsonObj.put("fileUrl", this.getMediaUrl().toString());
			}

			if(!TextUtils.isEmpty(this.getExtra())) {
				jsonObj.put("extra", this.getExtra());
			}

			if(this.getJSONUserInfo() != null) {
				jsonObj.putOpt("user", this.getJSONUserInfo());
			}
		} catch (JSONException var4) {
			RLog.e("FileMessage", "JSONException " + var4.getMessage());
		}

		try {
			return jsonObj.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException var3) {
			var3.printStackTrace();
			return null;
		}
	}

	public CustomzeFileMessage(byte[] data) {
		String jsonStr = null;

		try {
			jsonStr = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException var5) {
			var5.printStackTrace();
		}

		try {
			JSONObject e = new JSONObject(jsonStr);
			if(e.has("name")) {
				this.setName(e.optString("name"));
			}

			if(e.has("size")) {
				this.setSize(e.getLong("size"));
			}

			if(e.has("type")) {
				this.setType(e.optString("type"));
			}

			if(e.has("localPath")) {
				this.setLocalPath(Uri.parse(e.optString("localPath")));
			}

			if(e.has("fileUrl")) {
				this.setFileUrl(Uri.parse(e.optString("fileUrl")));
			}

			if(e.has("extra")) {
				this.setExtra(e.optString("extra"));
			}

			if(e.has("user")) {
				this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
			}
		} catch (JSONException var4) {
			RLog.e("FileMessage", "JSONException " + var4.getMessage());
		}

	}

	private CustomzeFileMessage() {
	}

	private CustomzeFileMessage(File file, Uri localUrl) {
		this.setLocalPath(localUrl);
		this.mName = file.getName();
		this.mSize = file.length();
	}

	public static CustomzeFileMessage obtain(Uri localUrl) {
		if(localUrl != null && localUrl.toString().startsWith("file")) {
			File file = new File(localUrl.toString().substring(7));
			return file.exists() && file.isFile()?new CustomzeFileMessage(file, localUrl):null;
		} else {
			return null;
		}
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		ParcelUtils.writeToParcel(dest, this.getExtra());
		ParcelUtils.writeToParcel(dest, this.getName());
		ParcelUtils.writeToParcel(dest, Long.valueOf(this.getSize()));
		ParcelUtils.writeToParcel(dest, this.getType());
		ParcelUtils.writeToParcel(dest, this.getLocalPath());
		ParcelUtils.writeToParcel(dest, this.getFileUrl());
		ParcelUtils.writeToParcel(dest, this.getUserInfo());
	}

	public CustomzeFileMessage(Parcel in) {
		this.setExtra(ParcelUtils.readFromParcel(in));
		this.setName(ParcelUtils.readFromParcel(in));
		this.setSize(ParcelUtils.readLongFromParcel(in).longValue());
		this.setType(ParcelUtils.readFromParcel(in));
		this.setLocalPath((Uri)ParcelUtils.readFromParcel(in, Uri.class));
		this.setFileUrl((Uri)ParcelUtils.readFromParcel(in, Uri.class));
		this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
	}

}
