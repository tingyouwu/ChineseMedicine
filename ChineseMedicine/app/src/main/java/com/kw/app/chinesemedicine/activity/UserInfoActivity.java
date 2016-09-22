package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.messagecontent.CustomzeContactNotificationMessage;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

/**
 * 用户资料
 */
public class UserInfoActivity extends BaseActivity {

    public static String TAG = "user";

    @Bind(R.id.iv_avator)
    ImageView iv_avator;
    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.btn_add_friend)
    Button btn_add_friend;
    @Bind(R.id.btn_chat)
    Button btn_chat;

    UserBmob user;
    BmobIMUserInfo info;

    public static void startUserInfoActivity(Context context,UserBmob user) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(TAG, user);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_add_friend)
    public void onAddClick(View view){
        sendAddFriendMessage();
    }

    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage(){
        /**
         * 发送添加好友消息
         * @param conversationType      会话类型
         * @param targetId              会话ID
         * @param content               消息的内容，一般是MessageContent的子类对象
         * @param pushContent           接收方离线时需要显示的push消息内容
         * @param pushData              接收方离线时需要在push消息中携带的非显示内容
         * @param SendMessageCallback   发送消息的回调
         * @param ResultCallback        消息存库的回调，可用于获取消息实体
         */
        UserBmob currentUser = BmobUser.getCurrentUser(this, UserBmob.class);
        CustomzeContactNotificationMessage message = CustomzeContactNotificationMessage.obtain(CustomzeContactNotificationMessage.CONTACT_OPERATION_REQUEST,
                PreferenceUtil.getInstance().getLastAccount(),
                user.getObjectId(), "很高兴认识你，可以加好友吗?");
        message.setUserInfo(new UserInfo(currentUser.getObjectId(), currentUser.getUsername(), Uri.parse(currentUser.getLogourl())));

        Map<String,Object> map =new HashMap<>();
        map.put("msgid", UUID.randomUUID().toString());//消息id
        map.put("time",System.currentTimeMillis());//当前时间
        message.setExtra(new Gson().toJson(map));

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                user.getObjectId(),
                message,
                "很高兴认识你，可以加个好友吗?",
                "",new RongIMClient.SendMessageCallback() {

                    @Override
                    public void onSuccess(Integer integer) {
                        showAppToast("好友请求发送成功，等待验证");
                    }

                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        showAppToast("发送失败:" + errorCode);
                    }
                }, new RongIMClient.ResultCallback<Message>() {
                    @Override
                    public void onSuccess(Message message) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

    }

    @OnClick(R.id.btn_chat)
    public void onChatClick(View view){
//        //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
//        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("c", c);
//        startActivity(ChatActivity.class, bundle, false);
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        user = (UserBmob)getIntent().getSerializableExtra(TAG);
        getDefaultNavigation().setTitle("个人资料");

        if(user.getObjectId().equals(getCurrentUid())){
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
        }else{
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
        }

        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getLogourl());
        ImageLoaderUtil.loadCircle(this, user.getLogourl(), iv_avator);
        tv_name.setText(user.getUsername());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_user_info;
    }

    private String getCurrentUid(){
        return BmobUser.getCurrentUser(this,UserBmob.class).getObjectId();
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
}
