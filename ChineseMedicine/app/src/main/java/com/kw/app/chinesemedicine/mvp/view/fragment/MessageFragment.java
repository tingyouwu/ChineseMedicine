package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.devspark.appmsg.AppMsg;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.bean.Conversation;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicContract;
import com.kw.app.chinesemedicine.mvp.presenter.DynamicPresenter;
import com.kw.app.chinesemedicine.mvp.view.activity.DynamicAddActivity;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.widget.loadingview.LoadingState;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 消息(聊天/电话)
 * @author wty
 */
public class MessageFragment extends BaseFragment<DynamicPresenter> implements IDynamicContract.IDynamicView {

    BaseRecyclerViewAdapter adapter;
    private List<Conversation> mDataList = new ArrayList<Conversation>();

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

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
        activity.getDefaultNavigation().setRightButton("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicAddActivity.startDynamicAddActivity(getActivity());
            }
        });
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
        if(list.size()==0)return;
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
        if(result == 0){
            listview.setNoMore("亲,已经是最后一页了！");
        }
    }
}
