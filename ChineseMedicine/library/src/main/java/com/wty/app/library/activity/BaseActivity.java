package com.wty.app.library.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.wty.app.library.R;
import com.wty.app.library.mvp.IBase;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.mvp.view.IBaseView;
import com.wty.app.library.utils.NetWorkUtils;
import com.wty.app.library.utils.SystemBarTintManager;
import com.wty.app.library.widget.navigation.NavigationText;
import com.wty.app.library.widget.sweetdialog.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author wty
 * 所有activity的基类
 * 一个高大上的名字：模版方法设计模式
 **/
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBase<P> {

    private NavigationText navigation;
    protected P mPresenter;
    protected View mRootView;
    private SystemBarTintManager tintManager;//沉浸式状态栏
    public SweetAlertDialog loadingdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null && this instanceof IBaseView) {
            mPresenter.attachView((IBaseView) this);
        }

        initStatusBar();
        mRootView = LayoutInflater.from(this).inflate(getLayoutResource(), null);
        setContentView(mRootView);
        ButterKnife.bind(this, mRootView);
        onInitView(savedInstanceState);
        setNavigation(getDefaultNavigation());
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null && this instanceof IBaseView) {
            mPresenter.detachView();
            mPresenter = null;
        }

        if (loadingdialog != null && loadingdialog.isShowing()) {
            loadingdialog.dismiss();
        }
    }

    /**
     * 功能描述：是否设置沉浸式(默认不打开)
     * @return
     */
    protected boolean isEnableStatusBar() {
        return false;
    }

    @TargetApi(19)
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && isEnableStatusBar()) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.blue);
        }
        else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP && isEnableStatusBar()) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
        }

    }

    /**
     * 功能描述：设置状态栏的颜色
     **/
    protected void setStatusBarTintRes(int color) {
        if (tintManager != null) {
            tintManager.setStatusBarTintResource(color);
        }
    }

    /**
     * 功能描述：设置标题栏
     **/
    private void setNavigation(View navigation){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(navigation, layoutParams);
            actionBar.show();
        }
    }

    public NavigationText getDefaultNavigation(){
        if(navigation==null){
            navigation = new NavigationText(this);
        }
        return navigation;
    }

    /**
     * @Decription 校验数据
     **/
    protected List<String> validate(){
        return new ArrayList<String>();
    }

    /**
     * @Decription 提交数据
     * 第一步：校验数据
     * 第二步：校验网络是否联通
     **/
    protected boolean submit(){
        List<String> validate = validate();
        if(validate.size()!=0){
            showFailed(validate.get(0));
            return false;
        }else if(!NetWorkUtils.isNetworkConnected(this)){
            showSuccess(getString(R.string.network_failed));
            return false;
        }else{
            return true;
        }
    }

    /**
     * @Decription 弹框提示
     **/
    public void onToast(OnDismissCallbackListener callback){
        SweetAlertDialog dialog = new SweetAlertDialog(this,callback.alertType);
        dialog.setTitleText(callback.msg)
                .setConfirmText("确定")
                .setConfirmClickListener(callback)
                .changeAlertType(callback.alertType);
        dialog.show();
    }

    public void showSuccess(final String msg){
        onToast(new OnDismissCallbackListener(msg, SweetAlertDialog.SUCCESS_TYPE));
    }

    public void showFailed(final String msg){
        onToast(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
    }

    /**
     * @Decription 提示加载中
     **/
    public void showLoading(String msg){
        if(this.isFinishing())return;
        if(loadingdialog==null || !loadingdialog.isShowing()){
            loadingdialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(msg);
            loadingdialog.setCancelable(false);
            loadingdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                int countDestroyBack = 0;
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        countDestroyBack++;
                        if(countDestroyBack == 3){
                            loadingdialog.dismiss();
                        }
                    }
                    return false;
                }
            });
            loadingdialog.show();

        }
    }

    public void dismissLoading(){
        dismissLoading(null);
    }

    public void dismissLoading(final OnDismissCallbackListener callback){
        if(loadingdialog!=null && loadingdialog.isShowing()){
            new CountDownTimer(500,1000) {
                //一些提交会比较快 所以需要500ms缓冲
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if(callback == null){
                        loadingdialog.dismiss();
                    }else{
                        loadingdialog.setTitleText(callback.msg)
                                .setConfirmText("确定")
                                .setConfirmClickListener(callback)
                                .changeAlertType(callback.alertType);
                    }
                }
            }.start();
        }
    }

    /**
     * 提交前提示确认按钮
     **/
    protected void confirmSubmit(String title){
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText(title);

        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                submit();
            }
        });
        dialog.setCancelText("取消");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        dialog.show();
    }

}
