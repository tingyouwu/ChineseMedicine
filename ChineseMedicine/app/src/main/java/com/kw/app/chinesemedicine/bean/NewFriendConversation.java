package com.kw.app.chinesemedicine.bean;

import android.content.Context;
import android.text.TextUtils;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.NewFriendActivity;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;


/**
 * 新朋友会话
 * Created by Administrator on 2016/5/25.
 */
public class NewFriendConversation extends Conversation{

    NewFriendDALEx lastFriend;

    public NewFriendConversation(NewFriendDALEx friend){
        this.lastFriend=friend;
        this.cName="新朋友";
    }

    @Override
    public String getLastMessageContent() {
        if(lastFriend!=null){
            Long status =lastFriend.getStatus();
            String name = lastFriend.getName();
            if(TextUtils.isEmpty(name)){
                name = lastFriend.getUid();
            }
            //目前的好友请求都是别人发给我的
            if(status==null || status== AddFriendMessage.STATUS_VERIFY_NONE||status ==AddFriendMessage.STATUS_VERIFY_READED){
                return name+"请求添加好友";
            }else{
                return "我已添加"+name;
            }
        }else{
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if(lastFriend!=null){
            return lastFriend.getTime();
        }else{
            return 0;
        }
    }

    @Override
    public Object getAvatar() {
        return R.mipmap.new_friends_icon;
    }

    @Override
    public int getUnReadCount() {
        return NewFriendDALEx.get().getNewInvitationCount();
    }

    @Override
    public void readAllMessages() {
        //批量更新未读未认证的消息为已读状态
        NewFriendDALEx.get().updateBatchStatus();
    }

    @Override
    public void onClick(Context context) {
        NewFriendActivity.startNewFriendActivity(context);
    }

    @Override
    public void onLongClick(Context context) {
//        NewFriendManager.getInstance(context).deleteNewFriend(lastFriend);
    }
}
