package Emergency.MessageCenter.Config;

//应急响应消息通用配置
//使用声网SDK进行消息收发
//一条消息内容为String类型，由Type + content构成，type用于区分及分类消息用途，content消息内容
public class MessagesConfig {

    //演示Demo配置信息***********************************************************
    public final Staff supervisor = new Staff("徐格","13757163621",StaffType.Supervisor,"莲都运维班值长,华为P20");
    public final Staff regularWatchman = new Staff("王炳昱","13429611911",StaffType.RegularWatchman,"莲都运维班正值,华为P20pro");
    public final Staff deputyWatchman = new Staff("崔亚博","18814800218",StaffType.DeputyWatchman,"莲都运维班副值,华为P20");

    //SDK消息频道名
    public final String channelID = "EmergencyRoomID";
    public final boolean offlineMessage = true;



    //消息类型******************************************************************
    public final String MSG_TYPE_RESPONSE = "Rresponse";//开启响应，pad和phone都可以启动响应， 如A启动了响应 type = MSG_TYPE_RESPONSE , content = MSG_CONTENT_START+MSG_CONTENT_SELECTDUTYOFFICER+A  消息为type = "Rresponse" content = "Start&A"
    public final String MSG_TYPE_EMERGENCY = "Emergency";//发布事故，pad和phone都可以发布事故，

    public final String MSG_TYPE_TASK = "Task";//任务类型
    public final String MSG_TYPE_TASKDETAILS = "Task details";//任务详情类型

    public final String MSG_TYPE_CHAT = "Chat";//聊天类型





    //通用消息******************************************************************
    public final String MSG_CONTENT_START = "Start";//启动或开始
    public final String MSG_CONTENT_CANCEL ="Cancel";//取消或停止


    public final String MSG_CONTENT_TASKSEPARATOR = "@";//任务分隔符
    public final String MSG_CONTENT_TASKRECEPTION = "Task reception";//接收任务，如A人员接收了任务   type = MSG_TYPE_TASK，content = MSG_CONTENT_TASKRECEPTION + MSG_CONTENT_TASKSEPARATOR + A 消息为type = "Task" content = "Task reception@A"

    //值长端发送的消息
    public final String MSG_CONTENT_SELECTDUTYOFFICER = "&";//启动响应时选择的人员id间的分隔符，方便切割字符串以获取收到者区分是否需要处理  如 type = MSG_TYPE_EMERGENCYRESPONSE , ccontent = A&B 代表值长启动响应 选择了A和B去执行


    //值班人员发送的消息




    //数据结构 ******************************************************************

    /// <summary>
    /// 客户端类型,值长端和其他值班人员端
    /// </summary>
    public enum ClientType
    {
        leaderClinet,//值长的Pad
        WatchmanClinet
    }

    /// <summary>
    /// 值班人员类型,值长,正值,副值,检修,保安,消防,驾驶员
    /// </summary>
    public enum StaffType
    {
        Supervisor,//值长
        RegularWatchman,//正值
        DeputyWatchman,//副值
        Repairer,//检修
        SecurityStaff,//保安
        Fireman,//消防
        Driver//驾驶员
    }

    /// <summary>
    /// 值班人员信息
    /// </summary>
    public class Staff{
        String name;
        String telephone;
        StaffType staffType;
        String notes;
        public Staff(String _name,String _telephone,StaffType _staffType,String _notes){
            name =_name;
            telephone = _telephone;
            staffType = _staffType;
            notes = _notes;
        }
    }

    /// <summary>
    /// 所有题目的基类
    /// </summary>
    public class BaseProblem{
        public boolean necessary;//是否为必须选择的题目
    }

    //一个勾选的Toggle，作为基础的题目类型
    public class Toggle{
        public String question;
        public boolean select;
    }

    //一个填空类型作为基础的题目类型
    public class Dictation{
        public String title;
        public String select;
    }

    /// <summary>
    /// 任务
    /// </summary>
    public  class TaskDetail{
        public String firstTitle;
        public String secondTitle;
        public String taskType;//任务类型，共8种
        public String detail;//任务详情的json，分别根据taskType来对应不同的json格式解析成对应任务详情，题目的基类为BaseProblem
    }

    /// <summary>
    /// type = 0
    /// 单选题，
    /// </summary>
    public class SingleChoice extends BaseProblem{
        public String questionTitle;
        public Toggle[] choices;
    }

    /// <summary>
    /// type = 1
    /// 异常类题目，
    /// </summary>
    public class Error extends BaseProblem{
        public String questionTitle;
        public Toggle[] choices;
        public ErrorInfo[] errorInfos;
    }

    public class ErrorInfo{
        public int type;//0图片，1视频，2文字，3语音
        public String info;
    }

    /// <summary>
    /// type = 2
    /// 勾选题，可不勾选
    /// </summary>
    public class Tick extends BaseProblem{
        public Toggle[] ticks;
    }

    /// <summary>
    /// type = 3
    /// 填空题，
    /// </summary>
    public class SpotDictation extends BaseProblem{
        public String questionTitle;
        public Dictation[] choices;
    }

    /// <summary>
    /// type = 4
    /// 多选题，多个Toggle可勾选多个
    /// </summary>
    public class MultipleChoice extends BaseProblem{
        public String questionTitle;
        public Toggle[] choices;
    }

    /// <summary>
    /// type = 5
    /// 混合题，填写后再选择类型
    /// </summary>
    public class BlendChoiceForDictation extends BaseProblem{
        public String questionTitle;
        public String value;
        public Toggle[] choices;
    }

    /// <summary>
    /// type = 6
    /// 时间题，
    /// </summary>
    public class TimeSelect extends BaseProblem{
        public String questionTitle;
    }
}
