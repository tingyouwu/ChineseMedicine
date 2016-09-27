package com.kw.app.chinesemedicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.widget.ClearEditText;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.PreferenceUtil;

import butterknife.Bind;

/**
 * 作者：samsung on 2016/9/26 21:35
 * 邮箱：kuangminan456123@163.com
 */
public class UpdateNameActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.name_edit)
    ClearEditText mNameEdit;
    @Bind(R.id.btn_name_update)
    Button nBtnNameUpdate;

    private String mNewName = null;


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
        getDefaultNavigation().setTitle("修改昵称");
        nBtnNameUpdate.setOnClickListener(this);


    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_update_name;
    }

    @Override
    public void onClick(View v){
        mNewName = mNameEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(mNewName)) {
           // request(UPDATE_NAME, true); 服务器更新
            // 本地preference更新
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, mNewName);
            showAppToast("昵称修改成功");
            //需要在服务器更新完后再回调finish当前activity
            finish();
        } else {
            showAppToast("昵称不能为空");
        }
    }

}
