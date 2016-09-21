package com.kw.app.chinesemedicine.base;

import android.content.Context;

import com.kw.app.chinesemedicine.bean.AddFriendMessage;
import com.wty.app.library.utils.AppLogUtil;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

/**
 * @author wty
 * 融云管理者
 **/
public class RongManager {

	private static volatile RongManager sInstance = null;

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
		RongIMClient.getInstance().setOnReceiveMessageListener(new RongReceiveMessageListener());
	}

	/**
	 * 设置当前用户信息，
	 * @param userInfo 当前用户信息
	 */
	public void setCurrentUserInfo(UserInfo userInfo){
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
			AppLogUtil.d("收到服务端发来的消息:");
			MessageContent content = message.getContent();
			if(content instanceof ContactNotificationMessage){
				ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) content;
				UserInfo info = contactNotificationMessage.getUserInfo();
				if (contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)) {
					//对方发来好友邀请
					AddFriendMessage.convert(contactNotificationMessage);
				} else if (contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE)) {
					//对方同意我的好友请求

				}else if(contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REJECT_RESPONSE)){
					//对方拒绝我的好友请求
				}
			}
			return false;
		}
	}

}
