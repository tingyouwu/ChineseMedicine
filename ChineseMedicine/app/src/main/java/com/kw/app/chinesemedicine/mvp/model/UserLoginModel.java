package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobExceptionCode;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.utils.PreferenceUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty
 */
public class UserLoginModel implements IUserLoginContract.IUserLoginModel {

    @Override
    public void login(final Context context, final String name, final String psw, final boolean isAutoLogin, final ICallBack<String> callBack) {
        final UserBmob bu2 = new UserBmob();
        bu2.setUsername(name);
        bu2.setPassword(psw);
        //首先尝试用户名+密码登陆

        bu2.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                callBack.onSuccess(bu2.getObjectId());
                saveUserPreference(isAutoLogin,psw,bu2);
            }

            @Override
            public void onFailure(int i, String s) {
                //再次尝试以邮箱登陆
                bu2.loginByAccount(context,name, psw, new LogInListener<UserBmob>() {
                    @Override
                    public void done(UserBmob bmobUser, BmobException e) {
                        if(bmobUser!=null){
                            callBack.onSuccess(bmobUser.getObjectId());
                            saveUserPreference(isAutoLogin,psw,bmobUser);
                        }else{
                            callBack.onFaild(BmobExceptionCode.match(e.getErrorCode()));
                        }
                    }
                });
            }
        });
    }

    /**
     * @Decription 保存用户数据到preference
     **/
    private void saveUserPreference(boolean isAutoLogin,String psw,UserBmob bmobUser){
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, bmobUser.getUsername());
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastAccount, bmobUser.getObjectId());
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LogoUrl, bmobUser.getLogourl());
        if(isAutoLogin){
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, true);
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword,psw);
        }else{
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, false);
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword, "");
        }
    }

}
