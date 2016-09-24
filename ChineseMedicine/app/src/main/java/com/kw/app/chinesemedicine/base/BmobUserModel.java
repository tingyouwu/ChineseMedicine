package com.kw.app.chinesemedicine.base;

import android.content.Context;
import android.text.TextUtils;

import com.kw.app.chinesemedicine.bean.Friend;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.listener.QueryUserListener;
import com.wty.app.library.base.MainApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author :wty
 * 管理用户信息  好友信息
 */
public class BmobUserModel{

    public int CODE_NULL=1000;
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
                if (list != null && list.size() > 0) {
                    listener.internalDone(list.get(0), null);
                } else {
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }

    /**
     * @更新用户信息
     **/
    public void updateUserInfo(String userid){
        BmobUserModel.getInstance().queryUserInfo(userid, new QueryUserListener() {
            @Override
            public void done(UserBmob user, BmobException e) {
                if(e==null){
                    user.save(user);
                }
            }
        });
    }

    /**
     * 同意添加好友：添加对方到自己的好友列表中
     */
    public void agreeAddFriend(UserBmob friend,SaveListener listener){
        Friend f = new Friend();
        UserBmob user = BmobUser.getCurrentUser(getContext(), UserBmob.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.setStatus(1);//1表示我添加为好友  0表示我删除这条好友关系
        f.save(getContext(),listener);
    }

    /**
     * 查询好友
     * @param listener
     */
    public void queryFriends(String updatetime,final FindListener<Friend> listener){
        BmobQuery<Friend> query = new BmobQuery<>();
        UserBmob user = BmobUser.getCurrentUser(getContext(), UserBmob.class);
        query.addWhereEqualTo("user", user);

        if(!TextUtils.isEmpty(updatetime)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try {
                date = sdf.parse(updatetime);
                query.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        query.include("friendUser");
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
