package com.kw.app.chinesemedicine.bean;

import android.content.Context;
import android.content.Intent;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.ChatActivity;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.listener.QueryUserListener;

import cn.bmob.v3.exception.BmobException;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

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
            BmobUserModel.getInstance().queryUserInfo(cId, new QueryUserListener() {
                @Override
                public void done(UserBmob userBmob, BmobException e) {
                    if(e==null){
                        userBmob.save(userBmob);
                        user = UserDALEx.get().findById(cId);
                        if(user != null){
                            cName = user.getNickname();
                        }else{
                            cName = "陌生人";
                        }
                    }
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
            }else if(lastMsg instanceof VoiceMessage){
                return "[语音]";
            }else if(lastMsg instanceof LocationMessage){
                return"[位置]";
            }else if(lastMsg instanceof ContactNotificationMessage){
                return ((ContactNotificationMessage)lastMsg).getMessage();
            }else {
                return "[未知]";
            }

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
//        /**
//         * 获取某个会话内未读消息条数
//         * conversationType 会话类型
//         * targetId         会话目标ID
//         */
//        RongIMClient.getInstance().getUnreadCount(cType, cId,
//                new RongIMClient.ResultCallback<Integer>() {
//                    @Override
//                    public void onSuccess(Integer integer) {
//                        int unreadCount = integer;
//                        //开发者根据自己需求自行处理接下来的逻辑
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//
//                    }
//                });
    }

    @Override
    public void readAllMessages() {

    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChatActivity.class);
        intent.putExtra(ChatActivity.TARGET, user);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(Context context) {
    }
}
