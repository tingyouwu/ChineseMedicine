package com.wty.app.library.mvp.presenter;

import com.wty.app.library.mvp.view.IBaseView;

/**
 * @author wty
 * IBasePresenter是所有Presenter的基类
 */
public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);

    void detachView();
}
