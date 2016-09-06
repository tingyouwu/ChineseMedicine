package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.ContactDALEx;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @Decription 热门 适配器
 */
public class ContactAdapter extends BaseRecyclerViewAdapter<ContactDALEx> {
    public ContactAdapter(Context context, List<ContactDALEx> data) {
        super(context, R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, ContactDALEx item, int position) {
        TextView name = helper.getView(R.id.tv_name);
        ImageView phone = helper.getView(R.id.iv_phone);
        name.setText(item.getUsername());
    }

}
