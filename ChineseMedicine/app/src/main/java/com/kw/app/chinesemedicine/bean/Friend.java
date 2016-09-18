package com.kw.app.chinesemedicine.bean;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;

import cn.bmob.v3.BmobObject;

/**好友表
 * @author smile
 * @project Friend
 * @date 2016-04-26
 */
public class Friend extends BmobObject {

    private UserBmob user;
    private UserBmob friendUser;

    //拼音
    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

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
