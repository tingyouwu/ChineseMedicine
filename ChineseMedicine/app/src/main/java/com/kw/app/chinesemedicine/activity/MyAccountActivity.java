package com.kw.app.chinesemedicine.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kw.app.chinesemedicine.R;
import com.orhanobut.logger.Logger;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.PhotoUtils;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.widget.BottomMenuDialog;
import com.wty.app.library.widget.SelectableRoundedImageView;

import java.io.IOException;

import butterknife.Bind;

/**
 * 作者：samsung on 2016/9/23 14:02
 * 邮箱：kuangminan456123@163.com
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.rl_my_portrait)
    RelativeLayout mMyPortrait;
    @Bind(R.id.rl_my_username)
    RelativeLayout mUserName;
    @Bind(R.id.img_my_portrait)
    SelectableRoundedImageView mImageView;
    @Bind(R.id.tv_my_username)
    TextView mMyName;
    @Bind(R.id.tv_my_id)
    TextView mMyId;
    private Uri selectUri;

    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private static final int UP_LOAD_PORTRAIT = 8;
    private static final int GET_QI_NIU_TOKEN = 128;
    private BottomMenuDialog dialog;
    private PhotoUtils photoUtils;

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //昵称更改返回后需要重新设置
        String name = PreferenceUtil.getInstance().getLastName();
        mMyName.setText(name);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("修改个人信息");
        mMyPortrait.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        String name = PreferenceUtil.getInstance().getLastName();
        String mMyid = PreferenceUtil.getInstance().getLastAccount();
        mMyName.setText(name);
        mMyName.setOnClickListener(this);
        mMyId.setText(mMyid);


        setPortraitChangeListener();//回调监听头像
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_my_count;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.rl_my_portrait:
                showPhotoDialog();
                break;
            case R.id.rl_my_username:
                startActivity(new Intent(this, UpdateNameActivity.class));
                break;
        }
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                Logger.d("KMA ...onPhotoResult uri:"+uri);
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                   // request(GET_QI_NIU_TOKEN);  服务器更新
                    try {
                        //需要申请存储权限
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyAccountActivity.this.getContentResolver(),uri);
                        mImageView.setImageBitmap(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    /**
     * 弹出底部框
     */
    @TargetApi(23)
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(this);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
                    int checkStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    Logger.d("KMA ...checkCameraPermission: "+checkCameraPermission);
                    Logger.d("KMA ...checkStoragePermission: "+checkStoragePermission);
                    if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            new AlertDialog.Builder(MyAccountActivity.this)
                                    .setMessage("您需要在设置里打开相机权限。")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create().show();
                        }
                    }
                    if(checkStoragePermission != PackageManager.PERMISSION_GRANTED){
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            new AlertDialog.Builder(MyAccountActivity.this)
                                    .setMessage("您需要在设置里打开存储权限。")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create().show();
                        }
                        return;
                    }

                }
                photoUtils.takePicture(MyAccountActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.selectPicture(MyAccountActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(MyAccountActivity.this, requestCode, resultCode, data);
                break;
        }
    }

}
