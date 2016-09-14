package com.kw.app.chinesemedicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.kw.app.chinesemedicine.mvp.presenter.UserLoginPresenter;
import com.kw.app.chinesemedicine.widget.login.LoginInputView;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.utils.CommonUtil;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author wty
 * @Description 注册/登陆界面
 **/
public class LoginActivity extends BaseActivity<UserLoginPresenter> implements IUserLoginContract.IUserLoginView{

    @Bind(R.id.login_icon)
    ImageView mloginIcon;
    @Bind(R.id.login_inputview)
    LoginInputView mloginInputview;
    @Bind(R.id.login_version)
    TextView tv_version;

    @OnClick(R.id.login_signup)
    void goToRegisterActivity(){
        UserRegisterActivity.startUserRegisterActivity(this, AppConstant.ActivityResult.Request_Register);
    }
    @OnClick(R.id.login_forgetpsw)
    void showForgetPswMenu(){
        UserEmailResetPSWActivity.startUserEmailResetPSWActivity(LoginActivity.this);
    }

    private String userid;

    @Override
    public UserLoginPresenter getPresenter() {
        return new UserLoginPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {

        getDefaultNavigation().setTitle("老中医");
        getDefaultNavigation().getLeftButton().hide();
        CommonUtil.keyboardControl(LoginActivity.this, false, mloginInputview.getAccountInput());

        final boolean isAutoLogin = PreferenceUtil.getInstance().isAutoLogin();

        if(isAutoLogin){//自动登录就调整到主页面
            finishActivity();

        }else{
            String lastOriginalAccount = PreferenceUtil.getInstance().getLastName();
            String lastPsw = PreferenceUtil.getInstance().getLastPassword();
            String logourl = PreferenceUtil.getInstance().getLogoUrl();
            //点击登陆后做的事情
            mloginInputview.setOnLoginAction(new LoginInputView.OnLoginActionListener() {
                @Override
                public void onLogin() {
                    CommonUtil.keyboardControl(LoginActivity.this, false, mloginInputview.getAccountInput());
                    if (submit()) {
                        mPresenter.login(LoginActivity.this,mloginInputview.getAccount().toString(), mloginInputview.getPassword().toString(), mloginInputview.isRememberPsw());
                    }
                }
            });

            if(!TextUtils.isEmpty(logourl)){
                ImageLoaderUtil.loadCircle(mloginIcon.getContext(),logourl,mloginIcon);
            }
            tv_version.setText("V"+ CommonUtil.getVersion(this)+"."+CommonUtil.getVersionCode(this));

            if (lastOriginalAccount != null) {
                mloginInputview.setAccount(lastOriginalAccount);
                mloginInputview.setPassword(lastPsw);
                if (!TextUtils.isEmpty(lastPsw)) {
                    mloginInputview.setIsRememberPsw(true);
                } else {
                    mloginInputview.setIsRememberPsw(false);
                }
            } else {// 第一次使用，默认记住密码
                mloginInputview.setIsRememberPsw(true);
            }
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
                userid = data.getStringExtra(UserRegisterActivity.USERID);
                UserDALEx user = UserDALEx.get().findById(userid);
                mloginInputview.getAccountInput().setText(user.getNickname());
                if(!TextUtils.isEmpty(user.getLogourl())){
                    ImageLoaderUtil.load(mloginIcon.getContext(),user.getLogourl(),mloginIcon);
                }
            }
        }
    }

    @Override
    public void finishActivity() {
        MainActivity.startMainActivity(this);
        finish();
    }
}
