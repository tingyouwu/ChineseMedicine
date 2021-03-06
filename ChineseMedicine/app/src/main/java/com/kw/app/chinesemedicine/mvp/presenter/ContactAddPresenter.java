package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactAddContract;
import com.kw.app.chinesemedicine.mvp.model.ContactAddModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 */
public class ContactAddPresenter extends BasePresenter<IContactAddContract.IContactAddView> {

    private IContactAddContract.IContactAddModel mContactAddModel;

    public ContactAddPresenter(){
        mContactAddModel = new ContactAddModel();
    }

    public void submit(Context context, final ContactDALEx data){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在添加...");

        mContactAddModel.submit(context,data, new ICallBack<String>() {
            @Override
            public void onSuccess(String objectid) {
                mView.dismissLoading(new OnDismissCallbackListener("添加成功"){
                    @Override
                    public void onCallback() {
                        mView.finishActivity();
                    }
                });
            }

            @Override
            public void onFaild(String error) {
                mView.dismissLoading(new OnDismissCallbackListener(error, SweetAlertDialog.ERROR_TYPE));
            }
        });

    }

}
