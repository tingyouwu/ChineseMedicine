package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.SearchUserActivity;
import com.kw.app.chinesemedicine.adapter.ContactAdapter;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactContract;
import com.kw.app.chinesemedicine.mvp.presenter.ContactPresenter;
import com.kw.app.chinesemedicine.widget.ClearEditText;
import com.kw.app.chinesemedicine.widget.SideBar;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.widget.DivItemDecoration;
import com.wty.app.library.widget.loadingview.LoadingState;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.loadingview.OnEmptyListener;
import com.wty.app.library.widget.loadingview.OnRetryListener;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 通讯录
 * @author wty
 */
public class ContactFragment extends BaseFragment<ContactPresenter> implements IContactContract.IContactView {

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

    ContactAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private List<UserDALEx> mDataList = new ArrayList<>();

    @Override
    public ContactPresenter getPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        filter_letters.setTextView(tv_letter);
        adapter = new ContactAdapter(getContext(),mDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(linearLayoutManager);
        listview.addItemDecoration(new DivItemDecoration(2, true));
        listview.setLoadingMoreEnabled(false);
        listview.setPullRefreshEnabled(false);
        listview.setAdapter(adapter);

        filter_letters.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    linearLayoutManager.scrollToPositionWithOffset(position-1,0);
                }
            }
        });

        mLoadingView.withLoadedEmptyText("暂时没有联系人")
                    .withOnEmptyListener(new OnEmptyListener() {
                        @Override
                        public void onClick() {
                        }
                    }).withOnRetryListener(new OnRetryListener() {
                        @Override
                        public void onRetry() {
                        }
                    }).build();

        mPresenter.loadAllFriend();
        mPresenter.refreshFriend(getContext());

    }

    @Override
    public void doWorkOnResume() {

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
                SearchUserActivity.startSearchUserActivity(getActivity());
            }
        });
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
    public void refreshFriend(List<UserDALEx> list) {
        if(list.size()!=0){
            filter_letters.setLettersList(getSortLetter(list));
            adapter.retsetData(list);
            mLoadingView.setVisibility(View.GONE);
            listview.setNoMore(list.size() + "位联系人");
        }else{
            if(adapter.getItemCount()==0){
                mLoadingView.setState(LoadingState.STATE_EMPTY);
            }else{
                mLoadingView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取排序的首字母
     **/
    public List<String> getSortLetter(List<UserDALEx> list){
        Map<String,String> letterMap = new LinkedHashMap<>();
        List<String> letters = new ArrayList<String>();

        for(UserDALEx user:list){
            String sortString = user.getPinyin().substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                letterMap.put(sortString,sortString);
            }else{
                letterMap.put("#","#");
            }
        }

        letters.addAll(letterMap.values());
        return letters;
    }

}
