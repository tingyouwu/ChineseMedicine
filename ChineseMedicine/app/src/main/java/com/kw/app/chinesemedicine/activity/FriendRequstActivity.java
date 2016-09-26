package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.kw.app.chinesemedicine.messagecontent.CustomzeContactNotificationMessage;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 好友申请
 * @author :wty
 */
public class FriendRequstActivity extends BaseActivity {

    public static String FRIENDID = "friendid";

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_content)
    TextView tv_content;

    String friendid;
    NewFriendDALEx newfriend;

    public static void startNewFriendActivity(Context context,String friendid) {
        Intent intent = new Intent(context, FriendRequstActivity.class);
        intent.putExtra(FRIENDID,friendid);
        context.startActivity(intent);
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("好友申请");
        friendid = getIntent().getStringExtra(FRIENDID);

        newfriend = NewFriendDALEx.get().findById(friendid);
        tvName.setText(newfriend.getName());
        tv_content.setText(newfriend.getMsg());
        ImageLoaderUtil.loadCircle(this,newfriend.getAvatar(),R.mipmap.img_contact_default,ivAvatar);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_friend_request;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @OnClick({R.id.ll_head, R.id.btn_refuse, R.id.btn_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head:
                break;
            case R.id.btn_refuse:
                break;
            case R.id.btn_agree:
                agreeAdd(newfriend, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //回到上一个页面
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showFailed(s);
                    }
                });
                break;
        }
    }

    /**
     * @Decription 同意添加到好友列表
     **/
    private void agreeAdd(final NewFriendDALEx add, final SaveListener listener){
        final UserBmob user = new UserBmob();
        user.setObjectId(add.getUid());
        BmobUserModel.getInstance().agreeAddFriend(user, new SaveListener() {
            @Override
            public void onSuccess() {
                sendAgreeAddFriendMessage(add,listener);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
            }
        });
    }

    /**
     * 发送同意添加好友的请求
     * 1.发一条系统消息
     * 2.发一条聊天消息
     */
    private void sendAgreeAddFriendMessage(final NewFriendDALEx user, final SaveListener listener){
        UserBmob currentUser = BmobUser.getCurrentUser(this, UserBmob.class);
        CustomzeContactNotificationMessage message = CustomzeContactNotificationMessage.obtain(CustomzeContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE,
                PreferenceUtil.getInstance().getLastAccount(),
                user.getUid(), "已同意您的好友请求");
        message.setUserInfo(new UserInfo(currentUser.getObjectId(), currentUser.getUsername(), Uri.parse(currentUser.getLogourl())));

        Map<String,Object> map =new HashMap<>();
        map.put("msgid", UUID.randomUUID().toString());//消息id
        map.put("time", System.currentTimeMillis());//当前时间
        map.put("name",PreferenceUtil.getInstance().getLastName());//用户名
        message.setExtra(new Gson().toJson(map));

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                user.getUid(),
                message,
                currentUser.getUsername()+"已同意您的好友请求",
                "", new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {
                        NewFriendDALEx.get().updateNewFriend(user, AddFriendMessage.STATUS_VERIFIED);
                        listener.onSuccess();
                        sendTxtMessage(user);
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        listener.onFailure(1,"同意失败");
                    }
                });


    }

    /**
     * 发送一个文本消息
     **/
    private void sendTxtMessage(NewFriendDALEx user){
        TextMessage textMessage = TextMessage.obtain("我通过了你的好友验证请求，我们可以开始聊天了!");
        Map<String,Object> map =new HashMap<>();
        map.put("msgid", UUID.randomUUID().toString());//消息id
        map.put("time", System.currentTimeMillis());//当前时间
        textMessage.setExtra(new Gson().toJson(map));
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                user.getUid(),
                textMessage,
                "我通过了你的好友验证请求，我们可以开始聊天了!",
                "", new IRongCallback.ISendMessageCallback() {
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

}
