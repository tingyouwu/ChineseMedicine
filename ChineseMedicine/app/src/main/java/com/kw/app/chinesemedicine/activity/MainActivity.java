package com.kw.app.chinesemedicine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.mvp.view.fragment.ContactFragment;
import com.kw.app.chinesemedicine.mvp.view.fragment.DynamicFragment;
import com.kw.app.chinesemedicine.mvp.view.fragment.MySelfFragment;
import com.orhanobut.logger.Logger;
import com.wty.app.bmobim.event.RefreshEvent;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.AppLogUtil;
import com.wty.app.library.widget.TabStripView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

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

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
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

        UserBmob user = BmobUser.getCurrentUser(this, UserBmob.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    AppLogUtil.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });

        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {

                if(status.getCode()==ConnectionStatus.CONNECTED.getCode()){
                    //连接成功
                }else if(status.getCode()==ConnectionStatus.CONNECTING.getCode()){
                    //正在连接
                }else if(status.getCode()==ConnectionStatus.DISCONNECT.getCode()){
                    //断开连接
                }else if(status.getCode()==ConnectionStatus.KICK_ASS.getCode()){
                    //被人踢下线
                }

                showAppToast("" + status.getMsg());
            }
        });
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
