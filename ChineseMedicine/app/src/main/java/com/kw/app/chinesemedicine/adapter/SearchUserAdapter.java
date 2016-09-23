package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.activity.UserInfoActivity;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.wty.app.library.adapter.BaseRecyclerViewAdapter;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @Decription 搜索好友 适配器
 */
public class SearchUserAdapter extends BaseRecyclerViewAdapter<UserBmob> {
    public SearchUserAdapter(Context context, List data) {
        super(context, R.layout.item_search_user, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder helper, final UserBmob item, int position) {
        TextView name = helper.getView(R.id.name);
        ImageView img = helper.getView(R.id.avatar);
        ImageLoaderUtil.loadCircle(mContext, item.getLogourl(), img);

        name.setText(item.getUsername());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.startUserInfoActivity(mContext,item);
            }
        });

    }

}
