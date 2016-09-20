package com.kw.app.chinesemedicine.fakeserver;

import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;

public class FakeServer {

    /**
     * 集成融云应用的唯一标识, 前端后台都需要用到.
     */
    public static final String APP_KEY = "e5t4ouvptkm2a";

    /**
     * 务必妥善保存, 不能放在应用端, 否则可以被反编译获得.
     */
    public static final String APP_SECRET = "vB4FakXm8f68";

    public static String getAppKey() {
        return APP_KEY;
    }

    /**
     * 获取融云Token, 通过调用融云ServerApi获得.
     *
     * @param user
     * @param callback
     */
    public static void getToken(UserBmob user, HttpUtil.OnResponse callback) {
        final String HTTP_GET_TOKEN = "https://api.cn.ronghub.com/user/getToken.json";
        HttpUtil.Header header = HttpUtil.getRcHeader(APP_KEY, APP_SECRET);
        String body = "userId=" + user.getObjectId() + "&name=" + user.getUsername() + "&portraitUri=" + user.getLogourl();
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.post(HTTP_GET_TOKEN, header, body, callback);
    }

    /**
     * 模拟用户登录, 返回的用户信息可以被扩展.
     *
     * @param id
     * @param password
     * @return 返回当前用户信息对象.
     */
    public static UserBmob getLoginUser(String id, String password) {
        return new UserBmob();
    }
}