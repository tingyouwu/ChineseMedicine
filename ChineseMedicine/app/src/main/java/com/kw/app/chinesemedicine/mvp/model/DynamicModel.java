package com.kw.app.chinesemedicine.mvp.model;

import com.kw.app.chinesemedicine.data.dalex.bmob.DynamicBmob;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicContract;
import com.wty.app.library.callback.ICallBack;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author wty
 */
public class DynamicModel implements IDynamicContract.IDynamicModel {

    @Override
    public void loadMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack) {
        List<DynamicDALEx> list = new ArrayList<DynamicDALEx>();
        callBack.onSuccess(list);
    }

    @Override
    public void refreshMoreDynamic(DynamicDALEx data, final ICallBack<List<DynamicDALEx>> callBack) {
        BmobQuery<DynamicBmob> query = new BmobQuery<DynamicBmob>();

        query.findObjects(new FindListener<DynamicBmob>() {
            @Override
            public void done(List<DynamicBmob> list, BmobException e) {
                if(e != null){
                    callBack.onFaild(e.getMessage());
                }else{
                    List<DynamicDALEx> newlist = DynamicBmob.get().saveReturn(list);
                    callBack.onSuccess(newlist);
                }
            }
        });
    }

    @Override
    public void loadDynamicFirst(ICallBack<List<DynamicDALEx>> callBack) {

    }
}
