package com.kw.app.chinesemedicine.mvp.model.impl;

import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;

import java.util.List;

/**
 * @author wty
 */
public interface IContactModel extends IBaseModel {
    /**
     * 加载更多
     **/
    void loadMoreContact(ContactDALEx data, ICallBack<List<ContactDALEx>> callBack);

    /**
     * 刷新更多
     **/
    void refreshMoreContact(ContactDALEx data, ICallBack<List<ContactDALEx>> callBack);

    /**
     * 首次进入加载数据
     **/
    void loadContactFirst(ICallBack<List<ContactDALEx>> callBack);
}
