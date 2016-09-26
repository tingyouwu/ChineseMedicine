package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.NewFriendAdapter;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 新朋友
 * @author :wty
 */
public class NewFriendActivity extends BaseActivity {

    @Bind(R.id.rc_view)
    XRecyclerView rcSearch;
    @Bind(R.id.lv_loading)
    LoadingView lvLoading;

    public static void startNewFriendActivity(Context context) {
        Intent intent = new Intent(context, NewFriendActivity.class);
        context.startActivity(intent);
    }

    NewFriendAdapter adapter;
    private List<NewFriendDALEx> mDataList = new ArrayList<NewFriendDALEx>();

    public void query() {
        adapter.retsetData(NewFriendDALEx.get().getAllNewFriend());
        rcSearch.refreshComplete();
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("新朋友");
        getDefaultNavigation().getLeftButton().setText("返回");

        adapter = new NewFriendAdapter(this,mDataList);
        rcSearch.setLayoutManager(new LinearLayoutManager(this));

        rcSearch.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        rcSearch.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        rcSearch.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新根据最近修改时间这个限定去服务端拿
                query();
            }

            @Override
            public void onLoadMore() {
                // 本地加载更多
            }

        });
        rcSearch.setAdapter(adapter);
        NewFriendDALEx.get().updateBatchStatus();
        query();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_friend_new;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
}
