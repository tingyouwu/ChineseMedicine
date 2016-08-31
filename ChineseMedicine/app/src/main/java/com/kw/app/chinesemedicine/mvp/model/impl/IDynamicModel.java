package com.kw.app.chinesemedicine.mvp.model.impl;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;

import java.util.List;

/**
 * @author wty
 */
public interface IDynamicModel extends IBaseModel {
    /**
     * 加载更多
     **/
    void loadMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack);

    /**
     * 刷新更多
     **/
    void refreshMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack);

    /**
     * 首次进入加载数据
     **/
    void loadDynamicFirst(ICallBack<List<DynamicDALEx>> callBack);
}
