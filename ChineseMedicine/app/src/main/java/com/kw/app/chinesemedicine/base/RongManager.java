package com.kw.app.chinesemedicine.base;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.kw.app.chinesemedicine.bean.AgreeAddFriendMessage;
import com.kw.app.chinesemedicine.data.dalex.bmob.UserBmob;
import com.kw.app.chinesemedicine.data.dalex.local.NewFriendDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.SystemMessageDALEx;
import com.kw.app.chinesemedicine.data.dalex.local.UserDALEx;
import com.kw.app.chinesemedicine.event.RefreshEvent;
import com.kw.app.chinesemedicine.messagecontent.CustomzeContactNotificationMessage;
import com.wty.app.library.utils.AppLogUtil;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.listener.SaveListener;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;

/**
 * @author wty
 * 融云管理者
 **/
public class RongManager {

	private static volatile RongManager sInstance = null;
	private Context mContext;

	public static RongManager init(Context context) {
		if (sInstance == null) {
			synchronized (RongManager.class) {
				if (sInstance == null) {
					sInstance = new RongManager(context);
				}
			}
		}
		return sInstance;
	}

	private RongManager(Context mContext) {
		this.mContext = mContext;
		RongIMClient.getInstance().setOnReceiveMessageListener(new RongReceiveMessageListener());
		try {
			//在这里注册自定义消息
			RongIMClient.getInstance().registerMessageType(CustomzeContactNotificationMessage.class);
		} catch (AnnotationNotFoundException e) {
			e.printStackTrace();
		}
	}

	class RongReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
		/**
		 * 收到消息的处理。
		 * @param message 收到的消息实体。
		 * @param left 剩余未拉取消息数目。
		 * @return
		 */
		@Override
		public boolean onReceived(Message message, int left) {
			MessageContent content = message.getContent();
			if(content instanceof CustomzeContactNotificationMessage){
				handleCustomzeContactNotificationMessage((CustomzeContactNotificationMessage) content);
			}
			//发送页面刷新的广播
			EventBus.getDefault().post(new RefreshEvent());
			return false;
		}
	}

	/**
	 * 处理一下自定义的通知信息
	 **/
	private void handleCustomzeContactNotificationMessage(CustomzeContactNotificationMessage contactNotificationMessage){

		//更新一下本地用户信息
		BmobUserModel.getInstance().updateUserInfo(contactNotificationMessage.getSourceUserId());

		if (contactNotificationMessage.getOperation().equals(CustomzeContactNotificationMessage.CONTACT_OPERATION_REQUEST)) {
			//对方发来好友邀请
			AppLogUtil.d("对方发来好友邀请");
			NewFriendDALEx friend = AddFriendMessage.convert(contactNotificationMessage);
			//本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
			if(!friend.isExist(friend.getMsgid())){
				friend.saveOrUpdate();
				CMNotificationManager.showNotification(mContext,friend.getName(),friend.getMsg(),null);
			}

			SystemMessageDALEx system = SystemMessageDALEx.convert(contactNotificationMessage);
			system.setType(SystemMessageDALEx.SystemMessageType.AddFriend.code);
			if(!system.isExist(system.getMsgid())){
				system.saveOrUpdate();
			}

		} else if (contactNotificationMessage.getOperation().equals(CustomzeContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE)) {
			//对方同意我的好友请求 此时需要做的事情：1、添加对方为好友，2、显示通知
			AppLogUtil.d("对方同意我的好友邀请");
			AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(contactNotificationMessage);
			addFriend(contactNotificationMessage.getSourceUserId());
			CMNotificationManager.showNotification(mContext, agree.getName(), agree.getMsg(), null);
			SystemMessageDALEx system = SystemMessageDALEx.convert(contactNotificationMessage);
			system.setType(SystemMessageDALEx.SystemMessageType.AgreeAdd.code);
			if(!system.isExist(system.getMsgid())){
				system.saveOrUpdate();
			}

		}else if(contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REJECT_RESPONSE)){
			//对方拒绝我的好友请求
			AppLogUtil.d("对方拒绝我的好友请求");
		}
	}

	/**
	 * 添加对方为自己的好友
	 * @param uid
	 */
	private void addFriend(String uid){
		UserBmob user =new UserBmob();
		user.setObjectId(uid);
		BmobUserModel.getInstance().agreeAddFriend(user, new SaveListener() {
			@Override
			public void onSuccess() {
				AppLogUtil.i("onSuccess");
			}

			@Override
			public void onFailure(int i, String s) {
				AppLogUtil.i("onFailure:" + s + "-" + i);
			}
		});
	}

}
