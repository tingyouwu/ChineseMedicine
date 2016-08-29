package com.wty.app.library.widget.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.wty.app.library.adapter.BaseRecyclerViewAdapter;

/**
 * @Decription 优化XRecyclerView
 * @author wty
 **/
public class MyXRecyclerView extends XRecyclerView implements GestureDetector.OnGestureListener{

    private BaseRecyclerViewAdapter adapter;
    private boolean scrlled;

    public MyXRecyclerView(Context context) {
        super(context);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new ImageAutoLoadScrollListener());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof BaseRecyclerViewAdapter) {
            this.adapter = (BaseRecyclerViewAdapter) adapter;
        }
        super.setAdapter(adapter);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(Math.abs(velocityY) > 4000){
            //选择Math.abs(v1) > 4000来作为快速滑动的判断原因，自己测出来的。。。这个可以自己测试来定，一般快速滑动时，v1的值在4000+。
            adapter.setScrolling(true);
        }
        return false;
    }

    public class ImageAutoLoadScrollListener extends OnScrollListener{

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy != 0){
                //scrolled是用来判断列表有没有发生滚动位移。
                scrlled = true;
            }
        }

        /**
         * 优化前：列表只要滚动停止就加载，不管用户滚动了多少，不管是否快速滚动
         * 优化后：
         *  1.能否检测是不是快速滚动，只有快速滚动中不加载图片及复杂布局。
         *  2.滚动停止时，判断是否存在图片或布局未加载，如果有，才加载。否则，不做任何操作。
         **/

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                    //当前屏幕停止滚动
                    if(adapter.getScrollingStatus() && scrlled) {
                        //通过判断mAdapter.getScrolling()是否为true，来判断是不是有未加载图片
                        adapter.setScrolling(false);
                        //刷新当前可见区域
                        adapter.notifyDataSetChanged();
                    }

                    scrlled = false;
                    break;
                case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                    //屏幕在滚动 且 用户仍在触碰或手指还在屏幕上
                    break;
                case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under
                    //随用户的操作，屏幕上产生的惯性滑动
                    break;
            }
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

}