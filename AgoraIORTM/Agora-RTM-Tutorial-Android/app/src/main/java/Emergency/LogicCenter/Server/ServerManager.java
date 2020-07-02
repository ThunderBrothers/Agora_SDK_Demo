package Emergency.LogicCenter.Server;

import android.content.Context;
import android.util.Log;

/**
 * 应急响应的服务器管理器
 * 链接服务器
 */
//public class ServerManager extends Application {
public class ServerManager {

    private static ServerManager Instance;
    private Context context;
    public static MessageCenter messageCenter;

    public static ServerManager GetInstance() {
        if (Instance == null) {
            Instance = new ServerManager();
        }
        return Instance;
    }

    //@Override
    public static void onCreate() {
        //super.onCreate();
        //messageCenter = new MessageCenter(this);
        //messageCenter.init();
    }

    public void init(Context context) {
        Log.e("######", "ServerManager Init");
        this.context = context;
        Instance = this;
    }

    public void initMessageCenter() {
        messageCenter = new MessageCenter(context);
        messageCenter.init();
    }
}
