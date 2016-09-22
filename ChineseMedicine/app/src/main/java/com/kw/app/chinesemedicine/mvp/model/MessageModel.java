package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.NewFriendConversation;
import com.kw.app.chinesemedicine.bean.PrivateConversation;
import com.kw.app.chinesemedicine.bean.RongConversation;
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
                            RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, item.getTargetId(), 10, new RongIMClient.ResultCallback<List<Message>>() {
                                @Override
                                public void onSuccess(List<Message> messages) {
                                    int i = 0;
                                    RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, item.getTargetId(), TextMessage.obtain("测试一下"),
                                            " ", " ", new IRongCallback.ISendMessageCallback() {
                                                @Override
                                                public void onAttached(Message message) {

                                                }

                                                @Override
                                                public void onSuccess(Message message) {

                                                }

                                                @Override
                                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                                                }
                                            });
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                        }else if(item.getConversationType()== Conversation.ConversationType.SYSTEM){
                            //系统通知(好友申请之类的)
                            conversationList.add(new NewFriendConversation(item));
                        }
                    }
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
