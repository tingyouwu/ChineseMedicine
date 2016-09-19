package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.Conversation;
import com.kw.app.chinesemedicine.mvp.contract.IMessageContract;
import com.kw.app.chinesemedicine.mvp.model.MessageModel;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.ArrayList;
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
        List<Conversation> conversation = new ArrayList<Conversation>();
        conversation.clear();
        if(!mView.checkNet()){
            mView.showNoNet();
            return ;
        }

        conversation =  mMessageModel.refreshMessage(context);
        mView.refreshMessage(conversation);
    }

    /**
     * @Description 刷新所有对话
     **/
    public void refreshConversations(Context context){
        List<Conversation> conversation = new ArrayList<Conversation>();
        conversation.clear();
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onRefreshComplete();
            return ;
        }

        conversation =  mMessageModel.refreshMessage(context);
        mView.onRefreshComplete();
        mView.refreshMessage(conversation);
    }
}
