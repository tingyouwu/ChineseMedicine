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
     * @Description 刷新更多朋友圈动态
     **/
    public List<Conversation> refreshMessage(Context context){
        List<Conversation> conversation = new ArrayList<Conversation>();
        if(!mView.checkNet()){
            mView.showNoNet();
            mView.onRefreshComplete();
            return conversation;
        }

        conversation =  mMessageModel.refreshMessage(context);
        mView.onRefreshComplete();
        return conversation;
    }

    /**
     * @Description 加载所有对话
     **/
    public List<Conversation> getConversations(Context context){
        List<Conversation> conversation = new ArrayList<Conversation>();
        if(!mView.checkNet()){
            mView.showNoNet();
            return conversation;
        }

        conversation =  mMessageModel.refreshMessage(context);
        return conversation;
    }
}
