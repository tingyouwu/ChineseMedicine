package com.kw.app.chinesemedicine.mvp.presenter;

import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.model.UserRegisterModel;
import com.kw.app.chinesemedicine.mvp.model.impl.IUserRegisterModel;
import com.kw.app.chinesemedicine.mvp.view.impl.IUserRegisterView;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 */
public class UserRegisterPresenter extends BasePresenter<IUserRegisterView> {

    private IUserRegisterModel mUserRegisterModel;

    public UserRegisterPresenter(){
        mUserRegisterModel = new UserRegisterModel();
    }

    public void register(final UserDALEx data){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在注册中...");

        mUserRegisterModel.register(data, new ICallBack<String>() {
            @Override
            public void onSuccess(String data) {
                mView.dismissLoading(new OnDismissCallbackListener("注册成功") {
                    @Override
                    public void onCallback() {
                        mView.finishActivity();
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
