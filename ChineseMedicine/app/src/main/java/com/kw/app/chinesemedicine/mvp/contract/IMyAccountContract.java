package com.kw.app.chinesemedicine.mvp.contract;

import android.content.Context;
import android.net.Uri;

import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * Created by kuangminan on 2016/9/29.
 */
public interface IMyAccountContract {
    interface IMyAccountModel extends IBaseModel {
        void updateHeadImage(Context context, Uri uri, ICallBack<String> callBack);
    }

    interface IMyAccountView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        //void finishActivity(String userid);
    }
}
