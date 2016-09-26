package com.kw.app.chinesemedicine.mvp.contract;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * @author wty
 */
public interface IUserLoginContract {

    interface IUserLoginModel extends IBaseModel {
        void login(Context context,String name, String psw, boolean isAutoLogin, ICallBack<UserBmob> callBack);
    }

    interface IUserLoginView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
        void showLoadingView(String loadmsg);
        void dismissLoadingView();
        void updateLoadingMsg(String msg);
        void showFailed(String msg);
    }

}
