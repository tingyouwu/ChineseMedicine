package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.mvp.contract.IMyAccountContract;
import com.kw.app.chinesemedicine.mvp.model.MyAccountModel;
import com.orhanobut.logger.Logger;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by kuangminan on 2016/9/29.
 */
public class MyAccountPresenter extends BasePresenter<IMyAccountContract.IMyAccountView> {

    IMyAccountContract.IMyAccountModel mMyAccountModel;

    public  MyAccountPresenter(){
        mMyAccountModel = new MyAccountModel();
    }

    public void updateHeadImage(final Context context, final Uri uri, final ImageView mHeadView){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在更新中...");

        mMyAccountModel.updateHeadImage(context, uri,new ICallBack<String>() {
            @Override
            public void onSuccess(final String bomburi) {
                mView.dismissLoading(new OnDismissCallbackListener("头像更新成功") {
                    @Override
                    public void onCallback() {
                        Logger.d("KMA ...uri:"+uri.getPath());
                        Logger.d("KMA ...bomburi:"+bomburi);

                        ImageLoaderUtil.loadCircle(context, PreferenceUtil.getInstance().getLogoUrl(), R.drawable.login_account,mHeadView);
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