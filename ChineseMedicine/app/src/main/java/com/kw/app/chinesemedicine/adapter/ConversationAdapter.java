package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.bean.Conversation;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @Decription 联系人 适配器
 */
public class ConversationAdapter extends BaseRecyclerViewAdapter<Conversation> {
    public ConversationAdapter(Context context, List data) {
        super(context, R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, Conversation item, int position) {

    }

}
