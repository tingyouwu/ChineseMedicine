package com.kw.app.chinesemedicine.mvp.contract;

import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * @author wty
 */
public interface IContactAddContract{

    interface IContactAddView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
    }

    interface IContactAddModel extends IBaseModel {
        void submit(ContactDALEx data, ICallBack<String> callBack);
    }
}
