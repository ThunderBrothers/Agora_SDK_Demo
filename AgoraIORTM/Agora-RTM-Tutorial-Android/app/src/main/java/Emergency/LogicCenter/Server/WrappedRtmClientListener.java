package Emergency.LogicCenter.Server;

import java.util.Map;

import Emergency.LogicCenter.Config.MessagesConfig;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;

/**
 * 封装后的消息
 * 主要修改了对比原始RTM的onMessageReceived接口，封装为type + msg的参数
 */
public interface WrappedRtmClientListener {

    void onConnectionStateChanged(int var1, int var2);

    void onMessageReceived(MessagesConfig.EmergencyMessage msg, String peerid);

    void onImageMessageReceivedFromPeer(RtmImageMessage var1, String var2);

    void onFileMessageReceivedFromPeer(RtmFileMessage var1, String var2);

    void onMediaUploadingProgress(RtmMediaOperationProgress var1, long var2);

    void onMediaDownloadingProgress(RtmMediaOperationProgress var1, long var2);

    void onTokenExpired();

    void onPeersOnlineStatusChanged(Map<String, Integer> var1);
}
