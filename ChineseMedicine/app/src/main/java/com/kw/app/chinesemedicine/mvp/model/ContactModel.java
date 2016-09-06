package com.kw.app.chinesemedicine.mvp.model;

import com.kw.app.chinesemedicine.data.dalex.bmob.ContactBmob;
import com.kw.app.chinesemedicine.data.dalex.bmob.DynamicBmob;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.model.impl.IContactModel;
import com.wty.app.library.callback.ICallBack;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author wty
 */
public class ContactModel implements IContactModel {

    @Override
    public void loadMoreContact(ContactDALEx data, ICallBack<List<ContactDALEx>> callBack) {
        List<ContactDALEx> list = new ArrayList<ContactDALEx>();
        callBack.onSuccess(list);
    }

    @Override
    public void refreshMoreContact(ContactDALEx data, final ICallBack<List<ContactDALEx>> callBack) {
        BmobQuery<ContactBmob> query = new BmobQuery<ContactBmob>();

        query.findObjects(new FindListener<ContactBmob>() {
            @Override
            public void done(List<ContactBmob> list, BmobException e) {
                if(e != null){
                    callBack.onFaild(e.getMessage());
                }else{
                    List<ContactDALEx> newlist = ContactBmob.get().saveReturn(list);
                    callBack.onSuccess(newlist);
                }
            }
        });
    }

    @Override
    public void loadContactFirst(ICallBack<List<ContactDALEx>> callBack) {
        List<ContactDALEx> list = ContactDALEx.get().findAll();
        callBack.onSuccess(list);
     }
}
