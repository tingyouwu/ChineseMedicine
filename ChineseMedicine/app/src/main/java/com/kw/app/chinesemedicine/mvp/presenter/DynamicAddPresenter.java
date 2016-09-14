package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicAddContract;
import com.kw.app.chinesemedicine.mvp.model.DynamicAddModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 */
public class DynamicAddPresenter extends BasePresenter<IDynamicAddContract.IDynamicAddView> {

    private IDynamicAddContract.IDynamicAddModel mDynamicAddModel;

    public DynamicAddPresenter(){
        mDynamicAddModel = new DynamicAddModel();
    }

    public void submit(Context context,final DynamicDALEx data){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在发布...");

        mDynamicAddModel.submit(context,data, new ICallBack<String>() {
            @Override
            public void onSuccess(String objectid) {
                mView.dismissLoading(new OnDismissCallbackListener("提交成功"){
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
