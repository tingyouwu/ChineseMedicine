package com.kw.app.chinesemedicine.mvp.contract;

import android.content.Context;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.model.IBaseModel;
import com.wty.app.library.mvp.view.IBaseView;

import java.util.List;

/**
 * @author wty
 */
public interface IContactContract {

    interface IContactModel extends IBaseModel {
        void refreshFriend(Context context,ICallBack<List<UserDALEx>> callBack);
    }

    interface IContactView extends IBaseView {
        boolean checkNet();
        void showNoNet();
        void refreshFriend(List<UserDALEx> list);
    }
}
