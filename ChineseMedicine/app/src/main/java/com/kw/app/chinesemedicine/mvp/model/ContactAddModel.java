package com.kw.app.chinesemedicine.mvp.model;

import com.kw.app.chinesemedicine.data.dalex.bmob.ContactBmob;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.model.impl.IContactAddModel;
import com.wty.app.library.callback.ICallBack;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty
 */
public class ContactAddModel implements IContactAddModel{

    @Override
    public void submit(final ContactDALEx data, final ICallBack<String> callBack) {
        ContactBmob  bmob = new ContactBmob();
        bmob.setAnnotationField(data);
        bmob.save(new SaveListener<String>() {
            @Override
            public void done(String objectid, BmobException e) {
                if (e != null) {
                    callBack.onFaild(e.getMessage());
                } else {
                    data.setContactid(objectid);
                    data.saveOrUpdate();
                    callBack.onSuccess(objectid);
                }
            }
        });
    }
}
