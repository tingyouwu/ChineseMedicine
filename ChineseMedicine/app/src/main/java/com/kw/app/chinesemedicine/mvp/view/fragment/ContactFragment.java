package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.ContactAdapter;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.presenter.ContactPresenter;
import com.kw.app.chinesemedicine.mvp.view.activity.ContactAddActivity;
import com.kw.app.chinesemedicine.mvp.view.impl.IContactView;
import com.kw.app.chinesemedicine.widget.ClearEditText;
import com.kw.app.chinesemedicine.widget.SideBar;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.utils.NetWorkUtils;
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
 * 通讯录
 * @author wty
 */
public class ContactFragment extends BaseFragment<ContactPresenter> implements IContactView{

    @Bind(R.id.filter_edit)
    ClearEditText et_filter;
    @Bind(R.id.filter_letters)
    SideBar filter_letters;
    @Bind(R.id.listview_dynamic)
    XRecyclerView listview;
    @Bind(R.id.dynamic_fl_loading)
    LoadingView mLoadingView;
    @Bind(R.id.tv_letter)
    TextView tv_letter;

    BaseRecyclerViewAdapter adapter;
    private List<ContactDALEx> mDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ContactPresenter getPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        adapter = new ContactAdapter(getContext(),mDataList);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.addItemDecoration(new DivItemDecoration(2, true));
        listview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        listview.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新根据最近修改时间这个限定去服务端拿
                mPresenter.refreshMoreContact();
            }

            @Override
            public void onLoadMore() {
                // 本地加载更多
            }

        });
        listview.setAdapter(adapter);

        mLoadingView.withOnEmptyListener(new OnEmptyListener() {
            @Override
            public void onClick() {
            }
        }).withOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
            }
        }).build();

        mPresenter.loadContactFirst();
        filter_letters.setTextView(tv_letter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
        activity.getDefaultNavigation().setRightButton("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactAddActivity.startContactAddActivity(getContext());
            }
        });
    }

    @Override
    public boolean checkNet() {
        return NetWorkUtils.isNetworkConnected(getContext());
    }

    @Override
    public void showNoNet() {
        if(adapter.getItemCount()==0){
            mLoadingView.setState(LoadingState.STATE_NO_NET);
        }else{
            mLoadingView.setVisibility(View.GONE);
            AppMsg.makeText(activity, "网络连接失败，请检查网路", AppMsg.STYLE_INFO).show();
        }
    }

    @Override
    public void refreshMore(List<ContactDALEx> list) {
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
    public void loadMore(List<ContactDALEx> list) {
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
