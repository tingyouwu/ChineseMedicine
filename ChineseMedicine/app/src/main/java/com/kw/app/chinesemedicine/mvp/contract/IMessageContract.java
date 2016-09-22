package com.kw.app.chinesemedicine.mvp.contract;

import android.content.Context;
import com.kw.app.chinesemedicine.bean.RongConversation;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IMessageContract {

    interface IMessageModel extends IBaseModel {
        void refreshMessage(Context context,ICallBack<List<RongConversation>> callBack);
    }

    interface IMessageView extends IBaseView {
        boolean checkNet();
        void showNoNet();
        void refreshMessage(List<RongConversation> list);
        void onRefreshComplete();
    }
}
