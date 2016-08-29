package com.wty.app.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.library.R;
import com.wty.app.library.widget.loadingview.OnRetryListener;

/**
 * @Decription 空白view
 **/
public class EmptyViewLayout extends LinearLayout {

	private String mLoaded_empty_text = "亲,生活好单调，一个数据都没有";//加载数据为空提示文字
	private int mEmptyIco = R.drawable.icon_no_data;//加载数据为空时图标
	private OnRetryListener mOnRetryListener;
	// 是否使能点击刷新按钮
	public boolean btn_empty_ennable = false;
	public String btn_empty_text = "点击刷新";

	TextView tv_loaded;
	ImageView iv_loaded;
	Button btn_loaded;

	public EmptyViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_empty_layout, this);
		tv_loaded = (TextView)findViewById(R.id.tv_loaded);
		iv_loaded = (ImageView)findViewById(R.id.iv_loaded);
	}

	public EmptyViewLayout withBtnEmptyEnnable(boolean ennable) {
		btn_empty_ennable = ennable;
		return this;
	}

	public EmptyViewLayout withLoadedEmptyText(int resId) {
		mLoaded_empty_text = getResources().getString(resId);
		return this;
	}

	public EmptyViewLayout withLoadedEmptyText(String mLoadedemptyText) {
		this.mLoaded_empty_text = mLoadedemptyText;
		return this;
	}

	public EmptyViewLayout withEmptyIco(int resId) {
		mEmptyIco = resId;
		return this;
	}

	public EmptyViewLayout withOnRetryListener(OnRetryListener mOnRetryListener) {
		this.mOnRetryListener = mOnRetryListener;
		return this;
	}

	public void build(Context context) {

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_empty_layout, this);
		tv_loaded = (TextView)findViewById(R.id.tv_loaded);
		iv_loaded = (ImageView)findViewById(R.id.iv_loaded);
		btn_loaded = (Button)findViewById(R.id.btn_loaded);

		iv_loaded.setImageResource(mEmptyIco);
		tv_loaded.setText(mLoaded_empty_text);
		if (btn_empty_ennable) {
			btn_loaded.setVisibility(VISIBLE);
			btn_loaded.setText(btn_empty_text);
		} else {
			btn_loaded.setVisibility(GONE);
		}

		btn_loaded.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnRetryListener != null) {
					mOnRetryListener.onRetry();
				}
			}
		});
	}

}
