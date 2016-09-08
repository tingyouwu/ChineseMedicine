package com.kw.app.chinesemedicine.mvp.contract;


import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IContactContract {

    interface IContactModel extends IBaseModel {
        void loadMoreContact(ContactDALEx data, ICallBack<List<ContactDALEx>> callBack);
        void refreshMoreContact(ContactDALEx data, ICallBack<List<ContactDALEx>> callBack);
        void loadContactFirst(ICallBack<List<ContactDALEx>> callBack);
    }

    interface IContactView extends IBaseView {
        boolean checkNet();
        void showNoNet();
        void refreshMore(List<ContactDALEx> list);
        void loadMore(List<ContactDALEx> list);
        void onRefreshComplete();
        void onRefreshComplete(int result);
        void onLoadMoreComplete();
        void onLoadMoreComplete(int result);
    }
}
