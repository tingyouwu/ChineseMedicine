package com.wty.app.library.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wty.app.library.R;

import java.io.File;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * @Decription 加载图片工具类
 * @author wty
 *
 * Glide 工具类
 *     Google推荐的图片加载库，专注于流畅的滚动
 *
 * Glide 比Picasso  加载快 但需要更大的内存来缓存
 *     Glide 不光接受Context，还接受Activity 和 Fragment ,图片加载会和Activity/Fragment的生命周期保持一致 在onPause（）暂停加载，
 *     onResume（）恢复加载
 *
 * 支持GIF格式图片加载
 */
public class ImageLoaderUtil {

    /**
     * 加载项目内的图片
     * @param activity
     * @param resId  资源id(R.drawable.xxx)
     * @param imageView
     */
    public static void load(Activity activity,int resId, ImageView imageView) {
        Glide.with(activity).load(resId).crossFade().into(imageView);
    }

    /**
     * 加载项目内的图片
     * @param fragment
     * @param resId  资源id(R.drawable.xxx)
     * @param imageView
     */
    public static void load(Fragment fragment,int resId, ImageView imageView) {
        Glide.with(fragment).load(resId).crossFade().into(imageView);
    }

    /**
     * 加载SD卡图片文件
     * @param context
     * @param file
     * @param imageView
     */
    public static void load(Context context,File file, ImageView imageView) {
        if(null!=file && file.isFile() && file.exists()){
            Glide.with(context).load(file).crossFade().centerCrop().into(imageView);
        }
    }

    /**
     * 标准图片
     * @param context context.
     * @param url
     * @param view the imageView.
     */
    public static void load(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.img_default_loading)
                .error(R.drawable.img_error_fail)
                .into(view);
    }

    /**
     * 加载圆形图片
     * @param context context.
     * @param url
     * @param view the imageView.
     */
    public static void loadCircle(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.img_default_loading)
                .error(R.drawable.img_error_fail)
                .into(view);
    }
}
