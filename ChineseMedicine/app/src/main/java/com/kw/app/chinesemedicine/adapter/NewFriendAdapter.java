package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

/**
 * @Decription 搜索好友 适配器
 */
public class NewFriendAdapter extends BaseRecyclerViewAdapter<NewFriendDALEx> {
    public NewFriendAdapter(Context context, List data) {
        super(context, R.layout.item_new_friend, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, final NewFriendDALEx item, int position) {
        ImageView icon = helper.getView(R.id.iv_recent_avatar);
        TextView name = helper.getView(R.id.tv_recent_name);
        TextView msg = helper.getView(R.id.tv_recent_msg);
        final Button agree = helper.getView(R.id.btn_aggree);

        name.setText(item.getName());
        msg.setText(item.getMsg());
        ImageLoaderUtil.loadCircle(mContext,item.getAvatar(),R.mipmap.img_contact_default,icon);

        long status = item.getStatus();
        if(status == AddFriendMessage.STATUS_VERIFY_NONE || status == AddFriendMessage.STATUS_VERIFY_READED){
            //未添加 /已读未添加
            agree.setText("同意");
            agree.setEnabled(true);
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeAdd(item, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            agree.setText("已同意");
                            agree.setEnabled(false);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            agree.setEnabled(false);
                            ((BaseActivity) mContext).showAppToast("添加好友失败:" + s);
                        }
                    });
                }
            });
        }else if(status == AddFriendMessage.STATUS_VERIFY_REFUSE){
            agree.setText("已拒绝");
            agree.setEnabled(false);
        }else{
            agree.setText("已同意");
            agree.setEnabled(false);
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
     */
    private void sendAgreeAddFriendMessage(final NewFriendDALEx user, final SaveListener listener){
        UserBmob currentUser = BmobUser.getCurrentUser(mContext, UserBmob.class);
        ContactNotificationMessage message = ContactNotificationMessage.obtain(ContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE,
                PreferenceUtil.getInstance().getLastAccount(),
                user.getUid(), "我通过了你的好友验证请求，我们可以开始聊天了!");
        message.setUserInfo(new UserInfo(currentUser.getObjectId(), currentUser.getUsername(), Uri.parse(currentUser.getLogourl())));

        Map<String,Object> map =new HashMap<>();
        map.put("msgid", UUID.randomUUID().toString());//消息id
        map.put("time", System.currentTimeMillis());//当前时间
        message.setExtra(new Gson().toJson(map));

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                user.getUid(),
                message,
                "我通过了你的好友验证请求，我们可以开始聊天了!",
                "", new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onSuccess(Integer integer) {
                        //修改本地的好友请求记录
                        NewFriendDALEx.get().updateNewFriend(user, AddFriendMessage.STATUS_VERIFIED);
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        listener.onFailure(integer,"同意失败");
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
}
