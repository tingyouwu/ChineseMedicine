package com.kw.app.chinesemedicine.mvp.model.impl;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;

/**
 * @author wty
 */
public interface IDynamicAddModel extends IBaseModel {
    /**
     * @Decription 提交数据
     **/
    void submit(DynamicDALEx data, ICallBack<String> callBack);
}
