package com.kw.app.chinesemedicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.kw.app.chinesemedicine.mvp.presenter.UserLoginPresenter;
import com.kw.app.chinesemedicine.widget.login.LoginInputView;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.base.AppConstant;
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
public class LoginActivity extends BaseActivity<UserLoginPresenter> implements IUserLoginContract.IUserLoginView{

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
        UserRegisterActivity.startUserRegisterActivity(this, AppConstant.ActivityResult.Request_Register);
    }
    @OnClick(R.id.login_forgetpsw)
    void showForgetPswMenu(){

    }


    @Override
    public UserLoginPresenter getPresenter() {
        return new UserLoginPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {

        getDefaultNavigation().setTitle("老中医");
        getDefaultNavigation().getLeftButton().hide();

        //最近登录的帐号
        String lastOriginalAccount = PreferenceUtil.getInstance().getLastAccount();
        //登录密码
        String lastPsw = PreferenceUtil.getInstance().getLastPassword();
        //是否记住了密码  是就自动登陆
        final boolean isAutoLogin = PreferenceUtil.getInstance().isAutoLogin();

        if(isAutoLogin){
            //自动登录就调整到主页面
            finishActivity();
        }

        //点击登陆后做的事情
        mloginInputview.setOnLoginAction(new LoginInputView.OnLoginActionListener() {
            @Override
            public void onLogin() {
                CommonUtil.keyboardControl(LoginActivity.this,false,mloginInputview.getAccountInput());
                if(submit()){
                    mPresenter.login(mloginInputview.getAccount(),mloginInputview.getPassword(),mloginInputview.isRememberPsw());
                }
            }
        });

        ImageLoaderUtil.load(this, R.drawable.icon_launcher, mloginIcon);
        tv_version.setText("V"+ CommonUtil.getVersion(this)+"."+CommonUtil.getVersionCode(this));

        if (lastOriginalAccount != null) {
            mloginInputview.setAccount(lastOriginalAccount);
            mloginInputview.setPassword(lastPsw);
            if (!TextUtils.isEmpty(lastPsw)) {
                // 记住密码
                mloginInputview.setIsRememberPsw(true);
            } else {
                // 不记住密码
                mloginInputview.setIsRememberPsw(false);
            }
        } else {
            // 第一次使用，默认记住密码
            mloginInputview.setIsRememberPsw(true);
        }
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        if(!TextUtils.isEmpty(mloginInputview.validata()))
            list.add(mloginInputview.validata());
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {//从选择图片页面返回
            if (requestCode == AppConstant.ActivityResult.Request_Register) {
                String name = data.getStringExtra(UserRegisterActivity.USERNAME);
                String psw = data.getStringExtra(UserRegisterActivity.PSW);
                mloginInputview.getAccountInput().setText(name);
                mloginInputview.getPswInput().setText(psw);
            }
        }
    }

    @Override
    public void finishActivity() {
        MainActivity.startMainActivity(this);
        finish();
    }
}
