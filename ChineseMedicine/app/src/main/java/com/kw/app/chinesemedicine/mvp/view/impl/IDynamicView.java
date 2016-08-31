package com.kw.app.chinesemedicine.mvp.view.impl;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IDynamicView extends IBaseView {

    boolean checkNet();

    void showNoNet();

    void refreshMore(List<DynamicDALEx> list);

    void loadMore(List<DynamicDALEx> list);

    void onRefreshComplete();

    void onRefreshComplete(int result);

    void onLoadMoreComplete();

    void onLoadMoreComplete(int result);
}
