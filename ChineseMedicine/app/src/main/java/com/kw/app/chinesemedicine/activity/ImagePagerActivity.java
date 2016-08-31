package com.kw.app.chinesemedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kw.app.chinesemedicine.adapter.ImagePagerAdapter;
import com.kw.app.chinesemedicine.bean.ImageSizeBean;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.mvp.presenter.BasePresenter;
import com.wty.app.library.widget.viewpager.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Decription 图片浏览
 * @author wty
 */
public class ImagePagerActivity extends BaseActivity {

    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";

    @Bind(com.wty.app.library.R.id.guideGroup)
    LinearLayout guideGroup;
    @Bind(com.wty.app.library.R.id.pager)
    ImageViewPager viewPager;

    private List<View> guideViewList = new ArrayList<>();
    public ImageSizeBean imageSize;
    private int startPos;
    private ArrayList<String> imgUrls;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutResource() {
        return com.wty.app.library.R.layout.activity_imagepager;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {

        //全屏  隐藏状态栏
        setStatusBarTintRes(com.wty.app.library.R.color.black);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSizeBean) getIntent().getSerializableExtra(INTENT_IMAGESIZE);

        ImagePagerAdapter mAdapter = new ImagePagerAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<guideViewList.size(); i++){
                    guideViewList.get(i).setSelected(i==position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(startPos);
        addGuideView(guideGroup, startPos, imgUrls);

    }

    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSizeBean imageSize){
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        context.startActivity(intent);
    }

    /**
     * @Decription 添加底部指示view
     **/
    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if(imgUrls==null || imgUrls.size()==1)return;

        if(imgUrls!=null && imgUrls.size()>0){
            guideViewList.clear();
            for (int i=0; i<imgUrls.size(); i++){
                View view = new View(this);
                view.setBackgroundResource(com.wty.app.library.R.drawable.selector_guide_bg);
                view.setSelected(i==startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(com.wty.app.library.R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(com.wty.app.library.R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
}
