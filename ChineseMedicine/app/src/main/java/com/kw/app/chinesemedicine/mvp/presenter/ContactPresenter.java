package com.kw.app.chinesemedicine.mvp.presenter;

import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.model.ContactModel;
import com.kw.app.chinesemedicine.mvp.model.impl.IContactModel;
import com.kw.app.chinesemedicine.mvp.view.impl.IContactView;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * @author wty
 */
public class ContactPresenter extends BasePresenter<IContactView> {

    private IContactModel mContactModel;

    public ContactPresenter(){
        mContactModel = new ContactModel();
    }

    public void refreshMoreContact(){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onRefreshComplete();
            return;
        }

        mContactModel.refreshMoreContact(new ContactDALEx(), new ICallBack<List<ContactDALEx>>() {
            @Override
            public void onSuccess(List<ContactDALEx> data) {
                mView.onRefreshComplete(data.size());
                mView.refreshMore(data);
            }

            @Override
            public void onFaild(String msg) {

            }
        });
    }

    /**
     * @Description 加载更多朋友圈动态
     **/
    public void loadMoreDynamic(){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onLoadMoreComplete();
            return;
        }

        mContactModel.loadMoreContact(new ContactDALEx(), new ICallBack<List<ContactDALEx>>() {
            @Override
            public void onSuccess(List<ContactDALEx> data) {
                mView.onLoadMoreComplete(data.size());
                mView.loadMore(data);
            }

            @Override
            public void onFaild(String msg) {

            }
        });
    }

    public void loadContactFirst(){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onLoadMoreComplete();
            return;
        }
//        mContactModel.loadContactFirst(new ICallBack<List<ContactDALEx>>() {
//            @Override
//            public void onSuccess(List<ContactDALEx> data) {
//                mView.onLoadMoreComplete(data.size());
//                mView.loadMore(data);
//            }
//
//            @Override
//            public void onFaild(String msg) {
//
//            }
//        });
        mView.onRefreshComplete(0);
    }

}
