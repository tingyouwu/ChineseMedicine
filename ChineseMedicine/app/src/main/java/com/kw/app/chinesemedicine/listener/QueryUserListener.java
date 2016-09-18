package com.kw.app.chinesemedicine.listener;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * @author :smile
 * @project:QueryUserListener
 * @date :2016-02-01-16:23
 */
public abstract class QueryUserListener extends BmobListener<UserBmob> {

    public abstract void done(UserBmob s, BmobException e);

    @Override
    protected void postDone(UserBmob o, BmobException e) {
        done(o, e);
    }
}
