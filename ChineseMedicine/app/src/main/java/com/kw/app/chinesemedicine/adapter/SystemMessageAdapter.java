package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.NewFriendActivity;
import com.kw.app.chinesemedicine.data.dalex.local.SystemMessageDALEx;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.TimeUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @Decription
 * 系统消息 适配器
 */
public class SystemMessageAdapter extends BaseRecyclerViewAdapter<SystemMessageDALEx> {
    public SystemMessageAdapter(Context context, List data) {
        super(context, R.layout.item_system_message, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, final SystemMessageDALEx item, int position) {
        ImageView icon = helper.getView(R.id.iv_recent_avatar);
        TextView name = helper.getView(R.id.tv_recent_name);
        TextView time = helper.getView(R.id.tv_recent_time);
        TextView msg = helper.getView(R.id.tv_recent_msg);
        View line = helper.getView(R.id.line);

        name.setText(item.getName());
        msg.setText(item.getMsg());
        time.setText(TimeUtil.getChatTime(false,item.getTime()));

        if(position == mData.size()-1){
            //最后一个不用显示线条
            line.setVisibility(View.GONE);
        }
        ImageLoaderUtil.loadCircle(mContext,item.getAvator(),R.mipmap.img_contact_default,icon);

        if(item.getType()==SystemMessageDALEx.SystemMessageType.AddFriend.code){
            //如果是好友邀请就跳到 新朋友页面
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewFriendActivity.startNewFriendActivity(mContext);
                }
            });
        }else if(item.getType()== SystemMessageDALEx.SystemMessageType.AgreeAdd.code){
            //如果是已经同意添加就跳到聊天页面
        }else{
            //拒绝添加 就不响应点击
        }

    }
}
