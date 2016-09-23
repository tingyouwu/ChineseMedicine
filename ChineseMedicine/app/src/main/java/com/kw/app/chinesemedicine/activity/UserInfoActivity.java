package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kw.app.chinesemedicine.R;
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
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

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
        CustomzeContactNotificationMessage message = CustomzeContactNotificationMessage.obtain(CustomzeContactNotificationMessage.CONTACT_OPERATION_REQUEST,
                PreferenceUtil.getInstance().getLastAccount(),
                user.getObjectId(), "很高兴认识你，可以加好友吗?");

        Map<String,Object> map =new HashMap<>();
        map.put("msgid", UUID.randomUUID().toString());//消息id
        map.put("time",System.currentTimeMillis());//当前时间
        message.setExtra(new Gson().toJson(map));

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                user.getObjectId(),
                message,
                "很高兴认识你，可以加个好友吗?",
                "", new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {
                        showAppToast("好友请求发送成功，等待验证");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        showAppToast("发送失败:" + errorCode);
                    }
                });
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
        }else{
            btn_add_friend.setVisibility(View.VISIBLE);
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
