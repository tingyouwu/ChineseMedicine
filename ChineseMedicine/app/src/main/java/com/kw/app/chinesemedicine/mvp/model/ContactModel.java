package com.kw.app.chinesemedicine.mvp.model;

import android.content.Context;

import com.kw.app.chinesemedicine.base.BmobUserModel;
import com.kw.app.chinesemedicine.bean.Friend;
import com.kw.app.chinesemedicine.data.dalex.local.FriendRelationDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.mvp.contract.IContactContract;
import com.wty.app.library.callback.ICallBack;

import java.util.List;

/**
 * @author wty
 */
public class ContactModel implements IContactContract.IContactModel {

    @Override
    public void refreshFriend(Context context, final ICallBack<List<UserDALEx>> callBack) {

        FriendRelationDALEx relation = FriendRelationDALEx.get().getNewestRelation();

        String updatetime = null;
        if(relation !=null){
            updatetime = relation.getUpdateAt();
        }

        BmobUserModel.getInstance().queryFriends(updatetime, new ICallBack<List<Friend>>() {
            @Override
            public void onSuccess(List<Friend> list) {
                Friend.get().save(list);
                callBack.onSuccess(UserDALEx.get().findAllFriend());
            }

            @Override
            public void onFaild(String msg) {
                callBack.onFaild(msg);
            }
        });
    }
}
