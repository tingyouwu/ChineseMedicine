package com.kw.app.chinesemedicine.mvp.contract;


import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * @author wty
 */
public interface IUserRegisterContract {

    interface IUserRegisterModel extends IBaseModel {
        void register(UserDALEx user, ICallBack<String> callBack);
    }

    interface IUserRegisterView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
    }

}
