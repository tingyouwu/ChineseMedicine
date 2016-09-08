package com.kw.app.chinesemedicine.mvp.contract;

import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * @author wty
 */
public interface IUserLoginContract {

    interface IUserLoginModel extends IBaseModel {
        void login(String name,String psw, ICallBack<String> callBack);
    }

    interface IUserLoginView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
    }

}
