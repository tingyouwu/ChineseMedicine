package com.kw.app.chinesemedicine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.kw.app.chinesemedicine.R;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.List;

import butterknife.Bind;

/**
 * @author wty
 * @Description 用户邮箱重置密码
 **/
public class UserEmailResetPSWActivity extends BaseActivity {

    @Bind(R.id.et_email)
    EditText etEmail;

    public static void startUserEmailResetPSWActivity(Activity activity) {
        Intent intent = new Intent(activity, UserEmailResetPSWActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("重置密码");
        getDefaultNavigation().getLeftButton().setText("老中医");
//        getDefaultNavigation().
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            list.add("请填写邮箱");
        }
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_email_resetpsw;
    }
}
