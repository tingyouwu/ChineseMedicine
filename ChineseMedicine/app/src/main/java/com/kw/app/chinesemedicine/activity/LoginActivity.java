package com.kw.app.chinesemedicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.base.CMApplication;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.fakeserver.FakeServer;
import com.kw.app.chinesemedicine.fakeserver.HttpUtil;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.kw.app.chinesemedicine.mvp.presenter.UserLoginPresenter;
import com.kw.app.chinesemedicine.widget.login.LoginInputView;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.utils.AppLogUtil;
import com.wty.app.library.utils.CommonUtil;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;
import io.rong.imlib.RongIMClient;

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
            BmobIM.connect(PreferenceUtil.getInstance().getLastAccount(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        AppLogUtil.i("connect success");
                        MainActivity.startMainActivity(LoginActivity.this);
                        finish();
                    } else {
                        AppLogUtil.e(e.getErrorCode() + "/" + e.getMessage());
                    }
                }
            });

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
                        mPresenter.login(CMApplication.getInstance(),mloginInputview.getAccount().toString(), mloginInputview.getPassword().toString(), mloginInputview.isRememberPsw());
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
    public void finishActivity(UserBmob user) {

        FakeServer.getToken(user, new HttpUtil.OnResponse() {
            @Override
            public void onResponse(int code, String body) {
                if (code != 200) {
                    Toast.makeText(LoginActivity.this, body, Toast.LENGTH_SHORT).show();
                    return;
                }

                String token;
                try {
                    JSONObject jsonObj = new JSONObject(body);
                    token = jsonObj.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Token 解析失败!", Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

//        BmobIM.connect(user.getObjectId(), new ConnectListener() {
//            @Override
//            public void done(String uid, BmobException e) {
//                if (e == null) {
//                    AppLogUtil.i("connect success");
//                    MainActivity.startMainActivity(LoginActivity.this);
//                    finish();
//                } else {
//                    AppLogUtil.e(e.getErrorCode() + "/" + e.getMessage());
//                }
//            }
//        });
    }


    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(CMApplication.getMyProcessName())) {
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {
                    AppLogUtil.d("LoginActivity--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String userid) {
                    AppLogUtil.d("LoginActivity--onSuccess---" + userid);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    AppLogUtil.d("LoginActivity--onError" + errorCode);
                }
            });
        }
    }
}
