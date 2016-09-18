package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.kw.app.chinesemedicine.mvp.model.UserLoginModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 */
public class UserLoginPresenter extends BasePresenter<IUserLoginContract.IUserLoginView> {

    private IUserLoginContract.IUserLoginModel mUserLoginModel;

    public UserLoginPresenter(){
        mUserLoginModel = new UserLoginModel();
    }

    public void login(Context context,final String name, final String psw, final boolean isAutoLogin){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }

        mView.showLoading("请稍候，登录中...");
        mUserLoginModel.login(context,name, psw, isAutoLogin,new ICallBack<UserBmob>() {
            @Override
            public void onSuccess(UserBmob user) {
                mView.dismissLoading(null);
                mView.finishActivity(user);
            }

            @Override
            public void onFaild(String msg) {
                mView.dismissLoading(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
            }
        });
    }
}
