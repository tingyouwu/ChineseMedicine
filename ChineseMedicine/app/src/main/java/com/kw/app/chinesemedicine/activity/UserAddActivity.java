package com.kw.app.chinesemedicine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IUserRegisterContract;
import com.kw.app.chinesemedicine.mvp.presenter.UserRegisterPresenter;
import com.kw.app.chinesemedicine.widget.CheckBoxLabel;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.activity.ImageSelectorActivity;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author wty
 * @Description 关注
 **/
public class UserAddActivity extends BaseActivity<UserRegisterPresenter> implements IUserRegisterContract.IUserRegisterView {

    public static final String USERID = "userid";

    @Bind(R.id.img_camera)
    ImageView imgCamera;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.btn_showpsw)
    CheckBoxLabel btnShowpsw;
    @Bind(R.id.tb_role)
    ToggleButton tb_role;//包邮
    @Bind(R.id.et_email)
    EditText etEmail;

    @OnClick(R.id.img_camera)
    void selectHeaderIcon(){
        //获取头像
        ImageSelectorActivity.start(UserAddActivity.this,1,ImageSelectorActivity.MODE_SINGLE,true,true,false,null);
    }

    @OnClick(R.id.btn_sign)
    void sign(){
        //注册
        if(super.submit()){
            mPresenter.register(getSubmitData());
        }
    }

    private String path_header = "";
    boolean isRoleOn;//默认患者

    public static void startUserRegisterActivity(Activity activity,int RequestCode) {
        Intent intent = new Intent(activity, UserAddActivity.class);
        activity.startActivityForResult(intent,RequestCode);
    }

    @Override
    public UserRegisterPresenter getPresenter() {
        return new UserRegisterPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("注册账号");
        getDefaultNavigation().getLeftButton().setText("老中医");

        btnShowpsw.setOnCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    // 显示为普通文本
                    etPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // 显示为密码
                    etPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable etable = etPsw.getText();
                Selection.setSelection(etable, etable.length());
            }
        });

        tb_role.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                isRoleOn = on;
            }
        });

    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        if(TextUtils.isEmpty(etName.getText().toString())){
            list.add("请填写昵称");
        }else if(TextUtils.isEmpty(etPsw.getText().toString())){
            list.add("请填写密码");
        }else if(TextUtils.isEmpty(etEmail.getText().toString())){
            list.add("请填写邮箱");
        }
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_register;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {//从选择图片页面返回
            if (requestCode == AppConstant.ActivityResult.Request_Image) {
                //拿到返回的图片路径
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                if(images != null && images.size()>0){
                    this.path_header = images.get(0);
                    imgCamera.setPadding(0,0,0,0);
                    ImageLoaderUtil.load(imgCamera.getContext(),this.path_header,imgCamera);
                }
            }
        }
    }

    private UserDALEx getSubmitData(){
        UserDALEx user = UserDALEx.get();
        user.setNickname(etName.getText().toString());
        user.setPassword(etPsw.getText().toString());
        user.setRole(isRoleOn ? UserDALEx.User_Doctor : UserDALEx.User_Not_Doctor);
        user.setLogourl(path_header);
        user.setEmail(etEmail.getText().toString());
        return user;
    }

    @Override
    public void finishActivity(String userid) {
        Intent intent = new Intent();
        intent.putExtra(USERID,userid);
        setResult(RESULT_OK, intent);
        finish();
    }
}
