package com.kw.app.chinesemedicine.bean;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;

import cn.bmob.v3.BmobObject;

/**
 * 好友关系表
 */
public class Friend extends BmobObject {

    private UserBmob user;
    private UserBmob friendUser;//关联的对象

    public UserBmob getUser() {
        return user;
    }

    public void setUser(UserBmob user) {
        this.user = user;
    }

    public UserBmob getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(UserBmob friendUser) {
        this.friendUser = friendUser;
    }
}
