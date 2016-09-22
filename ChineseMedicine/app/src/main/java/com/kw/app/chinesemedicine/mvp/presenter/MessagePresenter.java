package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.RongConversation;
import com.kw.app.chinesemedicine.mvp.contract.IMessageContract;
import com.kw.app.chinesemedicine.mvp.model.MessageModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * @author wty
 */
public class MessagePresenter extends BasePresenter<IMessageContract.IMessageView> {

    private IMessageContract.IMessageModel mMessageModel;

    public MessagePresenter(){
        mMessageModel = new MessageModel();
    }

    /**
     * @Description 加载所有对话
     **/
    public void getAllConversations(Context context){
        if(!mView.checkNet()){
            mView.showNoNet();
            return ;
        }

         mMessageModel.refreshMessage(context, new ICallBack<List<RongConversation>>() {
             @Override
             public void onSuccess(List<RongConversation> data) {
                 mView.refreshMessage(data);
             }

             @Override
             public void onFaild(String msg) {
                 int i = 0;
             }
         });
    }

    /**
     * @Description 刷新所有对话
     **/
    public void refreshConversations(Context context){
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onRefreshComplete();
            return ;
        }

        mMessageModel.refreshMessage(context, new ICallBack<List<RongConversation>>() {
            @Override
            public void onSuccess(List<RongConversation> data) {
                mView.refreshMessage(data);
                mView.onRefreshComplete();
            }

            @Override
            public void onFaild(String msg) {
                mView.onRefreshComplete();

            }
        });
    }
}
