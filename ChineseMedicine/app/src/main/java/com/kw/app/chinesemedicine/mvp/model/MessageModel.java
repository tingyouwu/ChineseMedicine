package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.Conversation;
import com.kw.app.chinesemedicine.bean.NewFriendConversation;
import com.kw.app.chinesemedicine.bean.PrivateConversation;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IMessageContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

/**
 * @author wty
 */
public class MessageModel implements IMessageContract.IMessageModel {

    @Override
    public List<Conversation> refreshMessage(Context context) {
        //添加会话
        List<Conversation> conversationList = new ArrayList<Conversation>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case PrivateConversation.PRIVATE://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriendDALEx> friends = NewFriendDALEx.get().getAllNewFriend();
        if(friends!=null && friends.size()>0){
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }
}
