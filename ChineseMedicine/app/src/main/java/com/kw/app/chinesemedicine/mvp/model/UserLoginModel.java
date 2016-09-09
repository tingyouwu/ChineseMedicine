package com.kw.app.chinesemedicine.mvp.model;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobExceptionCode;
import com.kw.app.chinesemedicine.mvp.contract.IUserLoginContract;
import com.wty.app.library.callback.ICallBack;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty
 */
public class UserLoginModel implements IUserLoginContract.IUserLoginModel {

    @Override
    public void login(final String name, final String psw, final ICallBack<String> callBack) {
        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(name);
        bu2.setPassword(psw);
        //首先尝试用户名+密码登陆
        bu2.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    callBack.onSuccess("登录成功");
                }else{
                    //再次尝试以邮箱登陆
                    bu2.loginByAccount(name, psw, new LogInListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if(bmobUser!=null){
                                callBack.onSuccess("登录成功");
                            }else{
                                callBack.onFaild(BmobExceptionCode.match(e.getErrorCode()));
                            }
                        }
                    });
                }
            }
        });
    }
}
