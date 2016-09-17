package com.wty.app.bmobim.bean;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wty.app.bmobim.db.NewFriend;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**添加好友请求
 * @author :smile
 * @project:AddFriendMessage
 * @date :2016-01-30-17:28
 */
public class AddFriendMessage extends BmobIMExtraMessage {

    public static final String MSGTYPE = "add";
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

    /**将BmobIMMessage转成NewFriend
     * @param msg 消息
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg){
        NewFriend add =new NewFriend();
        String content = msg.getContent();
        add.setMsg(content);
        add.setTime(msg.getCreateTime());
        add.setStatus(AddFriendMessage.STATUS_VERIFY_NONE);
        try {
            String extra = msg.getExtra();
            if(!TextUtils.isEmpty(extra)){
                JSONObject json =new JSONObject(extra);
                String name = json.getString("name");
                add.setName(name);
                String avatar = json.getString("avatar");
                add.setAvatar(avatar);
                add.setUid(json.getString("uid"));
            }else{
                Logger.i("AddFriendMessage的extra为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }


    @Override
    public String getMsgType() {
        return MSGTYPE;
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }

}
