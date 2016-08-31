package com.kw.app.chinesemedicine.activity;

import android.os.Bundle;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.mvp.view.fragment.ContactFragment;
import com.kw.app.chinesemedicine.mvp.view.fragment.DynamicFragment;
import com.kw.app.chinesemedicine.mvp.view.fragment.MySelfFragment;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.TabStripView;

import butterknife.Bind;

/**
 * @Description 主界面activity
 * @author wty
 **/
public class MainActivity extends BaseActivity {

    @Bind(R.id.navigateTabBar)
    TabStripView navigateTabBar;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        //对应xml中的containerId
        navigateTabBar.setFrameLayoutId(R.id.main_container);
        //对应xml中的navigateTabTextColor
        navigateTabBar.setTabTextColor(getResources().getColor(R.color.gray_font_3));
        //对应xml中的navigateTabSelectedTextColor
        navigateTabBar.setSelectedTabTextColor(getResources().getColor(R.color.colorPrimary));

        //恢复选项状态
        navigateTabBar.onRestoreInstanceState(savedInstanceState);

        navigateTabBar.addTab(DynamicFragment.class, new TabStripView.TabParam(R.drawable.ic_tab_home_normal, R.drawable.ic_tab_home_pressed, "发现"));
        navigateTabBar.addTab(ContactFragment.class, new TabStripView.TabParam(R.drawable.ic_tab_contact_normal, R.drawable.ic_tab_contact_pressed, "通讯录"));
        navigateTabBar.addTab(MySelfFragment.class,new TabStripView.TabParam(R.drawable.ic_tab_setting_normal,R.drawable.ic_tab_setting_pressed,"设置"));
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前选中的选项状态
        navigateTabBar.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }


}
