package com.wty.app.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.library.R;
import com.wty.app.library.widget.goodview.GoodView;


public class ItemView extends LinearLayout{

    TextView tv_Label;
    ImageButton iv_img;
    GoodView mGoodView;

    private ColorStateList mTextColor;

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.item_tab, this);
        iv_img = (ImageButton)findViewById(R.id.img_item_tab);
        tv_Label = (TextView)findViewById(R.id.tv_item_tab);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        int resourceId = ta.getResourceId(R.styleable.ItemView_itemViewImage, 0);

        String label = ta.getString(R.styleable.ItemView_itemViewText);

        if(resourceId != 0){
            iv_img.setBackgroundResource(resourceId);
        }

        ColorStateList tabTextColor = ta.getColorStateList(R.styleable.ItemView_itemViewTextColor);
        mTextColor = (tabTextColor != null ? tabTextColor : context.getResources().getColorStateList(R.color.gray_font_3));
        tv_Label.setTextColor(mTextColor);

        if(!TextUtils.isEmpty(label)){
            tv_Label.setText(label);
        }

        ta.recycle();
	}

    public void setSelected(boolean isSelected){
        tv_Label.setSelected(isSelected);
        iv_img.setSelected(isSelected);
    }


    public void setCount(int count){
        tv_Label.setText("" + count);
    }

    public void setGoodView(){
        if(mGoodView == null){
            mGoodView = new GoodView(getContext());
        }
        mGoodView.setText("+1");
        mGoodView.show(tv_Label);
    }

}
