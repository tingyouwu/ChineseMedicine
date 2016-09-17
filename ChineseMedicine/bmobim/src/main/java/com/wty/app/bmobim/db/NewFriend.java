package com.wty.app.bmobim.db;

import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.data.dalex.SqliteBaseDALEx;

/**
 * @Decription
 * 本地好友请求表
 **/
public class NewFriend extends SqliteBaseDALEx{

    private String uid;//发送者的uid
    private String msg;
    private String name;
    private String avatar;//发送者的头像
    private int status;
    private long time;

    public static NewFriend get() {
        return SqliteDao.getDao(NewFriend.class);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
