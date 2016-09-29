package com.kw.app.chinesemedicine.mvp.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.AccountSettingActivity;
import com.kw.app.chinesemedicine.activity.MyAccountActivity;
import com.orhanobut.logger.Logger;
import com.wty.app.library.fragment.BaseFragment;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.widget.HomeItemView;

import butterknife.Bind;

/**
 * 我的页面
 * @author kuangminan
 */
public class MySelfFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.start_user_profile)
    LinearLayout mResetImage;
    @Bind(R.id.mine_head)
    ImageView mHeadImage;
    @Bind(R.id.mine_name)
    TextView mName;
    @Bind(R.id.account_setting)
    HomeItemView mSettingAccount;
    @Bind(R.id.give_advise)
    HomeItemView mAdvise;
    @Bind(R.id.about_us)
    HomeItemView mAboutUs;



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
        mResetImage.setOnClickListener(this);
        //set the default drawable
        mSettingAccount.setNextPage(AccountSettingActivity.class);

    }

    @Override
    public void onResume(){
        super.onResume();
        //显示我自己的头像
        ImageLoaderUtil.loadCircle(getContext(), PreferenceUtil.getInstance().getLogoUrl(), R.drawable.login_account,mHeadImage);
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.start_user_profile:
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.account_setting:
                Logger.d("account_setting");
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));

        }

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
