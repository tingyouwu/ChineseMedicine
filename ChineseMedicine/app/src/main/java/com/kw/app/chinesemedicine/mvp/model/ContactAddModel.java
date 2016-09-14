package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.bmob.ContactBmob;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactAddContract;
import com.wty.app.library.callback.ICallBack;

import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty
 */
public class ContactAddModel implements IContactAddContract.IContactAddModel {

    @Override
    public void submit(Context context, final ContactDALEx data, final ICallBack<String> callBack) {
        final ContactBmob  bmob = new ContactBmob();
        bmob.setAnnotationField(data);

        bmob.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                data.setContactid(bmob.getObjectId());
                data.saveOrUpdate();
                callBack.onSuccess(bmob.getObjectId());
            }

            @Override
            public void onFailure(int i, String s) {
                callBack.onFaild(s);
            }
        });
    }
}
