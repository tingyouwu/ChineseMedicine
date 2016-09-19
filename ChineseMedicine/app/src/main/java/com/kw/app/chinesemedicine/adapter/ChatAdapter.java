package com.kw.app.chinesemedicine.adapter;

import android.content.Context;

import com.wty.app.bmobim.R;
import com.wty.app.library.adapter.BaseRecyclerViewMultiItemAdapter;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 * @author :wty
 */
public class ChatAdapter extends BaseRecyclerViewMultiItemAdapter<BmobIMMessage> {
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
    //同意添加好友成功后的样式
    private final int TYPE_AGREE = 10;

    private final long TIME_INTERVAL = 10 * 60 * 1000;//显示时间间隔:10分钟

    private String currentUid="";
    BmobIMConversation c;

    public ChatAdapter(Context context, List<BmobIMMessage> data, BmobIMConversation c) {
        super(context, data);

        try {
            currentUid = BmobUser.getCurrentUser(context).getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.c =c;

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
        addItemType(TYPE_AGREE,R.layout.item_chat_agree);
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

    public BmobIMMessage getFirstMessage() {
        if (null != mData && mData.size() > 0) {
            return mData.get(0);
        } else {
            return null;
        }
    }

    @Override
    protected void bindView(BaseRecyclerViewHolder helper, BmobIMMessage item, int position) {
        switch (helper.getItemViewType()){
            case TYPE_AGREE:
                break;
            case TYPE_RECEIVER_IMAGE:
                break;
            case TYPE_RECEIVER_LOCATION:
                break;
            case TYPE_RECEIVER_TXT:
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
        BmobIMMessage message = mData.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_IMAGE: TYPE_RECEIVER_IMAGE;
        }else if(message.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_LOCATION: TYPE_RECEIVER_LOCATION;
        }else if(message.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VOICE: TYPE_RECEIVER_VOICE;
        }else if(message.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
        }else if(message.getMsgType().equals(BmobIMMessageType.VIDEO.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VIDEO: TYPE_RECEIVER_VIDEO;
        }else if(message.getMsgType().equals("agree")) {//显示欢迎
            return TYPE_AGREE;
        }else{
            return -1;
        }
    }

    /**
     * @Decription 判断当前位置是否需要显示时间（条件：前后创建时间间隔大于10分钟）
     **/
    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = mData.get(position - 1).getCreateTime();
        long curTime = mData.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
