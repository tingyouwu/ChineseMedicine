package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.widget.ClearEditText;
import com.kw.app.chinesemedicine.widget.SideBar;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.loadingview.LoadingView;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import butterknife.Bind;

/**
 * 通讯录
 * @author wty
 */
public class ContactFragment extends BaseFragment {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        filter_letters.setLettersList(new String[]{"A","B","C","D","E","F","G"});
        filter_letters.setTextView(tv_letter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
    }
}
