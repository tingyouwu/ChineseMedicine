package com.kw.app.chinesemedicine.mvp.contract;


import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;
import java.util.List;

/**
 * @author wty
 */
public interface IDynamicContract {

    interface IDynamicModel extends IBaseModel {
        void loadMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack);
        void refreshMoreDynamic(DynamicDALEx data, ICallBack<List<DynamicDALEx>> callBack);
        void loadDynamicFirst(ICallBack<List<DynamicDALEx>> callBack);
    }

    interface IDynamicView extends IBaseView {
        boolean checkNet();
        void showNoNet();
        void refreshMore(List<DynamicDALEx> list);
        void loadMore(List<DynamicDALEx> list);
        void onRefreshComplete();
        void onRefreshComplete(int result);
        void onLoadMoreComplete();
        void onLoadMoreComplete(int result);
    }
}
