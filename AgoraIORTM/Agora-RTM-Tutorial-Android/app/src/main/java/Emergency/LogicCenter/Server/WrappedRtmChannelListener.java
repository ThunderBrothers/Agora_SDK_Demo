package Emergency.LogicCenter.Server;


import java.util.List;

import Emergency.LogicCenter.Config.MessagesConfig;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMessage;


/**
 * 封装后的频道消息
 * 主要修改了对比原始RTM的onMessageReceived接口，封装为type + msg的参数
 */
public interface WrappedRtmChannelListener{
    void onMemberCountUpdated(int var1);

    void onAttributesUpdated(List<RtmChannelAttribute> var1);

    void onMessageReceived(MessagesConfig.EmergencyMessage msg, RtmChannelMember var2);

    void onImageMessageReceived(RtmImageMessage var1, RtmChannelMember var2);

    void onFileMessageReceived(RtmFileMessage var1, RtmChannelMember var2);

    void onMemberJoined(RtmChannelMember var1);

    void onMemberLeft(RtmChannelMember var1);
}
