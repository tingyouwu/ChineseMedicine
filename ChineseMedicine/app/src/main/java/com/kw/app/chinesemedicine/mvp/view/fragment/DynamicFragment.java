package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.devspark.appmsg.AppMsg;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.DynamicAdapter;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.presenter.DynamicPresenter;
import com.kw.app.chinesemedicine.mvp.view.impl.IDynamicView;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.widget.DivItemDecoration;
import com.wty.app.library.widget.loadingview.LoadingState;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.loadingview.OnEmptyListener;
import com.wty.app.library.widget.loadingview.OnRetryListener;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 朋友圈
 * @author wty
 */
public class DynamicFragment extends BaseFragment<DynamicPresenter> implements IDynamicView{

    BaseRecyclerViewAdapter adapter;
    private List<DynamicDALEx> mDataList = new ArrayList<>();

    @Bind(R.id.listview_dynamic)
    XRecyclerView listview;
    @Bind(R.id.dynamic_fl_loading)
    LoadingView mLoadingView;

    @Override
    public DynamicPresenter getPresenter() {
        return new DynamicPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        adapter = new DynamicAdapter(getContext(),mDataList);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.addItemDecoration(new DivItemDecoration(15, false));
        listview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        listview.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshMoreDynamic();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreDynamic();
            }

        });
        listview.setAdapter(adapter);

        mLoadingView.withOnEmptyListener(new OnEmptyListener() {
            @Override
            public void onClick() {
                mPresenter.refreshMoreDynamic();
            }
        }).withOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                mPresenter.refreshMoreDynamic();
            }
        }).build();

        // 初始化进入页面加载数据
        mPresenter.loadDynamicFirst();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
    }


    @Override
    public boolean checkNet() {
        return false;
    }

    @Override
    public void showNoNet() {
        if(adapter.getItemCount()==0){
            mLoadingView.setState(LoadingState.STATE_NO_NET);
        }else{
            mLoadingView.setVisibility(View.GONE);
            AppMsg.makeText(activity,"网络连接失败，请检查网路",AppMsg.STYLE_INFO).show();
        }
    }

    @Override
    public void refreshMore(List<DynamicDALEx> list) {
        if(list.size()!=0){
            adapter.addData(list);
            mLoadingView.setVisibility(View.GONE);
        }else{
            if(adapter.getItemCount()==0){
                mLoadingView.setState(LoadingState.STATE_EMPTY);
            }else{
                mLoadingView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void loadMore(List<DynamicDALEx> list) {

    }

    @Override
    public void onRefreshComplete() {
        listview.refreshComplete();
    }

    @Override
    public void onRefreshComplete(int result) {
        if(result==0 && adapter.getItemCount()==0){
            mLoadingView.setState(LoadingState.STATE_EMPTY);
        }else{
            if(mLoadingView.getVisibility()==View.VISIBLE)
                mLoadingView.setVisibility(View.GONE);
        }
        listview.refreshComplete(result + "条新内容");
    }

    @Override
    public void onLoadMoreComplete() {
        listview.loadMoreComplete();
    }

    @Override
    public void onLoadMoreComplete(int result) {
        listview.loadMoreComplete();
        if(result == 0 && adapter.getItemCount()!=0){
            listview.setNoMore("亲,已经是最后一页了！");
        }
    }
}
