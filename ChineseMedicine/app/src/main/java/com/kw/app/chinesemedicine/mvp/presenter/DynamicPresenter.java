package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicContract;
import com.kw.app.chinesemedicine.mvp.model.DynamicModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * @author wty
 */
public class DynamicPresenter extends BasePresenter<IDynamicContract.IDynamicView> {

    private IDynamicContract.IDynamicModel mDynamicModel;

    public DynamicPresenter(){
        mDynamicModel = new DynamicModel();
    }

    /**
     * @Description 刷新更多朋友圈动态
     **/
    public void refreshMoreDynamic(Context context){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onRefreshComplete();
            return;
        }

        mDynamicModel.refreshMoreDynamic(context,new DynamicDALEx(), new ICallBack<List<DynamicDALEx>>() {
            @Override
            public void onSuccess(List<DynamicDALEx> data) {
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
    public void loadMoreDynamic(Context context){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onLoadMoreComplete();
            return;
        }

        mDynamicModel.loadMoreDynamic(context,new DynamicDALEx(), new ICallBack<List<DynamicDALEx>>() {
            @Override
            public void onSuccess(List<DynamicDALEx> data) {
                mView.onLoadMoreComplete(data.size());
                mView.loadMore(data);
            }

            @Override
            public void onFaild(String msg) {

            }
        });
    }

    public void loadDynamicFirst(){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onLoadMoreComplete();
            return;
        }
        mView.onRefreshComplete(0);
    }

}
