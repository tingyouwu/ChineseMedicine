package com.kw.app.chinesemedicine.mvp.model;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.model.impl.IDynamicModel;
import com.wty.app.library.callback.ICallBack;

import java.util.List;

/**
 * @author wty
 */
public class DynamicModel implements IDynamicModel {

    @Override
    public void loadMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack) {

    }

    @Override
    public void refreshMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack) {

    }

    @Override
    public void loadDynamicFirst(ICallBack<List<DynamicDALEx>> callBack) {

    }
}
