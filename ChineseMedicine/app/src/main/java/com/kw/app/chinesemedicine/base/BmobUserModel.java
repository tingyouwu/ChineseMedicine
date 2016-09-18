package com.kw.app.chinesemedicine.base;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.Friend;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.listener.QueryUserListener;
import com.kw.app.chinesemedicine.listener.UpdateCacheListener;
import com.orhanobut.logger.Logger;
import com.wty.app.library.base.MainApplication;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author :smile
 * @project:UserModel
 * @date :2016-01-22-18:09
 */
public class BmobUserModel{

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;
    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return MainApplication.getInstance();
    }

    private static BmobUserModel ourInstance = new BmobUserModel();

    public static BmobUserModel getInstance() {
        return ourInstance;
    }

    private BmobUserModel() {}


    /**查询用户
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username,int limit,final FindListener<UserBmob> listener){
        BmobQuery<UserBmob> query = new BmobQuery<>();
        //去掉当前用户
        BmobUser user = BmobUser.getCurrentUser(getContext());
        query.addWhereNotEqualTo("username",user.getUsername());
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<UserBmob>() {
            @Override
            public void onSuccess(List<UserBmob> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(CODE_NULL, "查无此人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    /**查询用户信息
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener){
        BmobQuery<UserBmob> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(getContext(), new FindListener<UserBmob>() {
            @Override
            public void onSuccess(List<UserBmob> list) {
                if(list!=null && list.size()>0){
                    listener.internalDone(list.get(0), null);
                }else{
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }

    /**更新用户资料和会话资料
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event,final UpdateCacheListener listener){
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info =event.getFromUserInfo();
        final BmobIMMessage msg =event.getMessage();
        String username =info.getName();
        String title =conversation.getConversationTitle();
        Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if(!username.equals(title)) {
            BmobUserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(UserBmob s, BmobException e) {
                    if(e==null){
                        String name =s.getUsername();
                        String avatar = s.getLogourl();
                        Logger.i("query success：" + name + "," + avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if(!msg.isTransient()){
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    }else{
                        Logger.e(e.getMessage());
                    }
                    listener.done(null);
                }
            });
        }else{
            listener.internalDone(null);
        }
    }

    /**
     * 同意添加好友：1、发送同意添加的请求，2、添加对方到自己的好友列表中
     */
    public void agreeAddFriend(UserBmob friend,SaveListener listener){
        Friend f = new Friend();
        UserBmob user = BmobUser.getCurrentUser(getContext(), UserBmob.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(getContext(),listener);
    }

    /**
     * 查询好友
     * @param listener
     */
    public void queryFriends(final FindListener<Friend> listener){
        BmobQuery<Friend> query = new BmobQuery<>();
        UserBmob user = BmobUser.getCurrentUser(getContext(), UserBmob.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(getContext(), new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(0, "暂无联系人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    /**
     * 删除好友
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f,DeleteListener listener){
        Friend friend =new Friend();
        friend.delete(getContext(),f.getObjectId(),listener);
    }
}
