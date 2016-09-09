package com.kw.app.chinesemedicine.mvp.presenter;

import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.kw.app.chinesemedicine.mvp.model.UserLoginModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.PreferenceUtil;
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

    public void login(final String name, final String psw, final boolean isAutoLogin){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }

        mView.showLoading("请稍候，登录中...");
        mUserLoginModel.login(name, psw, new ICallBack<String>() {
            @Override
            public void onSuccess(String data) {
                mView.dismissLoading(null);
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, name);
                if(isAutoLogin){
                    PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, true);
                    PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword,psw);
                }else{
                    PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, false);
                    PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword, "");
                }
                mView.finishActivity();
            }

            @Override
            public void onFaild(String msg) {
                mView.dismissLoading(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
            }
        });
    }
}
