package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.wty.app.library.adapter.BaseViewCommonAdapter;
import com.wty.app.library.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @Decription 主页界面 适配器
 */
public class DialogListAdapter extends BaseViewCommonAdapter<String> {
    public DialogListAdapter(Context context, List data) {
        super(context, R.layout.item_dialog_text, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv = helper.getView(R.id.tv_content);
        tv.setText(item);
    }

}
