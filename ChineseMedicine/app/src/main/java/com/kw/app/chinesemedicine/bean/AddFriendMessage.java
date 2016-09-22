package com.kw.app.chinesemedicine.bean;

import android.text.TextUtils;

import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.kw.app.chinesemedicine.messagecontent.CustomzeContactNotificationMessage;
import com.orhanobut.logger.Logger;
import org.json.JSONObject;

import io.rong.imlib.model.UserInfo;

/**
 * 添加好友请求
 */
public class AddFriendMessage{

    //好友请求：未读-未添加->接收到别人发给我的好友添加请求，初始状态
    public static final int STATUS_VERIFY_NONE=0;
    //好友请求：已读-未添加->点击查看了新朋友，则都变成已读状态
    public static final int STATUS_VERIFY_READED=2;
    //好友请求：已添加
    public static final int STATUS_VERIFIED=1;
    //好友请求：拒绝
    public static final int STATUS_VERIFY_REFUSE=3;
    //好友请求：我发出的好友请求-暂未存储到本地数据库中
    public static final int STATUS_VERIFY_ME_SEND=4;

    public AddFriendMessage(){}

    /**将ContactNotifiMessage转成NewFriend
     * @param msg 消息
     * @return
     */
    public static NewFriendDALEx convert(CustomzeContactNotificationMessage msg){
        NewFriendDALEx add =new NewFriendDALEx();
        String content = msg.getMessage();
        add.setMsg(content);
        add.setStatus(AddFriendMessage.STATUS_VERIFY_NONE);

        UserInfo user = msg.getUserInfo();
        add.setUid(user.getUserId());
        add.setName(user.getName());
        add.setAvatar(user.getPortraitUri().toString());

        try {
            String extra = msg.getExtra();
            if(!TextUtils.isEmpty(extra)){
                JSONObject json =new JSONObject(extra);
                add.setMsgid(json.getString("msgid"));
                add.setTime(json.getLong("time"));
            }else{
                Logger.i("AddFriendMessage的extra为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }
}
