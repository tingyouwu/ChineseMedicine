package com.wty.app.library.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.wty.app.library.entity.IMultiItemEntity;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;
import com.wty.app.library.viewholder.BaseViewHolder;

import java.util.List;

/**
 * 功能描述：通用recyclerview adapter（多布局）
 */
public abstract class BaseRecyclerViewMultiItemAdapter<T extends IMultiItemEntity> extends BaseRecyclerViewAdapter {

    /**
     * Map用于存储  类型以及 对应的布局文件  调用addItemType吧少年
     */
    private SparseArray<Integer> layouts;

    public BaseRecyclerViewMultiItemAdapter(Context context) {
        super(context,null);
    }

    public BaseRecyclerViewMultiItemAdapter(Context context,List<T> data) {
        super(context,data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return ((IMultiItemEntity) mData.get(position)).getItemType();
    }

    @Override
    protected BaseRecyclerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    /**
     * @Decription 添加多布局文件
     **/
    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }


    @Override
    protected void convert(BaseRecyclerViewHolder helper, Object item,int position) {
        convert(helper, (T) item,position);
    }

    /**
     * @Decription 绑定数据
     **/
    protected abstract void convert(BaseRecyclerViewHolder helper, T item,int position);

}


