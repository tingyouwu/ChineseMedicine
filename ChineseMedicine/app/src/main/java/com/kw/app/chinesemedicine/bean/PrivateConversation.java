package com.kw.app.chinesemedicine.bean;

import android.content.Context;
import android.text.TextUtils;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.ChatActivity;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.FileMessageDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.library.callback.ICallBack;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;

/**
 * 私聊会话
 * Created by Administrator on 2016/5/25.
 */
public class PrivateConversation extends RongConversation{

    private Conversation conversation;
    private MessageContent lastMsg;
    private UserDALEx user;

    public PrivateConversation(Conversation conversation){
        this.conversation = conversation;
        cType = conversation.getConversationType();
        cId = conversation.getTargetId();

        //去用户表中去拿此id的信息
        if(!UserDALEx.get().isExist(cId)){

            BmobUserModel.getInstance().queryUserInfo(cId, new ICallBack<UserBmob>() {
                @Override
                public void onSuccess(UserBmob userBmob) {
                    userBmob.save(userBmob);
                    user = UserDALEx.get().findById(cId);
                    if(user != null){
                        cName = user.getNickname();
                    }else{
                        cName = "陌生人";
                    }
                }

                @Override
                public void onFaild(String msg) {

                }
            });

        }else{
            user = UserDALEx.get().findById(cId);
            if(user != null){
                cName = user.getNickname();
            }else{
                cName = "陌生人";
            }
        }

        //获取最后一条消息
        lastMsg =conversation.getLatestMessage();
    }

    @Override
    public Object getAvatar() {
        if(user != null && user.getLogourl()!=null){
            return user.getLogourl();
        }else{
            return R.mipmap.img_contact_default;
        }
    }

    @Override
    public String getLastMessageContent() {
        if(lastMsg!=null){
            if(lastMsg instanceof TextMessage){
                return ((TextMessage)lastMsg).getContent();
            }else if(lastMsg instanceof ImageMessage){
                return "[图片]";
            }else if(lastMsg instanceof LocationMessage){
                return "[位置]";
            }else if(lastMsg instanceof ContactNotificationMessage){
                return ((ContactNotificationMessage)lastMsg).getMessage();
            }else if(lastMsg instanceof FileMessage){
                String extra = ((FileMessage)lastMsg).getExtra();
                if(!TextUtils.isEmpty(extra)){
                    JSONObject json = null;
                    try {
                        json = new JSONObject(extra);
                        if(json.has(FileMessageDALEx.FILETYPE)) {
                            if(json.getInt(FileMessageDALEx.FILETYPE)== FileMessageDALEx.FileMessageType.Voice.code){
                                return "[语音]";
                            }else if(json.getInt(FileMessageDALEx.FILETYPE)== FileMessageDALEx.FileMessageType.Video.code) {
                                return "[小视频]";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return "[未知]";

        }else{//防止消息错乱
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        return conversation.getSentTime();
    }

    @Override
    public int getUnReadCount() {
        return 0;
    }

    @Override
    public void readAllMessages() {

    }

    @Override
    public void onClick(Context context) {
        ChatActivity.startChatActivity(context,user);
    }

    @Override
    public void onLongClick(Context context) {
    }
}
