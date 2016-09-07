package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.widget.CheckBoxLabel;
import com.kw.app.chinesemedicine.widget.CheckCodeButton;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.activity.ImageSelectorActivity;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author wty
 * @Description 用户注册页面
 **/
public class UserRegisterActivity extends BaseActivity {

    @Bind(R.id.img_camera)
    ImageView imgCamera;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.checkcode)
    CheckCodeButton checkcode;
    @Bind(R.id.et_check)
    EditText etCheck;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.btn_showpsw)
    CheckBoxLabel btnShowpsw;
    @Bind(R.id.btn_sign)
    Button btnSign;

    @OnClick(R.id.img_camera)
    void selectHeaderIcon(){
        ImageSelectorActivity.start(UserRegisterActivity.this,1,ImageSelectorActivity.MODE_SINGLE,true,true,false,null);
    }

    private String path_header = "";

    public static void startUserRegisterActivity(Context context) {
        Intent intent = new Intent(context, UserRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("注册账号");
        getDefaultNavigation().getLeftButton().hide();
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
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
                boolean isCamera = data.getBooleanExtra(ImageSelectorActivity.OUTPUT_ISCAMERA, false);
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                if(images != null && images.size()>0){
                    this.path_header = images.get(0);
                    imgCamera.setPadding(0,0,0,0);
                    ImageLoaderUtil.load(this,this.path_header,imgCamera);
                }
            }
        }
    }

}
