package com.kw.app.chinesemedicine.mvp.contract;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

/**
 * @author wty
 */
public interface IDynamicAddContract {

    interface IDynamicAddModel extends IBaseModel {
        void submit(Context context,DynamicDALEx data, ICallBack<String> callBack);
    }

    interface IDynamicAddView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
    }
}
