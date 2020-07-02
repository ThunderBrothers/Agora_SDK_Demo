package Emergency.LogicCenter.Server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Emergency.LogicCenter.Config.MessagesConfig;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtmtutorial.R;

public class MessageCenter {

    private static final String TAG = MessageCenter.class.getSimpleName();
    private Context mContext;
    /**
     * 是否建立频道通信
     */
    private boolean isChannel = true;
    /**
     * RtmClient实例
     */
    private RtmClient mRtmClient;
    /**
     * 频道实例
     */
    private RtmChannel mRtmChannel;

    /**
     * 注册的监听器
     */
    private List<WrappedRtmClientListener> mListenerList = new ArrayList<>();

    private List<WrappedRtmChannelListener> mChannelListenerList = new ArrayList<>();

    /**
     * 收到的消息，peerid + List<消息>
     */
    private Map<String, List<RtmMessage>> mMessageMap = new HashMap<>();

    public MessageCenter(Context context) {
        mContext = context;
    }

    public void init() {
        Log.e("######", "MessageCenter Init");
        String appID = mContext.getString(R.string.agora_app_id);
        try {
            mRtmClient = RtmClient.createInstance(mContext, appID, new RtmClientListener() {

                @Override
                public void onConnectionStateChanged(int state, int reason) {
                    //执行消息
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onConnectionStateChanged(state, reason);
                    }
                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
                    //保存原始消息
                    boolean contains = mMessageMap.containsKey(peerId);
                    List<RtmMessage> list = contains ? mMessageMap.get(peerId) : new ArrayList<>();
                    if (list != null) {
                        list.add(rtmMessage);
                    }
                    if (contains) {
                        mMessageMap.put(peerId, list);
                    }
                    Toast.makeText(mContext, rtmMessage.getText(), Toast.LENGTH_SHORT).show();
                    Log.e("#########", rtmMessage.getText());
                    //封装后的消息
                    //RtmMessage转化成MessagesConfig.EmergencyMessage
                    Gson gson = new Gson();
                    MessagesConfig.EmergencyMessage emergencyMessage = gson.fromJson(rtmMessage.getText(), MessagesConfig.EmergencyMessage.class);
                    //执行消息
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onMessageReceived(emergencyMessage, peerId);
                        Log.e("##################", emergencyMessage.toString());
                    }
                }

                @Override
                public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String peerId) {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onImageMessageReceivedFromPeer(rtmImageMessage, peerId);
                    }
                }

                @Override
                public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String peerId) {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onFileMessageReceivedFromPeer(rtmFileMessage, peerId);
                    }
                }

                @Override
                public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onMediaUploadingProgress(rtmMediaOperationProgress, l);
                    }
                }

                @Override
                public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onMediaDownloadingProgress(rtmMediaOperationProgress, l);
                    }
                }

                @Override
                public void onTokenExpired() {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onTokenExpired();
                    }
                }

                @Override
                public void onPeersOnlineStatusChanged(Map<String, Integer> status) {
                    for (WrappedRtmClientListener listener : mListenerList) {
                        listener.onPeersOnlineStatusChanged(status);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        createAndJoinChannel();
    }

    /**
     * 发送消息 Type + Message
     */
    public void SendMessage(MessagesConfig.EmergencyMessage emergencyMessage) {
        Log.e("######", "SendChannelMessage =" + emergencyMessage.toString() + "   isChannel = " + isChannel);

        if (isChannel) {

            Log.e("######", "mRtmChannel  =" + mRtmChannel);
            if (mRtmChannel != null) {
                // MessagesConfig.EmergencyMessage的内容转化为RtmMessage的Text
                Gson gson = new Gson();
                String content = gson.toJson(emergencyMessage);
                RtmMessage message = mRtmClient.createMessage();
                message.setText(content);
                Log.e("######", "mRtmClient.createMessage() =" + message.toString());
                mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("######", "mRtmClient.sendMessage() onSuccess=");
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        Log.e("######", "mRtmClient.sendMessage() onSuccess =");
                    }
                });
                Log.e("######", "SendChannelMessage =" + content);
            }
        } else {
            if (mRtmClient != null) {
                // MessagesConfig.EmergencyMessage的内容转化为RtmMessage的Text
                Gson gson = new Gson();
                String content = gson.toJson(emergencyMessage);
                RtmMessage message = mRtmClient.createMessage();
                message.setText(content);


                Log.e("######", "SendChannelMessage =" + content);
            }
        }

    }

    public RtmClient getRtmClient() {

        return mRtmClient;
    }

    public void registerListener(WrappedRtmClientListener listener) {
        mListenerList.add(listener);
    }

    public void unregisterListener(WrappedRtmClientListener listener) {
        mListenerList.remove(listener);
    }

    private void getChannelMemberList() {
        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(final List<RtmChannelMember> responseInfo) {
                Log.e("######", String.valueOf(responseInfo.size()));
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e("######", "failed to get channel members, err: " + errorInfo.getErrorCode());
            }
        });
    }

    private void createAndJoinChannel() {
        Log.e("######", "createAndJoinChannel");
        //频道消息
        try {
            Log.e("######", "createChannel");
            mRtmChannel = mRtmClient.createChannel(MessagesConfig.channelID, new RtmChannelListener() {

                @Override
                public void onMemberCountUpdated(int i) {

                }

                @Override
                public void onAttributesUpdated(List<RtmChannelAttribute> list) {

                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
                    //执行消息
                    Log.e("######", "Channel emergencyMessage" + rtmMessage.getText() + "mChannelListenerList.size =" + mChannelListenerList.size());
                    for (WrappedRtmChannelListener listener : mChannelListenerList) {
                        Gson gson = new Gson();
                        MessagesConfig.EmergencyMessage emergencyMessage = gson.fromJson(rtmMessage.getText(), MessagesConfig.EmergencyMessage.class);
                        Log.e("######", "listener.onMessageReceived" + emergencyMessage.content);
                        listener.onMessageReceived(emergencyMessage, rtmChannelMember);
                    }
                }

                @Override
                public void onImageMessageReceived(RtmImageMessage rtmImageMessage, RtmChannelMember rtmChannelMember) {

                }

                @Override
                public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

                }

                @Override
                public void onMemberJoined(RtmChannelMember rtmChannelMember) {

                }

                @Override
                public void onMemberLeft(RtmChannelMember rtmChannelMember) {

                }
            });

            if (mRtmChannel == null) {
                Log.e("######", "Join channel failed");
                return;
            }

            mRtmChannel.join(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void responseInfo) {
                    Log.i("######", "join channel success");
                    getChannelMemberList();
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.e("######", "join channel failed");
                }

                ;
            });
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
