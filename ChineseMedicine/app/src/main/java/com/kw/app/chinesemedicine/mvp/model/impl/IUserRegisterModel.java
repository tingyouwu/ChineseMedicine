package com.kw.app.chinesemedicine.mvp.model.impl;

import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;

/**
 * @author wty
 */
public interface IUserRegisterModel extends IBaseModel {
    /**
     * 注册
     **/
    void register(UserDALEx user, ICallBack<String> callBack);
}
