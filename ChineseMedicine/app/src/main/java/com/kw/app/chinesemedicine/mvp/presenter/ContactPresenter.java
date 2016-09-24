package com.kw.app.chinesemedicine.mvp.presenter;

import android.content.Context;
import com.kw.app.chinesemedicine.data.dalex.local.FriendRelationDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactContract;
import com.kw.app.chinesemedicine.mvp.model.ContactModel;
import com.wty.app.library.callback.ICallBack;
import com.wty.app.library.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * @author wty
 */
public class ContactPresenter extends BasePresenter<IContactContract.IContactView> {

    private IContactContract.IContactModel mContactModel;

    public ContactPresenter(){
        mContactModel = new ContactModel();
    }

    /**
     * @Decription 根据本地最新的一条数据 去服务端拿新数据(比较更新时间)
     **/
    public void refreshFriend(Context context){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }

        mContactModel.refreshFriend(context,new ICallBack<List<UserDALEx>>(){
            @Override
            public void onSuccess(List<UserDALEx> data) {
                mView.refreshFriend(data);
            }

            @Override
            public void onFaild(String msg) {

            }
        });
    }

    /**
     * @Decription 从本地获得所有的朋友
     **/
    public void loadAllFriend(){
        mView.refreshFriend(UserDALEx.get().findAllFriend());
    }
}
