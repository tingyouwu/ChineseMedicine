package com.kw.app.chinesemedicine.mvp.view.impl;

import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;


/**
 * @author wty
 */
public interface IUserRegisterView extends IBaseView {
    /**
     * 提交数据过程的界面提示
     **/
    void showLoading(String loadmsg);
    void dismissLoading(OnDismissCallbackListener callback);

    /**
     * 网络检查提示
     **/
    boolean checkNet();

    /**
     * 显示无网络
     **/
    void showNoNet();

    /**
     * 结束activity
     **/
    void finishActivity();
}
