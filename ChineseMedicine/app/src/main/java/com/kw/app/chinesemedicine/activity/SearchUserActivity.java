package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.SearchUserAdapter;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.widget.ClearEditText;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.listener.FindListener;

/**
 * 搜索好友
 */
public class SearchUserActivity extends BaseActivity {

    @Bind(R.id.filter_edit)
    ClearEditText filterEdit;
    @Bind(R.id.rc_search)
    XRecyclerView rcSearch;
    @Bind(R.id.lv_loading)
    LoadingView lvLoading;

    public static void startSearchUserActivity(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

    SearchUserAdapter adapter;
    private List<UserBmob> mDataList = new ArrayList<UserBmob>();

    @OnClick(R.id.btn_search)
    public void onSearchClick(View view) {
        query();
    }

    public void query() {
        String name = filterEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showAppToast("请填写用户名");
            return;
        }
        BmobUserModel.getInstance().queryUsers(name, BmobUserModel.DEFAULT_LIMIT, new ICallBack<List<UserBmob>>() {
            @Override
            public void onSuccess(List<UserBmob> list) {
                adapter.retsetData(list);
            }

            @Override
            public void onFaild(String msg) {
                adapter.clearData();
                showAppToast(msg);
            }
        });
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("搜索好友");

        adapter = new SearchUserAdapter(this,mDataList);
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
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search_user;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
}
