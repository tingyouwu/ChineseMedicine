package com.kw.app.chinesemedicine.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactAddContract;
import com.kw.app.chinesemedicine.mvp.presenter.ContactAddPresenter;
import com.kw.app.chinesemedicine.mvp.presenter.DynamicAddPresenter;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.activity.ImageSelectorActivity;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.utils.NetWorkUtils;
import com.wty.app.library.widget.formitem.FormItemView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author wty
 * @Description 添加联系人
 **/
public class ContactAddActivity extends BaseActivity<ContactAddPresenter> implements IContactAddContract.IContactAddView {


    @Bind(R.id.item_name)
    FormItemView itemName;
    @Bind(R.id.item_age)
    FormItemView itemAge;
    @Bind(R.id.item_mobile)
    FormItemView itemMobile;
    @Bind(R.id.item_tel)
    FormItemView itemTel;

    public static void startContactAddActivity(Context context) {
        Intent intent = new Intent(context, ContactAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    public ContactAddPresenter getPresenter() {
        return new ContactAddPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("新增联系人");
        getDefaultNavigation().getLeftButton().setText("通讯录");
        getDefaultNavigation().setRightButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {//从选择图片页面返回
            if (requestCode == AppConstant.ActivityResult.Request_Image) {
                //拿到返回的图片路径
                boolean isCamera = data.getBooleanExtra(ImageSelectorActivity.OUTPUT_ISCAMERA, false);
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);

            }
        }
    }

    @Override
    protected boolean submit() {
        if (super.submit()) {
            mPresenter.submit(ContactAddActivity.this,getSubmitData());
        }
        return true;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_contact_add;
    }

    private ContactDALEx getSubmitData() {
        ContactDALEx dalex = ContactDALEx.get();
        dalex.setUsername(itemName.getContent().getValue());
        dalex.setMobilephone(itemMobile.getContent().getValue());
        dalex.setAge(Integer.parseInt(itemAge.getContent().getValue()));
        dalex.setTel(itemTel.getContent().getValue());
        return dalex;
    }

    @Override
    public boolean checkNet() {
        return NetWorkUtils.isNetworkConnected(this);
    }

    @Override
    public void showNoNet() {
        showFailed(getString(R.string.network_failed));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
