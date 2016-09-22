package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.NewFriendConversation;
import com.kw.app.chinesemedicine.bean.PrivateConversation;
import com.kw.app.chinesemedicine.bean.RongConversation;
import com.kw.app.chinesemedicine.bean.SystemMessageConversation;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.SystemMessageDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IMessageContract;
import com.wty.app.library.callback.ICallBack;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * @author wty
 */
public class MessageModel implements IMessageContract.IMessageModel {

    @Override
    public void refreshMessage(Context context, final ICallBack<List<RongConversation>> callBack) {
        /**
         * 获取会话列表。
         * 此方法会从本地数据库中，读取会话列表。
         * 返回的会话列表按照时间从前往后排列，如果有置顶的会话，则置顶的会话会排列在前面。
         * @param callback 获取会话列表的回调。
         */
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<RongConversation> conversationList = new ArrayList<RongConversation>();
                if (conversations != null && conversations.size()>0){
                    for(final Conversation item:conversations){
                        if(item.getConversationType() == Conversation.ConversationType.PRIVATE){
                            //单聊
                            conversationList.add(new PrivateConversation(item));
                        }
                    }
                }

                //添加系统消息
                List<SystemMessageDALEx> systems = SystemMessageDALEx.get().getAllMessage();
                if(systems != null && systems.size()>0){
                    conversationList.add(new SystemMessageConversation(systems.get(0)));
                }

                callBack.onSuccess(conversationList);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBack.onFaild("获取会话失败");
            }
        });
    }
}
