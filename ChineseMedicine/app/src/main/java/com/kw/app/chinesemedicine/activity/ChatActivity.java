package com.kw.app.chinesemedicine.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kw.app.chinesemedicine.adapter.ChatAdapter;
import com.orhanobut.logger.Logger;
import com.wty.app.bmobim.R;
import com.wty.app.library.activity.BaseActivity;
import com.wty.app.library.utils.CommonUtil;
import com.wty.app.library.utils.FileUtils;
import com.wty.app.library.widget.DivItemDecoration;
import com.wty.app.library.widget.xrecyclerview.ProgressStyle;
import com.wty.app.library.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity implements MessageListHandler {

    public static final String CONVERSATION = "conversation";

    Toast toast;
    XRecyclerView rc_view;
    EditText edit_msg;
    Button btn_speak,btn_chat_voice,btn_chat_keyboard,btn_chat_send,btn_chat_add;
    LinearLayout layout_more,layout_add;

    // 语音有关
    RelativeLayout layout_record;
    TextView tv_voice_tips;
    TextView tv_picture,tv_camera,tv_location;
    ImageView iv_record;
    BmobRecordManager recordManager;
    ChatAdapter adapter;
    LinearLayoutManager layoutManager;
    BmobIMConversation conversation;

    private List<BmobIMMessage> msgs = new ArrayList<BmobIMMessage>();

    private Drawable[] drawable_Anims;// 话筒动画

    /**
     * 初始化标题栏
     **/
    private void initTitle(){
        getDefaultNavigation().setTitle(conversation.getConversationTitle());
    }

    /**
     * 初始化内容
     **/
    private void initSwipeLayout(){
        adapter = new ChatAdapter(this,msgs,conversation);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.addItemDecoration(new DivItemDecoration(15, false));
        rc_view.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        rc_view.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        rc_view.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }

            @Override
            public void onLoadMore() {
            }

        });
        rc_view.setAdapter(adapter);

        queryMessages(null);

//        //设置RecyclerView的点击事件
//        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
//            @Override
//            public void onItemClick(int position) {
//                Logger.i("" + position);
//            }
//
//            @Override
//            public boolean onItemLongClick(int position) {
//                //这里省了个懒，直接长按就删除了该消息
//                conversation.deleteMessage(adapter.getItem(position));
//                adapter.remove(position);
//                return true;
//            }
//        });
    }

    private void initBottomView(){
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN||event.getAction()== MotionEvent.ACTION_UP){
                    scrollToBottom();
                }
                return false;
            }
        });

        edit_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化语音布局
     * @param
     * @return void
     */
    private void initVoiceView() {

        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6) };// 话筒动画

        btn_speak.setOnTouchListener(new VoiceTouchListener());
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Logger.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        conversation = (BmobIMConversation)getIntent().getSerializableExtra(CONVERSATION);

        rc_view = (XRecyclerView) findViewById(R.id.rc_view);
        edit_msg = (EditText) findViewById(R.id.edit_msg);
        btn_speak = (Button) findViewById(R.id.btn_speak);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_add = (Button) findViewById(R.id.btn_chat_add);

        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        layout_record = (RelativeLayout) findViewById(R.id.layout_record);
        tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
        iv_record = (ImageView) findViewById(R.id.iv_record);

        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_location = (TextView) findViewById(R.id.tv_location);

        initTitle();
        initListener();
        initSwipeLayout();
        initVoiceView();
        initBottomView();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_chat;
    }

    /**
     * 显示录音时间过短的Toast
     * @Title: showShortToast
     * @return void
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * 初始化各种监听
     **/
    private void initListener(){
        edit_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.VISIBLE) {
                    //隐藏底部
                    layout_add.setVisibility(View.GONE);
                    layout_more.setVisibility(View.GONE);
                }
            }
        });

        btn_chat_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.GONE) {
                    layout_more.setVisibility(View.VISIBLE);
                    layout_add.setVisibility(View.VISIBLE);
                    CommonUtil.keyboardControl(ChatActivity.this,false,edit_msg);
                } else {
                    layout_more.setVisibility(View.GONE);
                }
            }
        });

        btn_chat_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_msg.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
                btn_chat_voice.setVisibility(View.GONE);
                btn_chat_keyboard.setVisibility(View.VISIBLE);
                btn_speak.setVisibility(View.VISIBLE);
                CommonUtil.keyboardControl(ChatActivity.this,false,edit_msg);
            }
        });

        btn_chat_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditState();
            }
        });

        btn_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        tv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVideoMessage();
            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRemoteImageMessage();
            }
        });

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocationMessage();
            }
        });
    }

    /**
     * 显示编辑状态 显示输入法
     * @return void
     */
    private void showEditState() {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_msg.requestFocus();
        layout_more.setVisibility(View.GONE);
        showSoftInputView();
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage(){
        String text=edit_msg.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            showAppToast("请输入内容");
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String,Object> map =new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        conversation.sendMessage(msg, listener);
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage(){
        BmobIMImageMessage image =new BmobIMImageMessage();
        image.setRemoteUrl("http://img.lakalaec.com/ad/57ab6dc2-43f2-4087-81e2-b5ab5681642d.jpg");
        conversation.sendMessage(image, listener);
    }

    /**
     * 发送本地图片地址
     */
    public void sendLocalImageMessage(){
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image =new BmobIMImageMessage("/storage/emulated/0/bimagechooser/IMG_20160302_172003.jpg");
        conversation.sendMessage(image, listener);
//        //因此也可以使用BmobIMFileMessage来发送文件消息
//        BmobIMFileMessage file =new BmobIMFileMessage("文件地址");
//        c.sendMessage(file,listener);
    }

    /**
     * 发送语音消息
     * @Title: sendVoiceMessage
     * @param  local
     * @param  length
     * @return void
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio =new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
        Map<String,Object> map =new HashMap<>();
        map.put("from", "优酷");
        audio.setExtraMap(map);
        //设置语音文件时长：可选
//        audio.setDuration(length);
        conversation.sendMessage(audio, listener);
    }

    /**
     * 发送视频文件
     */
    private void sendVideoMessage(){
        BmobIMVideoMessage video =new BmobIMVideoMessage("/storage/sdcard0/bimagechooser/11.png");
        conversation.sendMessage(video, listener);
    }

    /**
     * 发送地理位置
     */
    public void sendLocationMessage(){
        //测试数据，真实数据需要从地图SDK中获取
        BmobIMLocationMessage location =new BmobIMLocationMessage("广州番禺区",23.5,112.0);
        Map<String,Object> map =new HashMap<>();
        map.put("from", "百度地图");
        location.setExtraMap(map);
        conversation.sendMessage(location, listener);
    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Logger.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addOne(msg);
            edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                showAppToast(e.getMessage());
            }
        }
    };

    /**首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg){
        conversation.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addData(0,list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    showAppToast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Logger.i("聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i=0;i<list.size();i++){
            addMessage2Chat(list.get(i));
        }
    }

    /**添加消息到聊天界面中
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(conversation!=null && event!=null && conversation.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()){//并且不为暂态消息
            if(adapter.findPosition(msg)<0){//如果未添加到界面中
                adapter.addOne(msg);
                //更新该会话下面的已读状态
                conversation.updateReceiveStatus(msg);
            }
            scrollToBottom();
        }else{
            Logger.i("不是与当前聊天对象的消息");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_more.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if(recordManager!=null){
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if(conversation!=null){
            conversation.updateLocalCache();
        }
        CommonUtil.keyboardControl(this,false,edit_msg);
        super.onDestroy();
    }

    /**
     * 长按说话
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!FileUtils.checkSDcard()) {
                        showAppToast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(conversation.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            Logger.i("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(conversation.getConversationId()),recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

}
