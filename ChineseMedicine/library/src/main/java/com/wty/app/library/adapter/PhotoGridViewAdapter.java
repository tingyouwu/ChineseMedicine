package com.wty.app.library.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.wty.app.library.R;
import com.wty.app.library.entity.ImageUriEntity;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Decription 图片选择器适配器
 * @author wty
 **/
public class PhotoGridViewAdapter extends BaseRecyclerViewMultiItemAdapter<ImageUriEntity> {

	private boolean isReducing = false;//是否正在处于删除
	private int Max_num = 9;//最多9个
	private int count_select = 0;

	public PhotoGridViewAdapter(Context context) {
		super(context);
		addItemType(ImageUriEntity.TYPE_NORMAL, R.layout.item_photo_grid);
		addItemType(ImageUriEntity.TYPE_ADD, R.layout.item_photo_grid_add);
		addItemType(ImageUriEntity.TYPE_DELETE, R.layout.item_photo_grid_delete);
		init();
	}

	/**
	 * @Decription 初始化需要添加
	 **/
	private void init(){
		this.mData.clear();
		this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_ADD, ""));
	}

	@Override
	protected void bindView(BaseRecyclerViewHolder helper, ImageUriEntity item, int position) {
		switch (helper.getItemViewType()){
			case ImageUriEntity.TYPE_NORMAL://正常图片
				setNormaItem(helper,item,position);
				break;
			case ImageUriEntity.TYPE_ADD://添加
				//倒数第二个
				setAddItem(helper);
				break;
			case ImageUriEntity.TYPE_DELETE://删除
				//倒数第一个
				setReduceItem(helper);
				break;
			default:
				break;
		}
	}

	@Override
	protected int getItemMultiViewType(int position) {
		return getItem(position).type;
	}

	/**
	 * @Decription 设置最大图片数
	 **/
	public void setMax(int max){
		Max_num = max;
	}

	/**
	 * @Decription 设置 - 图片
	 **/
	private void setReduceItem(BaseRecyclerViewHolder helper) {
		helper.getView(R.id.item_grid_icon_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isReducing = !isReducing;
				notifyDataSetChanged();
			}
		});
	}

	/**
	 * @Decription 设置 + 图片
	 **/
	private void setAddItem(BaseRecyclerViewHolder helper) {
		helper.getView(R.id.item_grid_icon_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isReducing = false;
				if (onGridItemClickListener != null) {
					onGridItemClickListener.onAddClick();
				}
			}
		});
	}

	/**
	 * @Decription 设置正常的图片
	 **/
	private void setNormaItem(BaseRecyclerViewHolder helper, final ImageUriEntity item, final int position) {
		ImageView reduce = helper.getView(R.id.item_grid_reduce);
		ImageView icon = helper.getView(R.id.item_grid_icon);

		if(isReducing){
			//删除状态，显示红色删除按钮
			reduce.setVisibility(View.VISIBLE);
		}else{
			reduce.setVisibility(View.GONE);
		}

		reduce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onGridItemClickListener != null){
					onGridItemClickListener.onReduceClick(position,item);
				}
			}
		});

		ImageLoaderUtil.load(mContext, item.uri, icon);
		icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//图片点击事件
				if (onGridItemClickListener != null) {
					onGridItemClickListener.onItemClick(v, position);
				}
			}
		});

	}

	/**
	 * @Decription 绑定最新数据
	 **/
	public void bindImagesByPath(List<String> data){

		if(data != null && data.size()>0){
			this.mData.clear();
			if(data.size()>=Max_num){
				for(String path:data.subList(0,Max_num)){
					this.mData.add(new ImageUriEntity(path));
				}
				this.count_select = Max_num;
			}else{
				for(String path:data){
					this.mData.add(new ImageUriEntity(path));
				}
				this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_ADD, ""));
				this.count_select = data.size();
			}
			this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_DELETE, ""));
			notifyDataSetChanged();
		}
	}

	/**
	 * @Decription 绑定最新数据
	 **/
	public void bindImages(List<ImageUriEntity> data){
		if(data == null || data.size()==0)return;
		this.mData.clear();
		if(data.size()>=Max_num){
			this.mData.addAll(data.subList(0, Max_num));
			this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_DELETE, ""));
			this.count_select = Max_num;
		}else{
			this.mData.addAll(data);
			this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_ADD, ""));
			this.mData.add(new ImageUriEntity(ImageUriEntity.TYPE_DELETE, ""));
			this.count_select = data.size();
		}
		notifyDataSetChanged();
	}

	/**
	 * @Decription 获取已经选择的图片
	 **/
	public List<ImageUriEntity> getSelectImages(){
		return this.mData.subList(0,count_select);
	}

	/**
	 * @Decription 获取已经选择的图片,以逗号隔开
	 **/
	public String getSelectImagesPath(){
		List<String> path = new ArrayList<String>();
		for(ImageUriEntity item:getSelectImages()){
			path.add(item.uri);
		}
		return path.size()==0?"":TextUtils.join(",",path);
	}

	/**
	 * @Decription 增加一个图片
	 **/
	public void addOneImage(String  path){
		int old_count_select = this.count_select;
		if(old_count_select>=Max_num)return;
		if(old_count_select == 0){
			//添加前一个数据都没有,默认只有一个+
			super.add(this.mData.size(), new ImageUriEntity(ImageUriEntity.TYPE_DELETE, ""));
		}else if(old_count_select == Max_num - 1){
			//干掉 +
			super.remove(getItemCount()-2);
		}
		super.add(old_count_select,new ImageUriEntity(path));
		this.count_select ++;
	}

	public void remove(int position,ImageUriEntity item) {
		if(item.type!=ImageUriEntity.TYPE_NORMAL) return;
		if(count_select ==1){
			//删除前只有最后一个数据了,那么只需要显示一个 +,需要干掉-
			super.remove(this.mData.size()-1);
		}else if(count_select == Max_num){
			//删除前已经到达最大值,这时只显示一个- 需要把加号也显示出来
			super.add(this.mData.size()-1,new ImageUriEntity(ImageUriEntity.TYPE_ADD, ""));
		}

		count_select --;//真实数据少一个
		super.remove(position);
		if(count_select == 0){
			//没有数据了,重置一下状态
			isReducing = false;
		}
	}

	public void setGridItemClickListener(OnGridItemClickListener listener){
		this.onGridItemClickListener = listener;
	}

	//item正常点击事件
	private OnGridItemClickListener onGridItemClickListener;

	public interface OnGridItemClickListener{
		void onAddClick();
		void onReduceClick(int position, ImageUriEntity mImageUriEntity);
		void onItemClick(View view, int position);

	}
}
