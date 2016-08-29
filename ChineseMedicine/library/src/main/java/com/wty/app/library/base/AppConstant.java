package com.wty.app.library.base;

/**
 * @author wty
 * 功能描述：本应用对应的一些常量
 **/
public class AppConstant {

    public static final String Bmob_ApplicationId = "38f52b3f41666fb3eb216c0b343ba22b";
    public static final String SD_PATH = "com.wty.app";
    public static final String PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/"+SD_PATH;
    public static final String SAVEIMAGEPATH = PATH + "/SaveImages";
    public static final String IMAGEPATH = PATH + "/Images";
    public static final String VOICEPATH = PATH  + "/voice";
    public static final String DOCPATH = PATH  + "/file";
    public static final String CAMERA_PATH = PATH + "/CameraImage/";
    public static final String CROP_PATH = PATH + "/CropImage/";
    public static final String CACHE_PATH = PATH + "/CacheFile/";

    public static class ActivityResult{
        public final static int Request_Camera = 100;
        public final static int Request_Image = 101;
        public final static int Request_Preview = 102;
    }

}
