package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IUserRegisterContract;
import com.kw.app.chinesemedicine.mvp.model.UserRegisterModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 */
public class UserRegisterPresenter extends BasePresenter<IUserRegisterContract.IUserRegisterView> {

    private IUserRegisterContract.IUserRegisterModel mUserRegisterModel;

    public UserRegisterPresenter(){
        mUserRegisterModel = new UserRegisterModel();
    }

    public void register(Context context,final UserDALEx data){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在注册中...");

        mUserRegisterModel.register(context,data, new ICallBack<String>() {
            @Override
            public void onSuccess(final String userid) {
                mView.dismissLoading(new OnDismissCallbackListener("注册成功") {
                    @Override
                    public void onCallback() {
                        mView.finishActivity(userid);
                    }
                });
            }

            @Override
            public void onFaild(String msg) {
                mView.dismissLoading(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
            }
        });
    }

}
