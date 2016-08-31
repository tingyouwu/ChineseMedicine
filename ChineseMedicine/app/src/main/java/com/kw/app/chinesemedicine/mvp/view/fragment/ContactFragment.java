package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.os.Bundle;

import com.kw.app.chinesemedicine.R;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.mvp.presenter.BasePresenter;

/**
 * 通讯录
 * @author wty
 */
public class ContactFragment extends BaseFragment {

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

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_myself;
    }

    @Override
    public void initFragmentActionBar(String title) {
        super.initFragmentActionBar(title);
    }
}
