package com.kw.app.chinesemedicine.mvp.view.impl;

import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IContactView extends IBaseView {

    boolean checkNet();

    void showNoNet();

    void refreshMore(List<ContactDALEx> list);

    void loadMore(List<ContactDALEx> list);

    void onRefreshComplete();

    void onRefreshComplete(int result);

    void onLoadMoreComplete();

    void onLoadMoreComplete(int result);
}
