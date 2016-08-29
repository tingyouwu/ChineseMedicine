package com.wty.app.library.utils;

import android.graphics.BitmapFactory;

/**
 * @Decription 图片处理工具类
 */
public class PhotoUtils {

    /**
     * @Description 获取图片的宽高比例
     **/
    public static float getImageWidthHeightSize(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return ((float) (options.outWidth))/(options.outHeight);
    }
}
