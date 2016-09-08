package com.kw.app.chinesemedicine.mvp.model;

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
    public void register(final UserDALEx user, final ICallBack<String> callBack) {
        if(!TextUtils.isEmpty(user.getLogourl())){
            Luban.get()
                    .load(new File(user.getLogourl()))
                    .putGear(Luban.THIRD_GEAR)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            // 压缩完毕
                            signUp(file.getAbsolutePath(), user, callBack);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callBack.onFaild("压缩图片失败:" + e.getMessage());
                        }
                    }).launch();
        }else{
            UserBmob bmob = new UserBmob();
            bmob.setAnnotationField(user);

            bmob.signUp(new SaveListener<UserBmob>() {
                @Override
                public void done(UserBmob bmob, BmobException e) {
                    if (e != null) {
                        callBack.onFaild(BmobExceptionCode.match(e.getErrorCode()));
                    } else {
                        user.setUserid(bmob.getObjectId());
                        user.saveOrUpdate();
                        callBack.onSuccess(bmob.getObjectId());
                    }
                }
            });
        }
    }

    /**
     * @Decription 注册
     **/
    private void signUp(final String compresspath, final UserDALEx data,final ICallBack<String> callBack){
        BmobFile.uploadBatch(new String[]{compresspath}, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {
                //有多少个文件上传，onSuccess方法就会执行多少次;
                //通过onSuccess回调方法中的files或urls集合的大小与上传的总文件个数比较，如果一样，则表示全部文件上传成功。
                if (urls.size() == 1) {//如果数量相等，则代表文件上传完成
                    //保存服务端文件地址
                    data.setLogourl(urls.get(0));
                    UserBmob bmob = new UserBmob();
                    bmob.setAnnotationField(data);

                    bmob.signUp(new SaveListener<UserBmob>() {
                        @Override
                        public void done(UserBmob bmob, BmobException e) {
                            if (e != null) {
                                callBack.onFaild(BmobExceptionCode.match(e.getErrorCode()));
                            } else {
                                data.setUserid(bmob.getObjectId());
                                data.saveOrUpdate();
                                callBack.onSuccess(bmob.getObjectId());
                            }
                        }
                    });
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }

            @Override
            public void onError(int i, String msg) {
                callBack.onFaild(i + ":" + msg);
            }
        });
    }


}
