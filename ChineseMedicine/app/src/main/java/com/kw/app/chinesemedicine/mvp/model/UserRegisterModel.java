package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;
import android.text.TextUtils;

import com.kw.app.chinesemedicine.data.annotation.bmob.BmobExceptionCode;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IUserRegisterContract;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.utils.luban.Luban;
import com.wty.app.library.utils.luban.OnCompressListener;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * @author wty
 */
public class UserRegisterModel implements IUserRegisterContract.IUserRegisterModel {

    @Override
    public void register(final Context context, final UserDALEx user, final ICallBack<String> callBack) {
        if(!TextUtils.isEmpty(user.getLogourl())){
            Luban.get().load(new File(user.getLogourl()))
                       .putGear(Luban.THIRD_GEAR)
                       .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                // 压缩完毕
                                signUp(context,file.getAbsolutePath(), user, callBack);
                            }

                            @Override
                            public void onError(Throwable e) {
                                callBack.onFaild("压缩图片失败:" + e.getMessage());
                            }
                        }).launch();
        }else{
            signUpToBmob(context,user,callBack);
        }
    }

    /**
     * @Decription 注册
     **/
    private void signUp(final Context context, final String compresspath, final UserDALEx data, final ICallBack<String> callBack){
        BmobFile.uploadBatch(new String[]{compresspath}, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {
                if (urls.size() == 1) {//如果数量相等，则代表文件上传完成
                    data.setLogourl(urls.get(0));
                    signUpToBmob(context,data,callBack);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
            }

            @Override
            public void onError(int code, String msg) {
                callBack.onFaild(BmobExceptionCode.match(code));
            }
        });
    }

    private void signUpToBmob(Context context, UserDALEx data,final ICallBack<String> callBack){
        final UserBmob bmob = new UserBmob();
        bmob.setAnnotationField(data);
        bmob.signUp(new SaveListener<UserBmob>() {
            @Override
            public void done(UserBmob userBmob, BmobException e) {
                if(e==null){
                    userBmob.save(userBmob);
                    callBack.onSuccess(userBmob.getObjectId());
                }else{
                    callBack.onFaild(e.getMessage());
                }
            }
        });
    }

}
