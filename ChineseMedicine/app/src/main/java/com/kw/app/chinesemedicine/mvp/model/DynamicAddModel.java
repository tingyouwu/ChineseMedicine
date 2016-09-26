package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;
import android.text.TextUtils;

import com.kw.app.chinesemedicine.data.dalex.bmob.DynamicBmob;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicAddContract;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.utils.PhotoUtils;
import com.wty.app.library.utils.luban.Luban;
import com.wty.app.library.utils.luban.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * @author wty
 */
public class DynamicAddModel implements IDynamicAddContract.IDynamicAddModel {

    @Override
    public void submit(final Context context, final DynamicDALEx data, final ICallBack<String> callBack) {

        final String[] filesPath = data.getImages().split(",");

        final List<String> compresspaths = new ArrayList<String>();
        for(String path:filesPath){
            Luban.get()
                 .load(new File(path))
                 .putGear(Luban.THIRD_GEAR)
                 .setCompressListener(new OnCompressListener() {
                     @Override
                     public void onStart() {
                     }

                     @Override
                     public void onSuccess(File file) {
                         compresspaths.add(file.getAbsolutePath());
                         data.setSinglesize(PhotoUtils.getImageWidthHeightSize(file.getAbsolutePath()));
                         if (compresspaths.size() == filesPath.length) {
                             // 全部压缩完毕
                             uploadBatch(context,compresspaths,data,callBack);
                         }
                     }

                     @Override
                     public void onError(Throwable e) {
                         callBack.onFaild("压缩图片失败:" + e.getMessage());
                     }
                 }).launch();
        }
    }

    private void uploadBatch(final Context context, final List<String> compresspaths, final DynamicDALEx data, final ICallBack<String> callBack){

        BmobFile.uploadBatch(compresspaths.toArray(new String[compresspaths.size()]), new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {
                //有多少个文件上传，onSuccess方法就会执行多少次;
                //通过onSuccess回调方法中的files或urls集合的大小与上传的总文件个数比较，如果一样，则表示全部文件上传成功。
                if (urls.size() == compresspaths.size()) {//如果数量相等，则代表文件全部上传完成
                    //保存服务端文件地址
                    data.setImages(TextUtils.join(",",urls));
                    final DynamicBmob bmob = new DynamicBmob();
                    bmob.setAnnotationField(data);

                    bmob.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectid, BmobException e) {
                            if(e==null){
                                data.setDynamicid(objectid);
                                data.saveOrUpdate();
                                callBack.onSuccess(objectid);
                            }else{
                                callBack.onFaild(e.getMessage());
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
