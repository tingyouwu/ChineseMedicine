package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.devspark.appmsg.AppMsg;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.adapter.ConversationAdapter;
import com.kw.app.chinesemedicine.bean.RongConversation;
import com.kw.app.chinesemedicine.event.RefreshEvent;
import com.kw.app.chinesemedicine.mvp.contract.IMessageContract;
import com.kw.app.chinesemedicine.mvp.presenter.MessagePresenter;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.utils.AppLogUtil;
import com.wty.app.library.widget.loadingview.LoadingState;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.loadingview.OnEmptyListener;
import com.wty.app.library.widget.loadingview.OnRetryListener;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 消息(聊天/电话)
 * @author wty
 */
public class MessageFragment extends BaseFragment<MessagePresenter> implements IMessageContract.IMessageView {

    ConversationAdapter adapter;
    private List<RongConversation> mDataList = new ArrayList<RongConversation>();

    @Bind(R.id.rc_view)
    XRecyclerView listview;
    @Bind(R.id.lv_loading)
    LoadingView mLoadingView;

    @Override
    public MessagePresenter getPresenter() {
        return new MessagePresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        adapter = new ConversationAdapter(getContext(),mDataList);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        listview.setLoadingMoreEnabled(false);
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshConversations(getContext());
            }

            @Override
            public void onLoadMore() {
            }

        });
        listview.setAdapter(adapter);

        mLoadingView.withLoadedEmptyText("暂时没有新消息")
                    .withOnEmptyListener(new OnEmptyListener() {
                        @Override
                        public void onClick() {
                            mPresenter.getAllConversations(getContext());
                        }
                    })
                    .withOnRetryListener(new OnRetryListener() {
                        @Override
                        public void onRetry() {
                            mPresenter.getAllConversations(getContext());
                        }
                    }).build();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_conversation;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
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
    public void refreshMessage(List<RongConversation> list) {
        if(list.size()!=0){
            adapter.retsetData(list);
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
    public void doWorkOnResume() {
        mPresenter.getAllConversations(getContext());
    }

    @Override
    public void onRefreshComplete() {
        listview.refreshComplete();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        AppLogUtil.i("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
        mPresenter.refreshConversations(getContext());
    }

}
