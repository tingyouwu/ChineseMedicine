package com.kw.app.chinesemedicine.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.widget.login.LoginInputView;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.CommonUtil;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.widget.imageview.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author wty
 * @Description 注册/登陆界面
 **/
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_icon)
    CircleImageView mloginIcon;
    @Bind(R.id.login_inputview)
    LoginInputView mloginInputview;
    @Bind(R.id.login_version)
    TextView tv_version;
    @Bind(R.id.login_forgetpsw)
    TextView tv_forgetpsw;
    @Bind(R.id.login_signup)
    TextView tv_signup;

    @OnClick(R.id.login_signup)
    void goToRegisterActivity(){
        UserRegisterActivity.startUserRegisterActivity(this);
    }
    @OnClick(R.id.login_forgetpsw)
    void showForgetPswMenu(){

    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("老中医");
        getDefaultNavigation().getLeftButton().hide();

        //点击登陆后做的事情
        mloginInputview.setOnLoginAction(new LoginInputView.OnLoginActionListener() {
            @Override
            public void onLogin() {
                CommonUtil.keyboardControl(LoginActivity.this,false,mloginInputview.getAccountInput());
                if(submit()){

                }
            }
        });

        ImageLoaderUtil.load(this, R.drawable.icon_launcher, mloginIcon);
        tv_version.setText("V"+ CommonUtil.getVersion(this)+"."+CommonUtil.getVersionCode(this));

        //最近登录的帐号
        String lastOriginalAccount = PreferenceUtil.getInstance().getLastAccount();
        //登录密码
        String lastPsw = PreferenceUtil.getInstance().getLastPassword();
        //是否记住了密码  是就自动登陆
        boolean isAutoLogin = PreferenceUtil.getInstance().isAutoLogin();
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        list.add(mloginInputview.validata());
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login;
    }
}
