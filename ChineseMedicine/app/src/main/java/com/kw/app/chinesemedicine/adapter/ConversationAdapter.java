package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.bean.Conversation;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.TimeUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;
import com.wty.app.library.widget.UnreadTextView;

import java.util.List;

/**
 * @Decription 联系人 适配器
 */
public class ConversationAdapter extends BaseRecyclerViewAdapter<Conversation> {
    public ConversationAdapter(Context context, List data) {
        super(context, R.layout.item_conversation, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, final Conversation item, int position) {
        ImageView icon = helper.getView(R.id.iv_recent_avatar);
        TextView time = helper.getView(R.id.tv_recent_time);
        TextView name = helper.getView(R.id.tv_recent_name);
        TextView msg = helper.getView(R.id.tv_recent_msg);
        UnreadTextView unreadtv = helper.getView(R.id.tv_recent_unread);

        msg.setText(item.getLastMessageContent());
        name.setText(item.getcName());
        time.setText(TimeUtil.getChatTime(false,item.getLastMessageTime()));

        //会话图标
        Object obj = item.getAvatar();
        if(obj instanceof String){
            String avatar=(String)obj;
            ImageLoaderUtil.loadCircle(mContext, avatar, R.mipmap.img_contact_default, icon);
        }else{
            int defaultRes = (int)obj;
            ImageLoaderUtil.loadCircle(mContext, defaultRes, icon);
        }

        //查询指定未读消息数
        long unread = item.getUnReadCount();
        if(unread>0){
            unreadtv.setVisibility(View.VISIBLE);
            unreadtv.setText(String.valueOf(unread));
        }else{
            unreadtv.setVisibility(View.GONE);
        }

        helper.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.onClick(v.getContext());
            }
        });

    }

}
