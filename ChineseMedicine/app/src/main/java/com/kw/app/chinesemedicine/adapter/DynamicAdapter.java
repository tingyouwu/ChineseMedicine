package com.kw.app.chinesemedicine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.wty.app.library.activity.ImagePagerActivity;
import com.wty.app.library.adapter.BaseRecyclerViewMultiItemAdapter;
import com.wty.app.library.adapter.NineGridImageViewAdapter;
import com.wty.app.library.bean.ImageSizeBean;
import com.wty.app.library.utils.ImageLoaderUtil;
import com.wty.app.library.utils.ScreenUtil;
import com.wty.app.library.viewholder.BaseRecyclerViewHolder;
import com.wty.app.library.widget.imageview.ColorFilterImageView;
import com.wty.app.library.widget.imageview.NineGridImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Decription 热门 适配器
 */
public class DynamicAdapter extends BaseRecyclerViewMultiItemAdapter<DynamicDALEx> {
    public DynamicAdapter(Context context, List<DynamicDALEx> data) {
        super(context, data);
        addItemType(DynamicDALEx.No_Picture,R.layout.fragment_dynamic_onlytext);
        addItemType(DynamicDALEx.OnlyOne_Picture, R.layout.fragment_dynamic_oneitem);
        addItemType(DynamicDALEx.Multi_Picture,R.layout.fragment_dynamic_multiitem);
    }

    @Override
    protected void bindView(BaseRecyclerViewHolder helper, DynamicDALEx item, int position) {
        TextView tv_content = helper.getView(R.id.tv_content);
        tv_content.setText(item.getContent());

        switch (helper.getItemViewType()){
            case DynamicDALEx.No_Picture:
                break;

            case DynamicDALEx.OnlyOne_Picture:
                final List<String> listOne = Arrays.asList(item.getImages().split(","));
                ColorFilterImageView img = helper.getView(R.id.oneImagView);
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                int width,height;
                if(item.getSinglesize()>1.0){
                    //宽>高
                    width = ScreenUtil.dp2px(mContext,200);
                    height = (int)(width / item.getSinglesize());
                }else if(item.getSinglesize()<1.0){
                    //高大于宽
                    width = ScreenUtil.dp2px(mContext,150);
                    height = (int)(width / item.getSinglesize());
                }else {
                    //宽等于高
                    width = ScreenUtil.dp2px(mContext,150);
                    height = width;
                }

                lp.width = width;
                lp.height = height;

                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                img.setLayoutParams(lp);

                ImageLoaderUtil.load(mContext,item.getImages(),img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageSizeBean imageSize = new ImageSizeBean(v.getMeasuredWidth(),v.getMeasuredHeight());
                        ImagePagerActivity.startImagePagerActivity(mContext, listOne, 0, imageSize);
                    }
                });
                break;

            case DynamicDALEx.Multi_Picture:
                List<String> list = Arrays.asList(item.getImages().split(","));
                NineGridImageView imageView = helper.getView(R.id.multiImagView);
                imageView.setAdapter(mAdapter);
                List<String> data = new ArrayList<>();
                data.addAll(list);
                imageView.setImagesData(data);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getItemMultiViewType(int position) {
        DynamicDALEx item = getItem(position);
        int length = item.getImages().split(",").length;
        if(length > 1)
            return DynamicDALEx.Multi_Picture;
        else if (length ==1)
            return DynamicDALEx.OnlyOne_Picture;

        return DynamicDALEx.No_Picture;
    }

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String path) {
            ImageLoaderUtil.load(mContext,path,imageView);
        }

        @Override
        public void onItemImageClick(Context context,View v, int index, List<String> list) {
            ImageSizeBean imageSize = new ImageSizeBean(v.getMeasuredWidth(),v.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(context,list,index,imageSize);
        }
    };

}
