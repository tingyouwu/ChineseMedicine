package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.bmob.ContactBmob;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactContract;
import com.wty.app.library.callback.ICallBack;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author wty
 */
public class ContactModel implements IContactContract.IContactModel {

    @Override
    public void loadMoreContact(Context context,ContactDALEx data, ICallBack<List<ContactDALEx>> callBack) {
        List<ContactDALEx> list = new ArrayList<ContactDALEx>();
        callBack.onSuccess(list);
    }

    @Override
    public void refreshMoreContact(Context context,ContactDALEx data, final ICallBack<List<ContactDALEx>> callBack) {
        BmobQuery<ContactBmob> query = new BmobQuery<ContactBmob>();

        query.findObjects(context, new FindListener<ContactBmob>() {
            @Override
            public void onSuccess(List<ContactBmob> list) {
                List<ContactDALEx> newlist = ContactBmob.get().saveReturn(list);
                callBack.onSuccess(newlist);
            }

            @Override
            public void onError(int i, String s) {
                callBack.onFaild(s);

            }
        });

    }

    @Override
    public void loadContactFirst(Context context,ICallBack<List<ContactDALEx>> callBack) {
        List<ContactDALEx> list = ContactDALEx.get().findAll();
        callBack.onSuccess(list);
     }
}
