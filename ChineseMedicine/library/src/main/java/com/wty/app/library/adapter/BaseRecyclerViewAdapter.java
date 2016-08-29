package com.wty.app.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：通用recyclerview adapter
 * @author wty
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;//item点击事件
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;//item长按事件
    private OnRecyclerViewItemChildClickListener mChildClickListener;
    private OnRecyclerViewItemChildLongClickListener mChildLongClickListener;

    protected Context mContext;
    protected int mLayoutResId;//布局文件id
    protected List<T> mData;
    private View mContentView;
    private boolean isScrolling = false;

    public BaseRecyclerViewAdapter(Context context) {
        this(context,0, null);
    }

    public BaseRecyclerViewAdapter(Context context,List<T> data) {
        this(context,0, data);
    }

    public BaseRecyclerViewAdapter(Context context,View contentView, List<T> data) {
        this(context,0, data);
        mContentView = contentView;
    }

    public BaseRecyclerViewAdapter(Context context,int layoutResId, List<T> data) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder BaseRecyclerViewHolder = onCreateDefViewHolder(parent, viewType);
        return BaseRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        convert((BaseRecyclerViewHolder)holder,mData.get(position),position);
    }

    protected BaseRecyclerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseRecyclerViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return BaseRecyclerViewHolder.get(mContext,parent,layoutResId);
        }
        return new BaseRecyclerViewHolder(mContentView);
    }

    /**
     * 功能描述：注册recyclerview  item点击监听
     **/
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 功能描述：注册recyclerview  item长按监听
     **/
    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public interface OnRecyclerViewItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    /**
     * 功能描述：注册item 内某一个 子view的监听
     */
    public void setOnRecyclerViewItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null)
                mChildClickListener.onItemChildClick(BaseRecyclerViewAdapter.this, v, mViewHolder.getLayoutPosition());
        }
    }

    public void setOnRecyclerViewItemChildLongClickListener(OnRecyclerViewItemChildLongClickListener childLongClickListener) {
        this.mChildLongClickListener = childLongClickListener;
    }

    public interface OnRecyclerViewItemChildLongClickListener {
        boolean onItemChildLongClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    public class OnItemChildLongClickListener implements View.OnLongClickListener {
        public RecyclerView.ViewHolder mViewHolder;
        @Override
        public boolean onLongClick(View v) {
            if (mChildLongClickListener != null) {
                return mChildLongClickListener.onItemChildLongClick(BaseRecyclerViewAdapter.this, v, mViewHolder.getLayoutPosition());
            }
            return false;
        }
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mData.size()-position);

    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void addData(List<T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(int position,List<T> data) {
        this.mData.addAll(position,data);
        notifyDataSetChanged();
    }

    public void retsetData(List<T> data){
        this.mData.clear();
        addData(data);
    }

    public void clearData(){
        this.mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 功能描述：绑定数据
     **/
    protected abstract void convert(BaseRecyclerViewHolder helper, T item,int position);

    /**
     * 功能描述：item的类型（适用于多布局）
     **/
    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setScrolling(boolean isScrolling){
        this.isScrolling = isScrolling;
    }

    public boolean getScrollingStatus(){
        return this.isScrolling;
    }
}
