package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.SystemMessageAdapter;
import com.kw.app.chinesemedicine.data.dalex.local.SystemMessageDALEx;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 系统消息列表
 * @author :wty
 */
public class SystemMessageActivity extends BaseActivity {

    @Bind(R.id.rc_view)
    XRecyclerView rcSearch;
    @Bind(R.id.lv_loading)
    LoadingView lvLoading;

    public static void startSystemMessageActivity(Context context) {
        Intent intent = new Intent(context, SystemMessageActivity.class);
        context.startActivity(intent);
    }

    SystemMessageAdapter adapter;
    private List<SystemMessageDALEx> mDataList = new ArrayList<SystemMessageDALEx>();

    public void query() {
        adapter.retsetData(SystemMessageDALEx.get().getAllMessage());
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("系统消息");
        getDefaultNavigation().getLeftButton().setText("消息");

        adapter = new SystemMessageAdapter(this,mDataList);
        rcSearch.setLayoutManager(new LinearLayoutManager(this));

        rcSearch.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        rcSearch.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        rcSearch.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                query();
            }

            @Override
            public void onLoadMore() {
                // 本地加载更多
            }
        });
        rcSearch.setAdapter(adapter);
        //进到页面更新已读状态
        SystemMessageDALEx.get().updateBatchStatus();
        query();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_systemmessage;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
}
