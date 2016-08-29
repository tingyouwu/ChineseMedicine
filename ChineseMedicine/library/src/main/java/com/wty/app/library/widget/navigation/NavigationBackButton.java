package com.wty.app.library.widget.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.wty.app.library.R;

/**
 * @Description 标题栏返回键
 **/
public class NavigationBackButton extends NavigationButton {

    public NavigationBackButton(Context context){
        this(context,null);
    }

    public NavigationBackButton(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }


    private void init(){
        setRootGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        setButton(R.drawable.actionbar_back, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v.getContext() instanceof Activity){
                    ((Activity)v.getContext()).finish();
                }
            }
        });
    }

}