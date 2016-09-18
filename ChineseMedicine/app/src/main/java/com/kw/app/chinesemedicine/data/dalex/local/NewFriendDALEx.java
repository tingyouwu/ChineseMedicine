package com.kw.app.chinesemedicine.data.dalex.local;

import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.wty.app.library.data.QueryBuilder;
import com.wty.app.library.data.annotation.DatabaseField;
import com.wty.app.library.data.annotation.DatabaseField.FieldType;
import com.wty.app.library.data.annotation.SqliteDao;
import com.wty.app.library.data.dalex.SqliteBaseDALEx;

import java.util.ArrayList;
import java.util.List;

/**
 * @Decription
 * 本地好友请求表
 **/
public class NewFriendDALEx extends SqliteBaseDALEx{

    public static final String STATUS = "status";

    @DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
    private String msgid;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String uid;//发送者的uid
    @DatabaseField(Type = FieldType.VARCHAR)
    private String msg;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String name;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String avatar;//发送者的头像
    @DatabaseField(Type = FieldType.INT)
    private int status;
    @DatabaseField(Type = FieldType.INT)
    private long time;

    public static NewFriendDALEx get() {
        return SqliteDao.getDao(NewFriendDALEx.class);
    }

    /**
     * 是否有新的好友邀请
     * @return
     */
    public boolean hasNewFriendInvitation(){
        List<NewFriendDALEx> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取未读的好友邀请
     * @return
     */
    public int getNewInvitationCount(){
        List<NewFriendDALEx> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return infos.size();
        }else{
            return 0;
        }
    }

    /**
     * 获取所有未读未验证的好友请求
     * @return
     */
    private List<NewFriendDALEx> getNoVerifyNewFriend(){
        return findList(new QueryBuilder().selectAll()
                .from(TABLE_NAME)
                .where(equal(STATUS, AddFriendMessage.STATUS_VERIFY_NONE))
                .build());
    }

    /**
     * 批量更新未读未验证的状态为已读
     */
    public void updateBatchStatus(){
        List<NewFriendDALEx> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            int size =infos.size();
            List<NewFriendDALEx> all =new ArrayList<>();
            for (int i = 0; i < size; i++) {
                NewFriendDALEx msg =infos.get(i);
                msg.setStatus(AddFriendMessage.STATUS_VERIFY_READED);
                all.add(msg);
            }
            saveOrUpdate(infos);
        }
    }

    /**
     * 修改指定好友请求的状态
     * @param friend
     * @param status
     * @return
     */
    public void updateNewFriend(NewFriendDALEx friend,int status){
        friend.setStatus(status);
        friend.saveOrUpdate();
    }

    /**
     * 删除指定的添加请求
     * @param friend
     */
    public void deleteNewFriend(NewFriendDALEx friend){
        friend.deleteById(friend.getMsgid());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
