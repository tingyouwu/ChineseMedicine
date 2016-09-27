package com.kw.app.chinesemedicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kw.app.chinesemedicine.R;
import com.orhanobut.logger.Logger;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.PreferenceUtil;

import butterknife.Bind;

/**
 * 作者：samsung on 2016/9/27 13:30
 * 邮箱：kuangminan456123@163.com
 */
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.account_change_pswd)
    RelativeLayout mChangePsw;
    @Bind(R.id.add_to_black_list)
    RelativeLayout mAddBlackList;
    @Bind(R.id.notify_setting)
    RelativeLayout mNotifySetting;
    @Bind(R.id.ac_set_exit)
    Button mAccountExit;



    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        Logger.d("KMA ...onInitView");
        getDefaultNavigation().setTitle("账号设置");
        mChangePsw.setOnClickListener(this);
        mAddBlackList.setOnClickListener(this);
        mNotifySetting.setOnClickListener(this);

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_count_setting;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.account_change_pswd:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                //modify the pwd
            case R.id.add_to_black_list:
                //add to black_list
            case R.id.notify_setting:
                //set the notification type
            case R.id.ac_set_exit:

        }
    }

}
