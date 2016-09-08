package com.kw.app.chinesemedicine.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kw.app.chinesemedicine.R;
import com.kw.app.chinesemedicine.data.dalex.local.DynamicDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IDynamicAddContract;
import com.kw.app.chinesemedicine.mvp.presenter.DynamicAddPresenter;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.activity.ImageSelectorActivity;
import com.wty.app.library.adapter.PhotoGridViewAdapter;
import com.wty.app.library.base.AppConstant;
import com.wty.app.library.entity.ImageModel;
import com.wty.app.library.entity.ImageUriEntity;
import com.wty.app.library.utils.NetWorkUtils;
import com.wty.app.library.widget.MyTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;

/**
 * @Description 添加动态
 * @author wty
 **/
public class DynamicAddActivity extends BaseActivity<DynamicAddPresenter> implements IDynamicAddContract.IDynamicAddView {

    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.img_grid_select)
    RecyclerView img_gridview;
    @Bind(R.id.tv_label)
    TextView tv_label;

    PhotoGridViewAdapter adapter;

    public static void startDynamicAddActivity(Context context){
        Intent intent = new Intent(context,DynamicAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    public DynamicAddPresenter getPresenter() {
        return new DynamicAddPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("发表动态");
        getDefaultNavigation().getLeftButton().setText("发现");
        getDefaultNavigation().setRightButton("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        getDefaultNavigation().getRightButton().setEnabled(false);

        adapter = new PhotoGridViewAdapter(this);
        adapter.setGridItemClickListener(listener);
        img_gridview.setLayoutManager(new GridLayoutManager(this, 4));
        img_gridview.setAdapter(adapter);
        registerListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {//从选择图片页面返回
            if (requestCode == AppConstant.ActivityResult.Request_Image) {
                //拿到返回的图片路径
                boolean isCamera = data.getBooleanExtra(ImageSelectorActivity.OUTPUT_ISCAMERA, false);
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                if(isCamera){
                    adapter.addOneImage(images.get(0));
                }else{
                    adapter.bindImagesByPath(images);
                }
            }
        }
    }

    @Override
    protected boolean submit() {
        if(super.submit()){
            mPresenter.submit(getSubmitData());
        }
        return true;
    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_dynamic_add;
    }

    /**
     * @Decription 注册监听
     **/
    private void registerListener(){
        et_content.addTextChangedListener(watcherForContent);
    }

    TextWatcher watcherForContent = new MyTextWatcher() {
        int MAX_LENGTH = 300;
        int rest_Length = 300;

        @Override
        public void afterTextChanged(Editable s) {
            rest_Length = MAX_LENGTH - s.length();
            tv_label.setText("(还能输入" + rest_Length + "个字)");
            if(s.length()==0){
                getDefaultNavigation().getRightButton().setEnabled(false);
            }else{
                getDefaultNavigation().getRightButton().setEnabled(true);
            }
        }
    };

    private PhotoGridViewAdapter.OnGridItemClickListener listener = new PhotoGridViewAdapter.OnGridItemClickListener() {
        final int  MAX_NUM = 9;
        @Override
        public void onAddClick() {
            List<ImageUriEntity> selectedImages = new ArrayList<>();
            List<ImageModel> selected = new ArrayList<>();
            selectedImages.addAll(adapter.getSelectImages());
            for(ImageUriEntity item:selectedImages){
                ImageModel model = new ImageModel(item.uri,"");
                selected.add(model);
            }
            ImageSelectorActivity.start(DynamicAddActivity.this,MAX_NUM,ImageSelectorActivity.MODE_MULTIPLE,true,true,false,selected);
        }

        @Override
        public void onReduceClick(int position, ImageUriEntity item) {
            adapter.remove(position,item);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    };

    private DynamicDALEx getSubmitData() {
        DynamicDALEx dalex = DynamicDALEx.get();
        dalex.setDynamicid(UUID.randomUUID().toString());//主键id
        dalex.setContent(et_content.getText().toString());//填写的内容
        dalex.setImages(adapter.getSelectImagesPath());//图片
        dalex.setSendname("wty");//发送人名
        dalex.setLogourl("");//发送人头像
        return dalex;
    }

    @Override
    public boolean checkNet() {
        return NetWorkUtils.isNetworkConnected(this);
    }

    @Override
    public void showNoNet() {
        showFailed(getString(R.string.network_failed));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
