package com.qwb.utils;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.step.model.XiaJi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class Constans {
//    public static String ROOT = "http://192.168.0.111:8080/";
//    public static String ROOT = "http://47.107.141.109:8080/";
//    public static String ROOT = "https://t3.cyecy.cn:8888/";
    public static String ROOT = "http://t3.cyecy.cn:8888/";


    //TODO 正式-测试修改：ISDEBUG，ROOT，AppDownUrl
    public static String AppDownUrl = "https://www.wwwpgyer.com/QAOX";//蒲公英的下载地址
    public static boolean ISDEBUG = true;//是否是调试阶段

//    public static String AppDownUrl = "https://www.pgyer.com/05W5";//正式蒲公英的下载地址
//    public static boolean ISDEBUG = false;//是否是调试阶段


    public static boolean ISPASSWORD = false;//初始化密码12345是否可以使用

    public static String H5_BASE_URL = ROOT + "h5/?";//h5域名

    /**
     * 本应用的文件图片都放到这个路径
     */
    public static String APPDIR = Environment.getExternalStorageDirectory() + "/qwb/";
    public static String DIR_IMAGE = APPDIR + "images/";
    /**
     * 缓存图片文件夹
     */
    public static String DIR_IMAGE_CACHE = DIR_IMAGE + "cache/";
    /**
     * 用户保存的图片
     */
    public static String DIR_IMAGE_SAVE = DIR_IMAGE + "save/";
    public static String DIR_VOICE = APPDIR + "voice/";
    public static String DIR_VOICE_KQ = APPDIR + "voice/kq";
    public static String DIR_FILES = APPDIR + "cachefiles/";
    public static String APKPATH = DIR_FILES + "app/clouldLife.apk";


    // res 未读消息 刷新广播
    public static String UnRreadMsg = "com.xmsx.mitu.unreadmsg";
    // action改变悬浮按钮颜色
    public static String Action_menucolor = "com.xmsx.clfile.menucolor";
    // action开始下载apk
    public static String Action_downnow = "com.xmsx.clfile.downnow";
    // 区别哪个板块对应显示红点
    public static String whitchbankuai = "com.xmsx.mitu.bankuai";
    public static String upParentTime = "com.xmsx.mitu.upparenttime";
    public static String Action_login = "com.xmsx.cnlife.login";
    public static String ACTION_upgroupinfor = "com.sxkj.cloud.upinfor";
    public static String chatMsg = "com.sxkj.cloud.chat";

    @Override
    public String toString() {
        return super.toString();
    }

    // 通知通讯录页面请求公司信息
    public static String Action_getcompanyInfo = "com.sxkj.cloud.getcompany";
    // 版本跟新通知
    public static String Action_versonup = "com.sxkj.cloud.versonup";

    /**
     * 去图片时 区分图片的用处 1 获取单张图片 图片地址存放在 headUrl 2 获取一组照片 存放在 publish_pics 限制为6张 3
     * 获取一组照片 存放在 publish_pics 无限制图片数量
     */
    public static int pic_tag;
    public static List<String> current = new ArrayList<String>();// 相册获得到的容器
    public static List<String> publish_pics = new ArrayList<String>();
    public static List<String> publish_pics_xj = new ArrayList<String>();//拍照图片的集合
    // 聊天页面调用相机requestcode（RC）
    public static int TAKE_PIC_XJ = 104;
    // 聊天页面调用相册requestcode（RC）
    public static int TAKE_PIC_XC = 105;

    // 发布帖子的resultcode 和requestcode
    public static int RESULTCODE_publish = 106;
    public static int REQUESTCODE_publish = 107;

    // 创建群组的resultcode 和requestcode
    public static int RESULTCODE_creat_group = 108;
    public static int REQUESTCODE_creat_group = 109;
    // 修改圈信息
    public static int REQUESTCODE_updata_name = 110;
    public static int RESULTCODE_updata_name = 111;

    // 添加成员的resultcode
    public static int RES_addmember = 114;
    public static int RES_addmember_creatgroup = 116;
    // 修改圈信息
    public static int RS_updata_name = 115;
    // 添加责任人
    public static int RS_chose_name = 116;
    // 删除帖子的code
    public static int delResultCode = 118;
    // 创建圈是添加管理员
    public static int creatgroup_manager = 119;
    // 裁剪图片
    public static int RQ_cut_image = 120;
    // 聊天页面发送位置
    public static int TAKE_PIC_WZ = 121;
    public static int TAKE_PIC_YUNPAN = 122;
    public static String headUrl = "";

    public static boolean ISNEW = true;//是否新版本
    public static boolean ISRZ = true;//是否需要认证

    public static int dbVesion = 9;//数据库版本号
    //	public static  String AppDownUrl = ROOTHOST + "wapdl";//市场发布的下载地址
    //	public static  String AppDownUrl = "http://www.qweib.com:8081/cnlife/upload/app/cnlife/CnlifeApp.apk";//正式蒲公英的下载地址

    public static String ROOTHOST = ROOT + "/web/";// 接口请求地址
    public static String IMGROOTHOST = ROOT + "/upload/";// 小林图片地址
    //	http://beta.qweib.com/upload/
    public static String ROOT_imgUrl = "http://oflldcc2r.bkt.clouddn.com/";//云盘的图片url
    public static String ZhiNanUrl = "http://v1.rabbitpre.com/m/BAIvybqiK#0-tsina-1-96489-397232819ff9a47a7b7e80a40613cfe1";//用户指南

    // 注册默认验证码
    public static String SYSCODE = "6012888";
    // 讯飞语音appid
    public static String APPID_VOICE = "57ae9efb";

    // OA地址
    public static String OAROOT = "http://222.76.252.244/";
    public static String OAROOTHOST = OAROOT + "mobile/";
    public static String WEB_URL = ROOTHOST + "pageQuestionnaire?token=%1$s";
    public static String WEB_URL_dianpu = ROOTHOST + "dczxct?token=%1$s";
    public static String WEB_URL_dingcan = ROOTHOST + "dcwddc?token=%1$s";
    public static String WEB_URL_yqq = ROOTHOST + "yqyqq?token=%1$s";
    public static String WEB_URL_qyfw = ROOTHOST + "yqindex?token=%1$s";
    public static String WEB_URL_gonggao = ROOTHOST + "noticedetailpage?token=%1$s&noticeId=%2$s&tp=%3$s";
    public static String WEB_URL_goukaixin = ROOTHOST + "ghsdetails?noticeId=%1$s";


    // 员工圈列表
    public static String allGroupURL = ROOTHOST + "allGroup";
    // 创建群组
    public static String addGroupURL = ROOTHOST + "addGroup";
    // 圈帖子列表
    public static String topicListURL = ROOTHOST + "topicList";
    // 圈组信息
    public static String groupDetailURL = ROOTHOST + "groupDetail";
    // 发帖子
    public static String addTopicURL = ROOTHOST + "addTopic";
    // 赞帖子
    public static String addPraiseURL = ROOTHOST + "operPraise";
    // 帖子发表评论
    public static String addCommentURL = ROOTHOST + "addComment";
    // 帖子发表评论
    public static String addBfcomment = ROOTHOST + "addBfcomment";
    // 个人主页
    public static String tophomepageURL = ROOTHOST + "tophomepage";
    // 个人主页的个人资料
    public static String memInfoURL = ROOTHOST + "memInfo";
    // 个人主页的个人资料的背景F
    public static String updateBgURL = ROOTHOST + "updateBg";
    // 个人资料
    public static String userinfoURL = ROOTHOST + "userinfo";
    // 修改圈头像
    public static String updateGroupHeadURL = ROOTHOST + "updateGroupHead";
    // 圈成员列表
    public static String memListURL = ROOTHOST + "memList";
    // 添加成员
    public static String addGroupMemURL = ROOTHOST + "addGroupMem";
    // 发送邀请入圈申请
    public static String askAddGroupURL = ROOTHOST + "askAddGroup";
    // 签到申请
    public static String addCheckInURL = ROOTHOST + "addCheckin";
    // 够划算商品列表
    public static String queryGroupGoodssj = ROOTHOST + "querygroupgoodssj";
    // 够划算商品详情
    public static String groupGoodssjxq = ROOTHOST + "groupgoodssjxq";
    // 圈成员
    public static String groupMemberListURL = ROOTHOST + "groupMemberList";
    // 移除圈成员
    public static String removeGroupURL = ROOTHOST + "removeGroup";
    // 退出圈
    public static String outGroupURL = ROOTHOST + "outGroup";
    // 修改圈信息
    public static String updateGroupURL = ROOTHOST + "updateGroup";
    // 圈消息置顶
    public static String updateGroupTopURL = ROOTHOST + "updateGroupTop";
    // 圈免打扰模式
    public static String updateGroupRemindURL = ROOTHOST + "updateGroupRemind";
    // 移除黑名单
    public static String updateBindtpURL = ROOTHOST + "updateBindtp";
    // 修改头像
    public static String updateHeadURL = ROOTHOST + "updateHead";
    // 跟新个人信息
    public static String updateinfoURL = ROOTHOST + "updateinfo";
    // 注册
    public static String updateShieldURL = ROOTHOST + "updateShield";
    // 添加好友
    public static String addMemberAttentionURL = ROOTHOST + "addMemberAttention";
    // 添加好友
    public static String deleteMyMemberURL = ROOTHOST + "deleteMyMember";
    // 邀请同事
    public static String yqworkmateURL = ROOTHOST + "yqworkmate";
    // 被邀请人信息
    public static String inviterDetailsURL = ROOTHOST + "inviterDetails";


    // 组长设置常用
    public static String setcyURL = ROOTHOST + "setcy";
    // 申请加入圈
    public static String applyJoinURL = ROOTHOST + "applyJoin";
    // 签到明细
    public static String checkinDetailsURL = ROOTHOST + "checkinDetails";
    // 签到列表
    public static String checkinListURL = ROOTHOST + "checkinList";
    // 新版本签到列表
    public static String newCheckinlist2 = ROOTHOST + "newCheckinlist2";
    public static String newCheckinlist3 = ROOTHOST + "newCheckinlist3";
    // 更改手机号码
    public static String changeMobileURL = ROOTHOST + "changeMobile";
    // 历史聊天记录
    public static String getMsgURL = ROOTHOST + "getMsg";
    // 历史聊天记录
    public static String getOldMsgURL = ROOTHOST + "getOldMsg";
    // 发送好申请
    public static String applyFriendURL = ROOTHOST + "applyFriend";
    // 该用户当前是否屏蔽设置
    public static String botherStateURL = ROOTHOST + "botherState";
    // 是否屏蔽设置
    public static String newsNotBotherURL = ROOTHOST + "newsNotBother";
    // 单个主题详情
    public static String topicDetailURL = ROOTHOST + "topicDetail";
    // 删除单个主题详情
    public static String delTopicURL = ROOTHOST + "delTopic";
    // 知识库
    public static String sortListURL = ROOTHOST + "sortList";
    // 添加知识库分类
    public static String addSortURL = ROOTHOST + "addSort";
    // 修改知识库类名称
    public static String updateSortURL = ROOTHOST + "updateSort";
    // 删除知识库类名称
    public static String delSortURL = ROOTHOST + "delSort";
    // 知识库列表类名称
    public static String knowledgePageURL = ROOTHOST + "knowledgePage";
    // 分享到知识库
    public static String setKnowledgeURL = ROOTHOST + "setKnowledge";
    // 知识库知识列表
    public static String knowledgeListURL = ROOTHOST + "knowledgeList";
    // 单个知识点详情
    public static String knowledgeDetailURL = ROOTHOST + "knowledgeDetail";
    // 单个知识点删除
    public static String delKnowledgeURL = ROOTHOST + "delKnowledge";
    // 添加外部知识点
    public static String addKnowledgeURL = ROOTHOST + "addKnowledge";
    // 删除部门
    public static String delDeptURL = ROOTHOST + "delDept";
    // 修改部门名称
    public static String updateDeptNmURL = ROOTHOST + "updateDeptNm";
    // 够开心
    public static String ghslistsURL = ROOTHOST + "ghslists";
    // 够开心
    public static String delTaskURL = ROOTHOST + "delTask";
    // 意见反馈
    public static String addFeedbackURL = ROOTHOST + "addFeedback";
    // 获取认证手机号验证码
    public static String getCodeRzMobile = ROOTHOST + "member/certify/code";
    // 认证手机号
    public static String rzMobile = ROOTHOST + "member/certify";
    // 获取验证码
    public static String detailTaskFeedURL = ROOTHOST + "detailTaskFeed";
    // 添加艾特成员列表
    public static String aitaMemPageURL = ROOTHOST + "aitaMemPage";
    // 知识库文件列表
    public static String knowdgeFileURL = ROOTHOST + "knowdgeFile";
    // 唯独消息模块切换
    public static String updateMsgmodelURL = ROOTHOST + "updateMsgmodel";

    /**
     * TODO --添加
     */
    public static Boolean isDelModel = false;// 图片是否处于删除状态
    public static Boolean isDelModel2 = false;// 图片是否处于删除状态
    public static Boolean isDelModel3 = false;// 图片是否处于删除状态
    public static int pic_type;// 用来区分同一个界面有多个“拍照和相册”的功能
    public static List<String> linDownloader = new ArrayList<String>();//临时下载的图片
    public static ArrayList<String> publish_pics1111 = new ArrayList<>();// 拍照和相册获得的图片容器
    public static ArrayList<String> publish_pics1 = new ArrayList<>();// 拍照和相册获得的图片容器
    public static List<String> current2 = new ArrayList<String>();// 相册获得到的图片容器
    public static ArrayList<String> publish_pics2222 = new ArrayList<String>();// 拍照和相册获得的图片容器
    public static ArrayList<String> publish_pics2 = new ArrayList<String>();// 拍照和相册获得的图片容器
    public static List<String> current3 = new ArrayList<String>();// 相册获得到的图片容器
    public static ArrayList<String> publish_pics3333 = new ArrayList<String>();// 拍照和相册获得的图片容器
    public static ArrayList<String> publish_pics3 = new ArrayList<String>();// 拍照和相册获得的图片容器
    public static HashMap<String, String> picmap = new HashMap<String, String>();//下载模板图片用hash容器存储
    public static HashMap<String, String> picmap2 = new HashMap<String, String>();//下载模板图片用hash容器存储
    public static HashMap<String, String> picmap3 = new HashMap<String, String>();//下载模板图片用hash容器存储
    //组织架构--旧的
    public static HashMap<Integer, MemberBean> memberMap = new HashMap<Integer, MemberBean>();//成员ID组
    public static HashMap<Integer, BranchBean> branchMap = new HashMap<Integer, BranchBean>();//部门ID组
    public static HashMap<Integer, Boolean> ziTrueMap = new HashMap<Integer, Boolean>();//拜访流程（销售小结和订货下单）--保存商品是否选中
    public static HashMap<Integer, Boolean> ParentTrueMap = new HashMap<Integer, Boolean>();//轨迹地图--部门是否选中
    public static HashMap<Integer, Integer> ParentTrueMap2 = new HashMap<Integer, Integer>();//轨迹地图--部门：0没选中，1全选中，2：部分选中
    //组织架构--新的
    public static HashMap<Integer, MemberBean> memberMap_zuzhi = new HashMap<Integer, MemberBean>();//成员ID组
    public static HashMap<Integer, BranchBean> branchMap_zuzhi = new HashMap<Integer, BranchBean>();//部门ID组
    public static HashMap<Integer, Boolean> ziTrueMap_zuzhi = new HashMap<Integer, Boolean>();//拜访流程（销售小结和订货下单）--保存商品是否选中
    public static HashMap<Integer, Integer> ParentTrueMap_zuzhi = new HashMap<Integer, Integer>();//轨迹地图--部门：0没选中，1全选中，2：部分选中

    public static HashMap<Integer, XiaJi> xiajiMap = new HashMap<Integer, XiaJi>();//拜访流程（销售小结和订货下单）--保存所有的商品
    public static HashMap<String, String> zjMap = new HashMap<String, String>();//拜访流程（销售小结和订货下单）--保存所有商品的金额
    public static List<XiaJi> XiajiList = new ArrayList<XiaJi>();//拜访流程（销售小结和订货下单）--保存选中的商品
    public static ArrayList<HashMap<String, String>> xsList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> goodsCacheList = new ArrayList<HashMap<String, String>>();
    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    public static int gatherInterval = 60;//默认60s
    public static int packInterval = 60;
    public static String isTrackStart = "track_isBegin";//是否开始上班签到

    public static String[] latlng = null;//客户管理—分布图--坐标数组
    public static String[] lat = null;//客户管理—分布图--坐标数组
    public static String[] lng = null;//客户管理—分布图--坐标数组
    public static String[] name = null;//客户管理—分布图--名称数组
    public static String[] address = null;//客户管理—分布图--地址数组

    //--------------------登录，注册------------------------------------
    public static String regURL = ROOTHOST + "reg";// 注册公司
    public static String regnewURL = ROOTHOST + "regnew";// 注册会员
    public static String loginURL = ROOTHOST + "login";// 登录
    public static String loginURL2 = ROOTHOST + "login/standalone";// 独立服务器登录
    public static String getJwt = ROOTHOST + "/jwt";// 获取jwt
    public static String logout = ROOTHOST + "quit";//退出：参数token
    public static String queryApplyByMemberRole = ROOTHOST + "queryApplyByMemberRole";//获取应用列表接口--角色
    public static String menus = ROOTHOST + "menus";//获取应用列表接口--(新的)-菜单
    public static String changeCompany = ROOTHOST + "changeCompany";//切换公司
    public static String changeCompany2 = ROOTHOST + "change/company";//切换公司(独立版)
    public static String switchCompany = "/web/switch/company";//切换公司(独立版)
    public static String jwt = ROOTHOST + "jwt";//获取jwt
    public static String getCodeURL = ROOTHOST + "getCode";// 获取验证码
    public static String changepwdToURL = ROOTHOST + "changepwdTo";// 忘记密码
    public static String checkCodeURL = ROOTHOST + "checkCode";// 忘记密码-验证验证码
    public static String changepwdURL = ROOTHOST + "changepwd";// 修改密码
    public static String qrcodeScanned = ROOTHOST + "qrcode/scanned";// 已扫描
    public static String qrcodeAccept = ROOTHOST + "qrcode/accept";// 同意授权登录
    public static String qrcodeReject = ROOTHOST + "qrcode/reject";// 取消授权登录

    public static String delCommentURL = ROOTHOST + "delComment"; // 删除评论或回复--发帖
    public static String deleteBfComment = ROOTHOST + "deleteBfComment";// 删除评论或回复--拜访查询
    public static String noticeLists = ROOTHOST + "noticelists"; // 公告
    public static String queryMyMemberURL = ROOTHOST + "queryMyMember"; // 通讯录
    public static String updateIsusedURL = ROOTHOST + "updateIsused";// 将好友设为常用
    public static String queryDepartlsURL = ROOTHOST + "queryDepartls";// 查询所在部门列表
    public static String queryDepartlszOrcyURL = ROOTHOST + "queryDepartlszOrcy";// 获取公司下的部门的子部门的成员
    public static String addDepartURL = ROOTHOST + "addDepart";// 创建部门
    public static String searchURL = ROOTHOST + "search";// 搜索
    public static String unReadURL = ROOTHOST + "unRead"; // 获取未读消息
    public static String unReadURL2 = ROOTHOST + "unRead2";// 获取未读消息--转让客户信息接口
    public static String updateVerSionURL = ROOTHOST + "updateVerSion";// 更新版本


    //-------公司相关-------
    public static String addCompanyURL = ROOTHOST + "addCompany";
    public static String addNewCompany = ROOTHOST + "addNewCompany";// 创建公司
    public static String queryCorporationURL = ROOTHOST + "queryCorporation";// 查询公司信息
    public static String workmateagreeURL = ROOTHOST + "workmateagree";// 同意公司/部门邀请
    public static String delCompanyURL = ROOTHOST + "delCompany";// 删除公司
    public static String updateCompanyNmURL = ROOTHOST + "updateCompanyNm";// 修改公司名
    public static String deptListURL = ROOTHOST + "deptList";//// 获取部门列表
    public static String likeCompanyNmURL = ROOTHOST + "likeCompanyNm";// 搜索公司
    public static String applyInCompanyURL = ROOTHOST + "applyInCompany";// 申请加入公司
    public static String delMemURL = ROOTHOST + "delMem";// 将用户从公司移除
    public static String moveDeptURL = ROOTHOST + "moveDept";// 移动成员到其他部门
    public static String inCompanyAgreeURL = ROOTHOST + "inCompanyAgree";// 同意或不同意加入公司申请
    public static String COMMON_UPLOAD = ROOTHOST + "common/upload";// 公司信息--上传图片
    public static String company_info = ROOTHOST + "company/info";// 公司信息
    public static String company_industry = ROOTHOST + "company/industry";// 所属行业
    public static String company_category = ROOTHOST + "company/category";// 所属行业分类
    public static String company_register_salesman = ROOTHOST + "company/register/salesman";// 平台业务员
    public static String company_save = ROOTHOST + "company/save";// 保存公司信息
    public static String UPDATE_MEMBER = ROOTHOST + "member/initial";//注册：完善信息
    public static String COMPANY_REGISTER_CODE = ROOTHOST + "company/register/code";// 注册公司-获取验证码
    public static String COMPANY_REGISTER = ROOTHOST + "company/register";// 注册公司

    //---------------------------审批模块-------------------------
    public static String addBscAuditZdy = ROOTHOST + "addBscAuditZdy";//添加自定义审批
    public static String queryAuditZdy = ROOTHOST + "queryAuditZdy";//查询自定义审批
    public static String deleteAuditZdy = ROOTHOST + "deleteAuditZdy";//删除自定义审批
    public static String updateAuditZdy = ROOTHOST + "updateAuditZdy";//修改自定义审批
    public static String addAuditURL = ROOTHOST + "addAudit";// 添加审批
    public static String queryAuditDetailURL = ROOTHOST + "queryAuditDetail";// 审批流程详情
    public static String queryMyAuditURL = ROOTHOST + "queryMyAudit";// 我发起的
    public static String queryMyCheckURL = ROOTHOST + "queryMyCheck";// 我审批的
    public static String queryExecAudit = ROOTHOST + "queryExecAudit";// 我执行的
    public static String toCheckURL = ROOTHOST + "toCheck";// 审批操作
    public static String cancelAuditURL = ROOTHOST + "cancelAudit";// 撤销操作
    public static String updateAuditExecStatus = ROOTHOST + "updateAuditExecStatus";// 修改审批执行状态
    public static String queryAuditModelById = ROOTHOST + "queryAuditModelById";//  查询审批模板
    public static String queryAccountList = ROOTHOST + "queryAccountList";//  账户列表
    public static String queryAuditZdyById = ROOTHOST + "queryAuditZdyById";//  根据审批流id查询审批流
    public static String queryAuditModelList = ROOTHOST + "queryAuditModelList";//查询审批模板
    public static String queryAuditZdyListByModelId = ROOTHOST + "queryAuditZdyListByModelId";//根据modelId查询审批流列表


    //客户管理
    public static String nearClient = ROOTHOST + "queryCustomerWebZb";//客户管理（周边客户）
    public static String mineClient = ROOTHOST + "queryCustomerWeb";//客户管理（我的客户）
    public static String deleteCustomerWeb = ROOTHOST + "deleteCustomerWeb";//删除客户
    public static String zrCustomerWeb = ROOTHOST + "zrCustomerWeb";//转让客户
    public static String zrCustomerCzWeb = ROOTHOST + "zrCustomerCzWeb";//同不同意转让客户
    public static String jxsFenlei = ROOTHOST + "queryJxsflls";//新增经销商——经销商分类C
    public static String jxsState = ROOTHOST + "queryJxsztls";//新增经销商——经销商状态
    public static String jxsLevel = ROOTHOST + "queryJxsjbls";//新增经销商——经销商等级
    public static String addProivder = ROOTHOST + "addCustomerWeb1";//新增经销商
    public static String updateCustomerSj1 = ROOTHOST + "updateCustomerSj1";//修改经销商
    public static String addClient = ROOTHOST + "addCustomerWeb2";//新增客户
    public static String updateClient = ROOTHOST + "updateCustomerSj2";//修改客户
    public static String updateCustomerByAddress = ROOTHOST + "updateCustomerByAddress";//修改客户(地址信息)
    public static String qdTpye = ROOTHOST + "queryQdtypls";//新增客户---渠道类型
    public static String Khlevells = ROOTHOST + "queryKhlevells";//新增客户---客户等级
    public static String Xsphasels = ROOTHOST + "queryXsphasels";//新增客户---销售阶段
    public static String Bfpcls = ROOTHOST + "queryBfpcls";//新增客户---拜访频次
    public static String queryHzfsls = ROOTHOST + "queryHzfsls";//新增客户---合作方式
    public static String queryCustomerls1 = ROOTHOST + "queryCustomerls1";//查询供货商---选择供货商
    public static String clientDetail = ROOTHOST + "queryCustomerOneWeb";//客户详情
    public static String queryBfkhsWeb = ROOTHOST + "queryBfkhsWeb";//获取拜访客户信息（判断有没有上传资料）
    public static String queryTolpricexs = ROOTHOST + "queryTolpricexs";//获取上月，本月的销售总额
    public static String queryRegionTree = ROOTHOST + "queryRegionTree";//客户所属区域
    public static String queryProviderPage = ROOTHOST + "queryProviderPage";//分页查询供应商
    public static String queryFinUnitPage = ROOTHOST + "queryFinUnitPage";//分页获取往来单位成功
    public static String queryCustomerMapList = ROOTHOST + "queryCustomerMapList";//客户分布图

    //部门和员工
    public static String queryDepartMemLs = ROOTHOST + "queryDepartMemLsForRole";//获取部门以及成员信息--角色
    public static String queryCompanyMemberList = ROOTHOST + "queryCompanyMemberList";//员工列表

    //---------------------------长连接模块 GPS-------------------------
    public static String ROOT_CHANG = ROOT + "/gps/User/";//正式  http://gps.7weib.com/gps
    public static String postLocation = ROOT + "/gps/User/postLocation";//上传定位数据
    public static String getDailyLocation = ROOT + "/gps/User/getDailyLocation";//轨迹回放-查询指定用户一天定位信息
    public static String getRealtimeLocation = ROOT + "/gps/User/getRealtimeLocation";//查询指定用户实时位置信息

    public static String queryMapGjLsdtClj = ROOTHOST + "queryMapGjLsdtClj";//员工分布图
    public static String queryMapGjLsClj2 = ROOTHOST + "queryMapGjLsClj2";//员工在线--获取员工最新位置列表
    public static String queryBflsmweb = ROOTHOST + "queryBflsmweb";//拜访回放

    //拜访流程
    public static String queryBfqdpz = ROOTHOST + "queryBfqdpzWeb";//拜访流程--1：查询--拜访签到拍照
    public static String addBfqdpz = ROOTHOST + "addBfqdpzWeb";//拜访流程--1：add--拜访签到拍照
    public static String updateBfqdpz = ROOTHOST + "updateBfqdpzWeb";//拜访流程--1：修改--拜访签到拍照
    public static String queryBfsdhjc = ROOTHOST + "queryBfsdhjcWeb";//拜访流程--2：查询--生动化检查（获取信息）
    public static String addBfsdhjc = ROOTHOST + "addBfsdhjcWeb";//拜访流程--2：添加--生动化检查
    public static String updateBfsdhjc = ROOTHOST + "updateBfsdhjcWeb";//拜访流程--2：修改--生动化检查）
    public static String queryCljccjMdls = ROOTHOST + "queryCljccjMdlsWeb";//拜访流程--3：查询--陈列检查采集（获取模板））
    public static String queryBfcljccj = ROOTHOST + "queryBfcljccjWeb";//拜访流程--3：查询--陈列检查采集（获取信息）
    public static String addBfcljccj = ROOTHOST + "addBfcljccjWeb";//拜访流程--3：添加--陈列检查采集（添加）
    public static String updateBfcljccj = ROOTHOST + "updateBfcljccjWeb";//拜访流程--3：陈列检查采集（修改）
    public static String queryBfgzxcWeb = ROOTHOST + "queryBfgzxcWeb";//拜访流程--7：查询--道谢并告知下次拜访（获取信息）
    public static String addBfgzxcWeb = ROOTHOST + "addBfgzxcWeb";//拜访流程--7：道谢并告知下次拜访（添加）
    public static String updateBfgzxcWeb = ROOTHOST + "updateBfgzxcWeb";//拜访流程--7：道谢并告知下次拜访（修改）
    public static String queryBforderWeb = ROOTHOST + "queryBforderWeb";//拜访流程--6：供货下单获取信息
    public static String addBforderWeb = ROOTHOST + "addBforderWeb";//拜访流程--6：供货下单（添加）
    public static String updateBforderWeb = ROOTHOST + "updateBforderWeb";//拜访流程--6：供货下单（修改）
    public static String queryWaretypeLs1 = ROOTHOST + "queryWaretypeLs1";//拜访流程--6：获取商品第一级分类列表
    public static String queryWaretypeLs2 = ROOTHOST + "queryWaretypeLs2";//拜访流程--6：获取商品下级分类列表
    public static String queryBfxsxjlsWeb = ROOTHOST + "queryBfxsxjlsWeb";//拜访流程--5：销售小结（查询）
    public static String addBfxsxjWeb = ROOTHOST + "addBfxsxjWeb";//拜访流程--5：销售小结（添加）
    public static String updateBfxsxjWeb = ROOTHOST + "updateBfxsxjWeb";//拜访流程--5：销售小结（修改）
    public static String queryXstypels = ROOTHOST + "queryXstypels";//拜访流程--5：销售小结---销售类型
    public static String queryWebStkStorageList = ROOTHOST + "queryWebStkStorageList";//出货仓库
    public static String queryKhFlJbls = ROOTHOST + "queryKhFlJbls";//客户管理--客户类型and客户级别集合
    public static String querycmapwebjk = ROOTHOST + "querycmapwebjk";//客户管理--客户分布图
    public static String querycmapweb = ROOTHOST + "querycmapweb";//客户管理--客户分布图


    public static String queryToken = ROOTHOST + "page/token/generate";//获取重复的token


    //订货下单--模块
    public static String queryDhorder = ROOTHOST + "queryDhorder";//订货下单（列表）
    public static String addDhorderWeb = ROOTHOST + "addDhorderWeb";//订货下单（添加）
    public static String queryDhorderWeb = ROOTHOST + "queryDhorderWeb";//订货下单获取信息
    public static String updateDhorderWeb = ROOTHOST + "updateDhorderWeb";//订货下单--修改
    public static String queryThorder = ROOTHOST + "thorder/queryThorder";//退货（列表）
    public static String addThorderWeb = ROOTHOST + "thorder/addThorderWeb";//退货
    public static String queryThorderWeb = ROOTHOST + "thorder/queryDhorderWeb";//退货获取信息
    public static String updateThorderWeb = ROOTHOST + "thorder/updateThorderWeb";//退货（修改）
    public static String cancelOrderAndOutByOrderId = ROOTHOST + "stkout/cancelOrderAndOutByOrderId";//取消订货下单

    //采购单
    public static String dragSaveStkIn = ROOTHOST + "stkIn/dragSaveStkIn";//采购单（暂存）
    public static String stkInHisPage = ROOTHOST + "stkIn/stkInHisPage";//采购单列表
    public static String showstkin = ROOTHOST + "stkIn/showstkin";//采购单（查看单个）

    //拜访查询
    public static String queryBfkhLsWeb = ROOTHOST + "queryBfkhLsWeb";//拜访查询--客户列表
    public static String queryBfkhLsWeb2 = ROOTHOST + "queryBfkhLsWeb2";//拜访查询--客户列表
    public static String queryBfkhLsWeb3 = ROOTHOST + "queryBfkhLsWeb3";//拜访查询--客户列表
    public static String queryBfkhLsWebNew = ROOTHOST + "queryBfkhLsWebNew";//拜访查询--客户列表
    public static String queryBfkhWebTime = ROOTHOST + "queryBfkhWebTime";//拜访查询--获取最新数据的时间
    public static String queryBfkhLsWebOne = ROOTHOST + "queryBfkhLsWebOne";//拜访查询--消息评论
    public static String queryBfkheWeb = ROOTHOST + "queryBfkheWeb";//拜访查询--拜访纪录
    public static String queryBfqdpzPageBymcid = ROOTHOST + "queryBfqdpzPageBymcid";//拜访查询--客户列表

    //统计报表--模块
    public static String queryYwbfzxWeb = ROOTHOST + "queryYwbfzxWeb";//业务拜访执行表
    public static String queryKhbftjWeb = ROOTHOST + "queryKhbftjWeb";//客户拜访统计表
    public static String queryCpddtjWeb = ROOTHOST + "queryCpddtjWeb";//产品订单统计表
    public static String queryXsddtjWeb = ROOTHOST + "queryXsddtjWeb";//销售订单统计表

    //计划拜访--模块
    public static String queryBscPlanWeb = ROOTHOST + "queryBscPlanWeb";//查询我的计划
    public static String queryBscPlanNewWeb = ROOTHOST + "queryBscPlanNewWeb";//查询我的计划(新的)
    public static String queryBscPlanNewUnderlingWeb = ROOTHOST + "queryBscPlanNewUnderlingWeb";//查询下属计划(新的)
    public static String queryBfkheWeb2 = ROOTHOST + "queryBfkheWeb2";//查询下属计划详情
    public static String deleteBscPlanWeb = ROOTHOST + "deleteBscPlanWeb";//删除拜访计划
    public static String addBscPlanWeb = ROOTHOST + "addBscPlanWeb";//添加拜访计划
    public static String addBscPlanNewWeb = ROOTHOST + "addBscPlanNewWeb";//添加拜访计划(新的)
    public static String addBscPlanxlWeb = ROOTHOST + "addBscPlanxlWeb";//添加计划线路
    public static String addBscPlanxlDetailWeb = ROOTHOST + "addBscPlanxlDetailWeb";//添加计划线路客户
    public static String queryBscPlanxlWeb = ROOTHOST + "queryBscPlanxlWeb";//查询计划线路
    public static String deleteBscPlanxlWeb = ROOTHOST + "deleteBscPlanxlWeb";//删除计划线路
    public static String queryBscPlanxlDetailWeb = ROOTHOST + "queryBscPlanxlDetailWeb";//查询计划线路客户
    public static String deleteBscPlanxlDetailWeb = ROOTHOST + "deleteBscPlanxlDetailWeb";//删除计划线路客户
    public static String updateBscPlanxlWeb = ROOTHOST + "updateBscPlanxlWeb";//修改计划线路
    public static String updateBscPlanxlWeb2 = ROOTHOST + "updateBscPlanxlWeb2";//修改计划线路2
    public static String queryNearCustomerListByLatLng = ROOTHOST + "queryNearCustomerListByLatLng";//查询线路的周边客户


    //卖场--模块
    public static String queryMsWareLs = ROOTHOST + "queryMsWareLs";//获取商品信息
    public static String addOrderlsWeb = ROOTHOST + "addOrderlsWeb";//添加订单
    public static String updateOrderlsWeb = ROOTHOST + "updateOrderlsWeb";//修改订单
    public static String queryOrderlsOneWeb = ROOTHOST + "queryOrderlsOneWeb";//获取订单详情信息
    public static String queryOrderlsPage = ROOTHOST + "queryOrderlsPage";//获取订单列表信息
    public static String queryOrderlsOneDhWeb = ROOTHOST + "queryOrderlsOneDhWeb";//获取订单详情信息（订货）
    public static String queryOrderlsBbPage = ROOTHOST + "queryOrderlsBbPage";//获取结算订单列表信息

    //轨迹
    public static String queryXtcsWeb1 = ROOTHOST + "queryXtcsWeb1";//获取轨迹时间间隔
    public static String queryAddressUplaodWeb = ROOTHOST + "queryAddressUplaodWeb";//获取轨迹时间间隔
    public static String updateAddressUpload = ROOTHOST + "updateAddressUpload";//业务员自己修改上传方式

    //云盘
    public static String getToken = ROOTHOST + "getUploadToken";//获取token
    public static String getBucketList = ROOTHOST + "getBucketList";//列举文件条目
    public static String downloadBucke = ROOTHOST + "downloadBucke";//下载链接接口
    public static String deleteBucke = ROOTHOST + "deleteBucke";//删除文件
    public static String addYfileWeb = ROOTHOST + "addYfileWeb";//新建文件夹
    public static String queryYfileWeb = ROOTHOST + "queryYfileWeb";//查询文件
    public static String updatefileNm = ROOTHOST + "updatefileNm";//修改文件名
    public static String moveBucke = ROOTHOST + "moveBucke";//文件重命名
    public static String movefile = ROOTHOST + "movefile";//移动文件
    public static String queryYfilepwd = ROOTHOST + "queryYfilepwd";//获取文件密码
    public static String addYfilepwd = ROOTHOST + "addYfilepwd";//添加文件密码
    public static String updateYfilepwd = ROOTHOST + "updateYfilepwd";//修改文件密码
    public static String getBucketInfo = ROOTHOST + "getBucketInfo";//获取文件信息
    public static int requestcode_movefile = 1000;//移动文件

    //----------------------日志模块--------------------------------------------
    public static String addReport = ROOTHOST + "addReport";//添加日报，周报，月报
    public static String queryreportcdWeb = ROOTHOST + "queryreportcdWeb";//限制日报，周报，月报输入文字的字数
    public static String queryReportWeb = ROOTHOST + "queryReportWeb";//获取报详情
    public static String queryReportWebPage1 = ROOTHOST + "queryReportWebPage1";//获取报（表）
    public static String queryReportWebPage2 = ROOTHOST + "queryReportWebPage2";//获取报(我发出的)
    public static String queryReportWebPage3 = ROOTHOST + "queryReportWebPage3";//获取报(我收到的)
    public static String queryReportPlWebPage = ROOTHOST + "queryReportPlWebPage";//获取报评论列表
    public static String addReportPl = ROOTHOST + "addReportPl";//添加报评论


    //---------------------------考勤模块-------------------------
    public static String addCheckRuleWeb = ROOTHOST + "addCheckRuleWeb";//添加考勤规则
    public static String queryCheckRuleWeb = ROOTHOST + "queryCheckRuleWeb";//查询考勤规则
    public static String queryCheckRuleWebOne = ROOTHOST + "queryCheckRuleWebOne";//获取考勤规则
    public static String queryCheckRuleWebMy = ROOTHOST + "queryCheckRuleWebMy";//获取我的考勤规则说明
    public static String deleteCheckRuleWeb = ROOTHOST + "deleteCheckRuleWeb";//删除考勤规则
    public static String updateCheckRuleWebXq = ROOTHOST + "updateCheckRuleWebXq";//修改考勤规则详情
    public static String updateCheckRuleWebRy = ROOTHOST + "updateCheckRuleWebRy";//修改考勤规则人员权限
    public static String queryCheckTd = ROOTHOST + "queryCheckTd";//获取考勤(团队)数据
    public static String queryCheckMy = ROOTHOST + "queryCheckMy";//获取考勤(我的)数据
    public static String queryBcList = ROOTHOST + "queryBcList";//获得班次列表
    public static String updateBcPosition = ROOTHOST + "updateBcPosition";//修改班次

    //配送--模块
    public static String queryDeliverBillPage = ROOTHOST + "queryDeliverBillPage";//手机端配送单据查询接口（待接收、配送中、已收货、配送终止4个标签页展示）
    public static String updateDeliveryState = ROOTHOST + "updateDeliveryState";//手机端配送单状态更新接口（接收、配送、已完成、终止接口）
    public static String queryDeliveryDetail = ROOTHOST + "queryDeliveryDetail";//配送单详情
    public static String queryDeliverListByNavMap = ROOTHOST + "queryDeliverListByNavMap";//配送单导航地图


    //是否重新开启轨迹服务
    public static String shangbaofanshi = "shangbaofanshi";//上报方式
    //是否重新结束轨迹服务
    public static String khNum = "khNum";//客户数量
    public static String khSearch = "khSearch";//客户搜索

    //首页模块--添加模块（插件）
    public static String isAdd_tongjibaobiao = "isAdd_tongjibaobiao";//是否添加“统计报表”
    public static String isAdd_baifangjihua = "isAdd_baifangjihua";//是否添加“拜访计划”
    public static String isAdd_yunpan = "isAdd_yunpan";//是否添加“云盘”
    public static String isAdd_rizhi = "isAdd_rizhi";//是否添加“日志”

    public static String id = "id";//id
    public static String cid = "clientId";//客户id
    public static String khNm = "khNm";//客户名称
    public static String memberNm = "memberNm";//用户名
    public static String type = "type";//类型（区分不同的类型）
    public static String track = "trackList";//类型（区分不同的类型）
    public static String date = "date";//传具体日期
    public static String callOnRecord = "callOnRecord";//传拜访记录


    //---------------------------张工：选择商品-------------------------
    public static String queryStkWareType = ROOTHOST + "queryStkWareType";//获取公司商品类型(不包括非公司商品)
    public static String queryWareTypeList = ROOTHOST + "queryWareTypeList";//获取所有商品类型（包括非公司商品）
    public static String queryCompanyWareTypeTree = ROOTHOST + "queryCompanyWareTypeTree";//查询公司类商品类别
    public static String querySaleWare = ROOTHOST + "querySaleWare";//获得常卖商品列表
    public static String querySaveWare = ROOTHOST + "querySaveWare";//获得收藏商品列表
    public static String queryStkWare = ROOTHOST + "queryStkWare";//根据类型查询商品列表:wareType
    public static String queryStkWare1 = ROOTHOST + "queryStkWare1";//模糊查询
    public static String addEmpWare = ROOTHOST + "addEmpWare";//收藏商品
    public static String deleteEmpWare = ROOTHOST + "deleteEmpWare";//取消收藏
    public static String queryCustomerHisWarePrice = ROOTHOST + "queryCustomerHisWarePrice";//获取商品历史价格，商品原价
    public static String queryAllCacheWare = ROOTHOST + "queryAllCacheWare";//查询所有商品
    public static String queryCompanyStockWareList = ROOTHOST + "queryCompanyStockWareList";//获取所有商品信息
    public static String queryAutoPrice = ROOTHOST + "queryAutoPrice";//获取是执行价还是历史价成功
    public static String queryOrderConfigWeb = ROOTHOST + "queryOrderConfigWeb";//获取手机端下单配置（拜访下单，订货下单）

    public static String queryWareWeb = ROOTHOST + "queryWareWeb";//获取商品信息
    public static String updateWareWeb = ROOTHOST + "updateWareWeb";//修改商品信息(早期的)
    public static String saveWare = ROOTHOST + "saveWare";//保存或修改商品

    //---------------------------商城-------------------------
    public static String toShoppingMall = ROOTHOST + "wxlogin";//进入商城
    public static String shopMember_apply = ROOTHOST + "shopMember/apply";//申请成为会员
    public static String shopMember_cancel = ROOTHOST + "shopMember/cancel";//取消企业会员
    public static String hotShop = ROOTHOST + "shopMember/hotShop";//热门商城
    public static String shopMember_mySuppliers = ROOTHOST + "shopMember/mySuppliers";//加载我的供货商
    public static String myFollow = ROOTHOST + "shopMember/myFollow";//我的关注
    public static String shopWareWeb_queryWareType = ROOTHOST + "shopWareWeb/queryWareType";//获取商品类别接口
    public static String shopWareWeb_page = ROOTHOST + "shopWareWeb/page";//获取商品列表
    public static String shopWareGroupWeb_queryWareGroupList = ROOTHOST + "shopWareGroupWeb/queryWareGroupList";//获取商品分组列表
    public static String shopWareWeb_showShopWare = ROOTHOST + "shopWareWeb/showShopWare";//获取商品详细接口
    public static String shopBforderWeb_queryShopBforderPage = ROOTHOST + "shopBforderWeb/queryShopBforderPage";//获取会员订单列表
    public static String queryAllShopOrder = ROOTHOST + "shopBforderWeb/queryAllShopOrder";//管理员获取所有商城订单
    public static String shopBforderWeb_queryShopBforderWeb = ROOTHOST + "shopBforderWeb/queryShopBforderWeb";//获取订单详细接口
    public static String shopBforderWeb_addAppOrderWeb = ROOTHOST + "shopBforderWeb/addAppOrderWeb";//添加订单接口
    public static String shopBforderWeb_deleteBforderWeb = ROOTHOST + "shopBforderWeb/deleteBforderWeb";//删除订单接口
    public static String shopBforderWeb_updateBforderWeb = ROOTHOST + "shopBforderWeb/updateBforderWeb";//修改订单接口
    public static String addWareFavorite = ROOTHOST + "shopWareFavoriteWeb/addWareFavorite";//会员收藏：商品
    public static String deleteWareFavorite = ROOTHOST + "shopWareFavoriteWeb/deleteWareFavorite";//取消会员收藏：商品
    public static String queryWareFavorite = ROOTHOST + "shopWareWeb/queryWareFavorite";//查询会员收藏：商品
    public static String addMemberAddressWeb = ROOTHOST + "shopMemberAddrss/addMemberAddressWeb";//添加会员地址
    public static String queryMemberAddressWeb = ROOTHOST + "shopMemberAddrss/queryMemberAddressWeb";//会员地址列表
    public static String queryMemberAddressWebById = ROOTHOST + "shopMemberAddrss/queryMemberAddressWebById";//会员地址
    public static String updateMemberAddressWeb = ROOTHOST + "shopMemberAddrss/updateMemberAddressWeb";//修改会员地址
    public static String deleteMemberAddressWeb = ROOTHOST + "shopMemberAddrss/deleteMemberAddressWeb";//删除会员地址
    public static String updateMemberDefaultAddressWeb = ROOTHOST + "shopMemberAddrss/updateMemberDefaultAddressWeb";//修改默认会员地址
    public static String queryMemberDefaultAddressWeb = ROOTHOST + "shopMemberAddrss/queryMemberDefaultAddressWeb";//获取默认会员地址


    //---------------------------车销-------------------------
    public static String addCarOorderWeb = ROOTHOST + "addCarOorderWeb";//车销单：添加
    public static String updateCarOrderWeb = ROOTHOST + "updateCarOrderWeb";//车销单：修改
    public static String queryCarOrder = ROOTHOST + "queryCarOrder";//车销单列表：查询
    public static String queryWebStkCarStorageList = ROOTHOST + "queryWebStkCarStorageList";//车销仓库列表
    public static String queryStorageWareCarList = ROOTHOST + "queryStorageWareCarList";//车销仓库商品列表
    public static String stkCarMove_page = ROOTHOST + "stkCarMove/page";//车销回库列表
    public static String stkCarMove_save = ROOTHOST + "stkCarMove/save";//车销回库保存更新
    public static String stkCarMove_show = ROOTHOST + "stkCarMove/show";//查看车销回库单
    public static String stkCarMove_audit = ROOTHOST + "stkCarMove/audit";//审批：车销配货单；车销回库单
    public static String stkCarMove_cancel = ROOTHOST + "stkCarMove/cancel";//作废：车销配货单；车销回库单
    public static String stkCarRecMast_pages = ROOTHOST + "stkCarRecMast/pages";//查看收款记录列表
    public static String stkCarRecMast_cancelStatus = ROOTHOST + "stkCarRecMast/cancelStatus";//作废收款记录
    public static String stkCarRecMast_confirmSk = ROOTHOST + "stkCarRecMast/confirmSk";//确认收款
    public static String stkCarRecMast_drageRec = ROOTHOST + "stkCarRecMast/drageRec";//暂存收款记录
    public static String stkCarRecMast_checkOrderHasRec = ROOTHOST + "stkCarRecMast/checkOrderHasRec";//检查车销订单是否有空
    public static String querySysCarBluetooth = ROOTHOST + "querySysCarBluetooth";//该蓝牙设备是否在平台上登记

    //---------------------------盘点库存-------------------------
    public static String drageStkCheck = ROOTHOST + "stkCheckWeb/drageStkCheck";//保存库存盘点
    public static String queryStkCheckPages = ROOTHOST + "stkCheckWeb/pages";//查询盘点单
    public static String queryStkCheck = ROOTHOST + "stkCheckWeb/showStkcheck";//查询单个盘点单
    public static String queryStorageWarePage = ROOTHOST + "queryStorageWarePage";//查询库存底下所有商品
    public static String drageStkCheckTemp = ROOTHOST + "stkCheckTempWeb/drageStkCheck";//添加临时盘点单
    public static String queryStkCheckTempPages = ROOTHOST + "stkCheckTempWeb/pages";//查询临时盘点单
    public static String queryStkCheckTemp = ROOTHOST + "stkCheckTempWeb/showStkcheck";//查询单个临时盘点单
    public static String mergeStkcheck = ROOTHOST + "stkCheckTempWeb/mergeStkcheck";//查询单个临时盘点单
    public static String getWareProduceDateList = ROOTHOST + "stkCheckWeb/getWareProduceDateList";//商品ids查询商品生产日期列表
    public static String drageBatchStkCheck = ROOTHOST + "stkCheckWeb/drageBatchStkCheck";//批次盘点暂存（添加和修改）


    //---------------------------流动打卡-------------------------
    public static String addFlow = ROOTHOST + "sign/addSignIn";//添加流动打卡
    public static String queryFlow = ROOTHOST + "sign/querySignInPage";//查询流动打卡
    public static String querySignInBfhf = ROOTHOST + "sign/querySignInBfhf";//流动打卡 + 上下班

    //---------------------------个人空间-------------------------
    public static String addSignInMS = ROOTHOST + "sign/addSignInMS";//
    public static String querySignInPageMS = ROOTHOST + "sign/querySignInPageMS";//

    //---------------------------库位管理-------------------------
    public static String queryPageForCrew = ROOTHOST + "stkIn/queryPageForCrew";//待入仓发票单列表
    public static String queryOutPageForCrewOut = ROOTHOST + "stkout/queryOutPageForCrewOut";//待出仓的发票单列表
    public static String queryStkInByBillId = ROOTHOST + "stkIn/queryStkInByBillId";//待入仓的发票单
    public static String queryStkOutByBillId = ROOTHOST + "stkout/queryStkOutByBillId";//待出仓的发票单
    public static String queryStorehouseList = ROOTHOST + "stkHouse/queryList";//查询库位列表
    public static String saveStkCreIn = ROOTHOST + "stkCrewIn/save";//保存入仓单
    public static String saveStkCreOut = ROOTHOST + "stkCrewOut/save";//保存出仓单
    public static String queryStkHouseWareList = ROOTHOST + "stkHouseWare/list";//查询库位商品
    public static String queryStkHouseWarePage = ROOTHOST + "stkHouseWare/page";//查询库位商品
    public static String saveStkCrew = ROOTHOST + "stkCrew/save";//保存库位整理
    public static String queryStkCrewPage = ROOTHOST + "stkCrew/page";//查询库位整理单列表
    public static String queryStkCrewById = ROOTHOST + "stkCrew/show";//查询库位整理单
    public static String queryStkCrewInPage = ROOTHOST + "stkCrewIn/page";//入仓单列表
    public static String queryStkCrewOutPage = ROOTHOST + "stkCrewOut/page";//出仓单列表
    public static String queryStkCrewOut = ROOTHOST + "stkCrewOut/show";//出仓单
    public static String queryStkCrewIn = ROOTHOST + "stkCrewIn/show";//入仓单
    public static String updateStatusAuditStkCrewIn = ROOTHOST + "stkCrewIn/audit";//入仓单(审批)
    public static String updateStatusAuditStkCrewOut = ROOTHOST + "stkCrewOut/audit";//出仓单(审批)

    //---------------------------拍照，相册-------------------------
    public static List<File> publish_file1 = new ArrayList<>();// 拍照和相册获得的图片容器
    public static List<File> publish_file2 = new ArrayList<>();// 拍照和相册获得的图片容器
    public static List<File> publish_file3 = new ArrayList<>();// 拍照和相册获得的图片容器
    public static String DIR_IMAGE_PROCEDURE = DIR_IMAGE + "procedure/";//缓存图片文件夹(拜访步骤)
    public static int maxImgCount = 6;//图片最多几张




    //---------------------------新的-------------------------
    public static String sumPageStat = ROOTHOST + "sumPageStat";//
    public static String queryShopinfo = ROOTHOST + "queryShopinfo";//
    public static String getBanners = ROOTHOST + "getBanners";//


}
