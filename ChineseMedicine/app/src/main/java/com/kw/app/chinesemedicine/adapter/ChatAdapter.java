package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.bmobim.R;
import com.wty.app.library.adapter.BaseRecyclerViewMultiItemAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.PreferenceUtil;
import com.wty.app.library.utils.TimeUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author :wty
 */
public class ChatAdapter extends BaseRecyclerViewMultiItemAdapter<Message> {
    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE =6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //视频
    private final int TYPE_SEND_VIDEO =8;
    private final int TYPE_RECEIVER_VIDEO = 9;

    private final long TIME_INTERVAL = 10 * 60 * 1000;//显示时间间隔:10分钟

    private String currentUid="";
    private UserDALEx target;

    public ChatAdapter(Context context, List<Message> data,UserDALEx target) {
        super(context, data);
        this.target = target;

        currentUid = BmobUser.getCurrentUser(context).getObjectId();
        addItemType(TYPE_RECEIVER_TXT, R.layout.item_chat_received_message);
        addItemType(TYPE_SEND_TXT, R.layout.item_chat_sent_message);
        addItemType(TYPE_SEND_IMAGE,R.layout.item_chat_sent_image);
        addItemType(TYPE_RECEIVER_IMAGE, R.layout.item_chat_received_image);
        addItemType(TYPE_SEND_LOCATION, R.layout.item_chat_sent_location);
        addItemType(TYPE_RECEIVER_LOCATION,R.layout.item_chat_received_location);
        addItemType(TYPE_SEND_VOICE, R.layout.item_chat_sent_voice);
        addItemType(TYPE_RECEIVER_VOICE,R.layout.item_chat_received_voice);
        addItemType(TYPE_SEND_VIDEO, R.layout.item_chat_sent_message);
        addItemType(TYPE_RECEIVER_VIDEO, R.layout.item_chat_received_message);
    }

    public int findPosition(BmobIMMessage message) {
        int index = this.getItemCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int findPosition(long id) {
        int index = this.getItemCount();
        int position = -1;
        while(index-- > 0) {
            if(this.getItemId(index) == id) {
                position = index;
                break;
            }
        }
        return position;
    }

    public Message getFirstMessage() {
        if (null != mData && mData.size() > 0) {
            return mData.get(0);
        } else {
            return null;
        }
    }

    @Override
    protected void bindView(BaseRecyclerViewHolder helper, Message item, int position) {
        //首先处理一下头像
        ImageView icon = helper.getView(R.id.iv_avatar);
        if(item.getSenderUserId().equals(currentUid)){
            //显示我自己的头像
            ImageLoaderUtil.loadCircle(mContext, PreferenceUtil.getInstance().getLogoUrl(),R.mipmap.img_contact_default,icon);
        }else{
            ImageLoaderUtil.loadCircle(mContext,target.getLogourl(),R.mipmap.img_contact_default,icon);
        }

        TextView tv_time = helper.getView(R.id.tv_time);
        tv_time.setText(TimeUtil.getChatTime(false, item.getSentTime()));
        tv_time.setVisibility(shouldShowTime(position)? View.VISIBLE:View.GONE);

        switch (helper.getItemViewType()){
            case TYPE_RECEIVER_TXT:
                handleReceiveTextMessage(helper,item);
                break;
            case TYPE_RECEIVER_IMAGE:
                break;
            case TYPE_RECEIVER_LOCATION:
                break;
            case TYPE_RECEIVER_VIDEO:
                break;
            case TYPE_RECEIVER_VOICE:
                break;
            case TYPE_SEND_IMAGE:
                break;
            case TYPE_SEND_LOCATION:
                break;
            case TYPE_SEND_TXT:
                handleSendTextMessage(helper,item);
                break;
            case TYPE_SEND_VIDEO:
                break;
            case TYPE_SEND_VOICE:
                break;
            default:
                break;
        }
    }

    @Override
    protected int getItemMultiViewType(int position) {
        Message message = mData.get(position);
        MessageContent content = message.getContent();

        if(content instanceof TextMessage){
            //文本消息
            return message.getSenderUserId().equals(currentUid) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
        }else if(content instanceof ImageMessage){
            //图片消息
            return message.getSenderUserId().equals(currentUid) ? TYPE_SEND_IMAGE: TYPE_RECEIVER_IMAGE;
        }else if(content instanceof LocationMessage){
            //定位信息
            return message.getSenderUserId().equals(currentUid) ? TYPE_SEND_LOCATION: TYPE_RECEIVER_LOCATION;
        }else if(content instanceof VoiceMessage){
            //语音信息
            return message.getSenderUserId().equals(currentUid) ? TYPE_SEND_VOICE: TYPE_RECEIVER_VOICE;
        }else {
            return -1;
        }

    }

    /**
     * @Decription 处理我发出去的文本信息
     **/
    private void handleSendTextMessage(BaseRecyclerViewHolder helper,Message msg){
        TextMessage textMessage = (TextMessage)(msg.getContent());
        TextView content = helper.getView(R.id.tv_message);
        content.setText(textMessage.getContent());
    }

    /**
     * @Decription 处理别人发给我的文本信息
     **/
    private void handleReceiveTextMessage(BaseRecyclerViewHolder helper,Message msg){
        TextMessage textMessage = (TextMessage)(msg.getContent());
        TextView content = helper.getView(R.id.tv_message);
        content.setText(textMessage.getContent());
    }

    /**
     * @Decription 判断当前位置是否需要显示时间（条件：前后创建时间间隔大于10分钟）
     **/
    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = mData.get(position - 1).getSentTime();
        long curTime = mData.get(position).getSentTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
