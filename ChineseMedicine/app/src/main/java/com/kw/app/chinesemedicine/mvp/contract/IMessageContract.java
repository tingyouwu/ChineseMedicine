package com.kw.app.chinesemedicine.mvp.contract;


import android.content.Context;

import com.kw.app.chinesemedicine.bean.Conversation;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IMessageContract {

    interface IMessageModel extends IBaseModel {
        List<Conversation> refreshMessage(Context context);
    }

    interface IMessageView extends IBaseView {
        boolean checkNet();
        void showNoNet();
        void refreshMessage(List<Conversation> list);
        void onRefreshComplete();
    }
}
