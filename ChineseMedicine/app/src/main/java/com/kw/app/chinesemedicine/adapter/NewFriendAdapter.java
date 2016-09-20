package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.kw.app.chinesemedicine.bean.AgreeAddFriendMessage;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
                            ((BaseActivity)mContext).showAppToast("添加好友失败:"+s);
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
        UserBmob user = new UserBmob();
        user.setObjectId(add.getUid());
        BmobUserModel.getInstance().agreeAddFriend(user, new SaveListener() {
            @Override
            public void onSuccess() {
                sendAgreeAddFriendMessage(add, listener);
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
    private void sendAgreeAddFriendMessage(final NewFriendDALEx add,final SaveListener listener){
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg =new AgreeAddFriendMessage();
        UserBmob currentUser = BmobUser.getCurrentUser(mContext, UserBmob.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String,Object> map =new HashMap<>();
        map.put("msg",currentUser.getUsername()+"同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid",add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        map.put("msgid", UUID.randomUUID().toString());//消息id
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e){
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendDALEx.get().updateNewFriend(add,AddFriendMessage.STATUS_VERIFIED);
                    listener.onSuccess();
                } else {//发送失败
                    listener.onFailure(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }
}
