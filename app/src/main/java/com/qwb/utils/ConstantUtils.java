package com.qwb.utils;

import com.qwb.utils.SPUtils;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.db.DStepAllBean;
import com.qwb.view.delivery.model.DeliveryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建描述：常用类
 */

public class ConstantUtils {

    public static boolean IS_APPLY_NEW = true;//点击应用模式是否是新的(目前是获取新旧应用dataTp)
    public static final boolean DEBUG = true;
    public static final int STATUSBAR_ALPHA = 100;//状态栏的透明度
    public static final int TREE_ID = 100000;//树形结构：避免父，子id重复
    //TODO 有使用BASE的要在MyLoginUtil中修改
    public static final String BASE = SPUtils.getID()+"_"+SPUtils.getCompanyId();//树形结构：避免父，子id重复
    public static final String SCAN_RESULT = "scanResult";//扫描结果
    public static final String SCAN_RESULT_LIST = "scanResultList";//扫描结果
    public static final String ISNEW = "isNewVersion";//是否新版本
    public static String BFHF_TYPE = "";//默认拜访地图，2：流动打卡
    public static ArrayList<MineClientInfo> selectCustomerList = new ArrayList<>();
    public static List<List<MineClientInfo>> selectCustomerLists = new ArrayList<>();
    public static java.util.Map<Integer, List<MineClientInfo>> selectCustomerMap = new HashMap<>();
    public static DStepAllBean stepAllBean;//缓存的拜访步骤1,2,3,6
    public static ArrayList<DeliveryBean> selectDeliveryList = new ArrayList<>();//配送单列表
    public static ArrayList<DDeliveryCustomerBean> selectDeliveryCustomerList = new ArrayList<>();//配送单客户列表
    public static String NORMAL_XSTP = "正常销售";//默认销售类型

    //------------------------------SP------------------------------------------
    public static class Sp {
        public static final String  TOKEN = "token";
        public static final String  USER_ID = "memId";
        public static final String  USER_HEAD = "memberHead";
        public static final String  USER_NAME = "username";
        public static final String  USER_MOBILE = "memberMobile";
        public static final String  PASSWORD = "psw";
        public static final String  COMPANY_ID = "companyId";
        public static final String  COMPANY_S = "companys";
        public static final String  COMPANY_NAME = "companyName";
        public static final String  IS_CREAT = "iscreat";
        public static final String  IS_UNITMNG = "isUnitmng";
        public static final String  MSG_MODEL = "msgmodel";
        public static final String  DATA_SOURCE = "datasource";
        public static final String  IS_ROLE = "isJueshe";
        public static final String  IS_LOGIN = "islogin";//是否登录
        public static final String  RZ_STATE = "rzState";//手机认证状态
        public static final String  INIT_PASSWORD = "init_password";//初始密码
        public static final String  COMPANY_TYPE = "tpNm";//公司类型：快消，卖场
        public static final String  OPEN_TRACK_CUSTOM_TIME = "isOPen_traceTime";//打开轨迹自定义时间
        public static final String  USER_MANAGER = "zhanghaoguanli";//用户管理（账号管理）
        public static final String  TRACK_CUSTOM_TIME = "traceTime";//打开轨迹自定义时间
        public static final String  VERSION_UPDATE = "versonup";//版本更新
        public static String  APP_LIST = "appList"+BASE;//应用列表
        public static String  APP_LIST_NEW = "appList_new"+BASE;//应用列表(新的)
        public static String  APP_LIST_CHILDREN = "appList_children"+BASE;//应用列表_子
        public static String  APP_LIST_CHILDREN_NORMAL = "appList_children_normal"+BASE;//应用列表_子(快捷菜单默认)
        public static String  APPLY_QWB_LIST = "qwbList"+BASE;//应用列表--工作台
        public static String  APPLY_QWBFW_LIST = "qwfwList"+BASE;//应用列表-企微服务-微生活
        public static String  TRACK_UPDATE_TYPE = "track_update_type"+BASE;//轨迹上传类型
        public static String  FRAME_MEMBER_IDS_MAP = "memberIds_map"+BASE;//结构图（部门及员工）-拜访地图
        public static String  FRAME_BRANCH_IDS_MAP = "branchIds_map"+BASE;//结构图（部门及员工）-拜访地图
        public static String  WORK_STATE = "work_state"+BASE;//上班状态
        public static String  WORK_TIME = "work_time"+BASE;//上班时间（超过12小时自动下班）
        public static String  CAR_DEFAULT_STORAGE = "car_default_storage"+BASE;//车销：默认仓库
        public static String  CAR_DEFAULT_STORAGE_NAME = "car_default_storage_name"+BASE;//车销：默认仓库
        public static String  STORAGE_ZERO = "storage_zero"+BASE;//车销：默认仓库
        public static String  TRACK_UPLOAD_STATUS = "track_upload_status"+BASE;//轨迹上传状态：0不上传；1上传
        public static String  TRACK_UPLOAD_MIN = "track_upload_min"+BASE;//轨迹上传间隔：默认1分钟
        public static String  OPEN_SMALL_PRICE = "open_small_price"+BASE;//开启开单小单位参考
        public static String  SCAN_MULTIPLE = "scan_multiple";//第一次扫描多次提醒
        public static String  AUDIT_NORMAL_TAB = "audit_normal_tab";//审批默认tab
        public static String  STOREHOUSE_NORMAL_TAB = "storehouse_normal_tab";//库位默认tab

        public static String  TREE_BFDT_MEMBER_BRANCH_IDS = "tree_bfdt_member_branch_ids"+BASE;//拜访地图-员工，部门选中和部分选中的ids
        public static String  TREE_BFDT_MEMBER_BRANCH_IDS_MAP = "tree_bfdt_member_branch_ids_map"+BASE;//拜访地图:员工，部门选中和部分选中的ids(HashMap)
        public static String  NORMAL_SHOP_COMPANY_ID = "normal_shop_company_id"+BASE;//首页tab "默认商城"
        public static String  NORMAL_SHOP_COMPANY_NAME = "normal_shop_company_name"+BASE;//首页tab "默认商城"
        public static String  NORMAL_SHOP_COMPANY_URL = "normal_shop_company_url"+BASE;//首页tab "默认商城"
        public static String  CLEAR_NORMAL_SHOP_COMPANY = "clear_normal_shop_company"+BASE;//清空默认商城
        public static String  CHOOSE_WARE_PIC_SHOW = "choose_ware_pic_show";//选择商品时，图片是否显示
        public static String  FIRST_USE_BLUETOOTH = "first_use_bluetooth";//第一次使用蓝牙扫描枪
        public static String  STOP_UPLOAD_STEP_CACHE = "stop_upload_step_cache";//停止上传拜访缓存数据
        public static String  LOGIN_BASE_URL = "login_base_url";//基本域名
        public static String  LOGIN_JWT = "login_jwt";//基本域名

        public static String  CUSTOMER_MAP_INIT = "customer_map_init&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_PROVINCE = "customer_map_province&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_CITY = "customer_map_city&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_AREA = "customer_map_area&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_CUSTOMER_TYPE = "customer_map_customer_type&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_MEMBER_IDS = "customer_map_member_ids&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_MEMBER_NAMES = "customer_map_member_names&"+BASE;//客户分布图
        public static String  CUSTOMER_MAP_CB_NORMAL = "customer_map_cb_normal&"+BASE;//客户分布图

        public static String  MAP_MEMBER_USER_ID = "map_member_user_id&"+BASE;//员工分布图-地图中心点
        public static String  MAP_MEMBER_USER_NAME = "map_member_user_name&"+BASE;//员工分布图-地图中心点


    }

    //------------------------------Intent------------------------------------------
    public static class Intent {
        public static final int  REQUEST_CODE_ORDER = 100;//订货下单列表-修改订货
        public static final int  REQUEST_CODE_RETREAT = 101;//退货下单列表-修改订货
        public static final int  REQUEST_MINE_CLIENT_ADDRESS = 102;//添加客户-选择地址
        public static final int  REQUEST_MINE_CLIENT_PROVIDER = 103;//添加客户-选择经销商
        public static final int  REQUEST_STEP_UPDATE_CLIENT = 104;//添加客户-选择经销商
        public static final int  REQUEST_STEP_UPDATE_PROVIDER = 105;//添加客户-选择经销商
        public static final int  REQUEST_STEP_ONE = 106;//拜访步骤一
        public static final int  REQUEST_STEP_TWO = 107;//拜访步骤一
        public static final int  REQUEST_STEP_THREE = 108;//拜访步骤一
        public static final int  REQUEST_STEP_FOUR = 109;//拜访步骤一
        public static final int  REQUEST_STEP_FIVE = 110;//拜访步骤一
        public static final int  REQUEST_STEP_FIVE_FIVE = 111;//拜访步骤一
        public static final int  REQUEST_STEP_SIX = 112;//拜访步骤一
        public static final int  REQUEST_STEP_5_CHOOSE_GOODS = 113;//拜访步骤五-添加商品
        public static final int  REQUEST_CODE_CHOOSE_WARE = 113;//选择商品
        public static final int  REQUEST_STEP_4_CHOOSE_GOODS = 114;//拜访步骤4-添加商品
        public static final int  REQUEST_CODE_SCAN = 115;//扫二维码返回
        public static final int  RESULT_CODE_CHOOSE_GOODS = 200;//选择商品后（销售小结；订货下单）
        public static final int  RESULT_CODE_CHOOSE_WARE = 200;//选择商品
        public static final int  REQUEST_CODE_KAOQIN_ADDRESS = 201;//考勤地址
        public static final int  REQUEST_CODE_CHOOSE_CUSTOMER = 202;//选择客户
        public static final int  REQUEST_CODE_CHOOSE_DELIVERY = 203;//选择配送单
        public static final int  REQUEST_CODE_UPDATE_SUCCESS = 204;//修改成功
        public static final int  REQUEST_CODE_LOCATION = 205;//定位
        public static final String  STEP = "count";//拜访步骤一
        public static final String  STEP_ONE = "count1";//拜访步骤一
        public static final String  STEP_TWO = "count2";//拜访步骤一
        public static final String  STEP_THREE = "count3";//拜访步骤一
        public static final String  STEP_FOUR = "count4";//拜访步骤一
        public static final String  STEP_FIVE = "count5";//拜访步骤一
        public static final String  STEP_SIX = "count6";//拜访步骤一
        public static final String  SUCCESS = "state";//成功
        public static final String  ORDER_TYPE = "xdType";// 1：拜访步骤下单 2:单独下单(电话下单) 3：订货列表 4：退货 5：退货列表
        public static final String  ORDER_RED_MARK = "redMark";// 0.正常单；1.红字单
        public static final String  ORDER_STATE = "orderZt";//订单状态：审核，未审核（可修改）
        public static final String  ORDER_ID = "dd_Id";//订单id
        public static final String  CLIENT_NAME = "clientName";//客户名称
        public static final String  CLIENT_NAME2 = "KhNm";//客户名称
        public static final String  CLIENT_ID = "clientId";//客户名称
        public static final String  TITLE = "title";//标题
        public static final String  VALUE = "value";//值
        public static final String  KEY = "key";//key
        public static final String  SINGLE = "single";//单
        public static final String  IS_ME = "isMe";// 是否自己：1:我的，2：别人
        public static final String  TYPE = "type";//类型
        public static final String  MINE_CLIENT = "mineClient";//我的客户
        public static final String  TEL = "tel";//电话
        public static final String  MOBILE = "mobile";//电话
        public static final String  LINKMAN = "linkman";//联系人

        public static final String  ADDRESS = "address";//联系人
        public static final String  LATITUDE = "latitude";//经纬度
        public static final String  LONGITUDE = "longitude";//经纬度
        public static final String  LOCATION = "location";//位置
        public static final String  NEED_NAV = "neednav";//是否需要导航
        public static final String  IS_SIGNLE = "isSignle";//是否需要导航
        public static final String  PROVIDER_NAME = "chosiceProvider";//选择经销商
        public static final String  PROVIDER_ID = "providerId";//经销商id
        public static final String  PROVINCE = "province";//省份
        public static final String  CITY = "city";//市
        public static final String  AREA = "area";//区
        public static final String  URL = "url";//网址
        public static final String  ID = "id";//id
        public static final String  MEMBER_NAME = "member_name";//员工名称
        public static final String  MEMBERId = "memberId";//员工名称
        public static final String  LOOK_WORK_TABLE_DETAILS = "rizhi";//查看报表详情
        public static final String  START_TIME = "startTime";//开始时间
        public static final String  END_TIME = "endTime";//结束时间
        public static final String  SUPPLEMENT_TIME = "pdate";//补拜访时间
        public static final String  USER_ID = "mid";//用户id
        public static final String  USER_NAME = "name";//用户名
        public static final String  DATE = "date";//日期
        public static final String  COMPANY_ID = "companyId";//公司id
        public static final String  GOODS_ID = "goodsId";//商品id
        public static final String  REMARK = "remark";//备注
        public static final String  EDIT_PRICE = "editPrice";//价格是否可以编辑(默认true)
        public static final String  AREA_LONG = "areaLong";//考勤地址的有效范围
        public static final String  ORDER_NO = "orderNo";//订单号
        public static final String  STK_ID = "stkId";//仓库id
        public static final String  TOMPORARY_CLIENT = "tomporary_client";//临时客户
        public static final String  TOMPORARY_CLIENT_NAME = "tomporary_client_name";//临时客户名称
        public static final String  TOMPORARY_CLIENT_LIKMAN = "tomporary_client_likman";//临时客户联系人
        public static final String  TOMPORARY_CLIENT_PHONE = "tomporary_client_phone";//临时客户电话
        public static final String  IS_TOMPORARY_BF = "is_tomporary_bf";//是否是临时拜访（默认false）
        public static final String  PHONE = "phone";
        public static final String  SHENPI_MODEL = "shenpiModel";
        public static final String  WEB_KEY = "key";
        public static final String  BANKUAI = "bankuai";
        public static final String  LOG = "rizhi";
        public static final String  CHOOSE_WARE = "choose_ware";
        public static final String  CHOOSE_WARE_ID = "choose_ware_id";
        public static final String  CHOOSE_WARE_NEW = "choose_ware_new";
        public static final String  COUNT = "count";
        public static final String  PLAN_CALL = "planCall";//计划拜访
        public static final String  PDATE = "pdate";//计划拜访日期
        public static final String  SELECT_CUSTOMER = "selectCustomer";//选择客户
        public static final String  NAME = "name";
        public static final String  IDS = "ids";
        public static final String  IS_CONTAIN_SALE = "is_contain_sale";//查询商品分类时是否包含“常售商品”，“收藏商品”
        public static final String  NEED_CHECK = "needCheck";// 1 我的审核 2 我发起的
        public static final String  IS_SINGLE = "isSingle";//是否单
        public static final String  FILE_BEAN = "fileBean";//文件
        public static final String  SHENPI_MODEL_CHUAN = "mShenpiModel_chuan";//文件
        public static final String  CODE = "code";//验证码
        public static final String  SESSION_ID = "sessionId";//验证码-sessionId
        public static final String  AUTO_PRICE = "auto_price";//执行价还是历史价
        public static final String  P_DATE = "pdate";//补拜访时间
        public static final String  TAG = "tag";//标记
        public static final String  IS_SHOW = "isShow";//是否显示

    }

    public static class Event {
        public static final int  TAG_BASE = 0;//基本
        public static final int  TAG_ORDER = 1;//订货列表
        public static final int  TAG_RETREAT =2;//订货退货列表
        public static final int  TAG_MINE_CLIENT =3;//我的客户
        public static final int  TAG_GOODS_MANAGER =4;//商品管理
        public static final int  TAG_CREATE_COMPANY =5;//创建公司
        public static final int  TAG_SUPPLIER_ORDER_GOODS_LIST =6;//供货商-选中的商品列表改变：通知上个界面更新UI
        public static final int  TAG_SUPPLIER_CACHE_GOODS_LIST =7;//供货商-缓存商品
        public static final int  TAG_SUPPLIER_ADDRESS =8;//供货商-会员地址
        public static final int  TAG_CAR_CHOOSE_CLIENT =9;//车销下单-选择客户
        public static final int  TAG_DELIVERY =10;//配送单
        public static final int  TAG_APPLY =11;//更新应用
        public static final int  TAG_APPLY_YUN =12;//更新应用-微生活（云）
        public static final int  MSG_MODEL =13;//更改消息显示模式
        public static final int  TAG_CHANGE_COMPANY =14;//切换公司
        public static final int  TAG_STK_CHECK =15;//盘点库存
        public static final int  TAG_NORMAL_SHOP =16;//默认商城
        public static final int  TAG_CACHE_STEP_1 = 17;// 缓存拜访步骤1
        public static final int  TAG_CACHE_STEP_2 = 18;// 缓存拜访步骤2
        public static final int  TAG_CACHE_STEP_3 = 19;// 缓存拜访步骤3
        public static final int  TAG_CACHE_STEP_4 = 20;// 缓存拜访步骤4
        public static final int  TAG_CACHE_STEP_5 = 21;// 缓存拜访步骤5
        public static final int  TAG_CACHE_STEP_6 = 22;// 缓存拜访步骤6
        public static final int  TAG_CHOOSE_DELIVERY = 23;// 选择配送单
        public static final int  TAG_CHOOSE_CUSTOMER = 24;// 选择客户
        public static final int  TAG_OBJECT = 25;// 选择对象：1.供应商 2.员工 3.客户 4.其他往来单位
        public static final int  TAG_PLAN_LINE = 26;// 计划线路
        public static final int  TAG_CRASH = 27;// 闪退
        public static final int  TAG_SCAN = 28;// 扫描
        public static final int  TAG_STK_MOVE = 29;// 移库
    }

    public static class Map {
        //订货下单
        public static final String  STEP5_0_GOODS_ID = "c0";//步骤5-商品id
        public static final String  STEP5_1_GOODS_NAME = "c1";//步骤5-商品名称
        public static final String  STEP5_2_GOODS_XSTP = "c2";//步骤5-销售类型
        public static final String  STEP5_3_GOODS_COUNT = "c3";//步骤5-商品数量
        public static final String  STEP5_4_GOODS_DW= "c4";//步骤5-商品单位
        public static final String  STEP5_5_GOODS_DJ = "c5";//步骤5-商品单价
        public static final String  STEP5_5_GOODS_DJ_TEMP = "c5_temp";//步骤5-商品单价(临时)
        public static final String  STEP5_6_GOODS_ZJ = "c6";//步骤5-商品总价
        public static final String  STEP5_7_GOODS_DEL = "c7";//步骤5-删除
        public static final String  STEP5_8_GOODS_BEUNIT = "c8";//步骤5-包装单位代码或计量单位代码
        public static final String  STEP5_9_GOODS_MAXUNIT_CODE = "c9";//步骤5-包装单位代码
        public static final String  STEP5_10_GOODS_MINUNIT_CODE= "c10";//步骤5-计量单位代码
        public static final String  STEP5_11_GOODS_MAXUNIT = "c11";//步骤5-包装单位
        public static final String  STEP5_12_GOODS_MINUNIT = "c12";//步骤5-计量单位
        public static final String  STEP5_13_GOODS_HS_NUM = "c13";//步骤5-换算数量
        public static final String  STEP5_14_GOODS_ZXJ = "c14";//步骤5- 执行价
        public static final String  STEP5_15_GOODS_YJ = "c15";//步骤5- 原价
        public static final String  STEP5_16_GOODS_GG = "c16";//步骤5-商品规格
        public static final String  STEP5_17_GOODS_REMARK = "c17";//步骤5-备注
        public static final String  STEP5_18_STK_COUNT = "c18";//步骤5-商品虚拟库存提示


        //销售小结:品项，到货量，实销量，库存量，订单，销售类型，新鲜值，规格，备注，删除
        public static final String  STEP4_0_GOODS_ID = "c0";//步骤4-商品id
        public static final String  STEP4_1_GOODS_NAME = "c1";//步骤4-商品名称
        public static final String  STEP4_2_GOODS_DHL = "c2";//步骤4-到货量
        public static final String  STEP4_3_GOODS_SXL = "c3";//步骤4-实销量
        public static final String  STEP4_4_GOODS_KCL= "c4";//步骤4-库存量
        public static final String  STEP4_5_GOODS_DD = "c5";//步骤4-订单
        public static final String  STEP4_6_GOODS_XSTP = "c6";//步骤4-销售类型
        public static final String  STEP4_7_GOODS_WG = "c7";//步骤4-规格
        public static final String  STEP4_8_GOODS_BZ= "c8";//步骤4-备注
        public static final String  STEP4_9_GOODS_DEL = "c9";//步骤4-删除
        public static final String  STEP4_10_GOODS_XXZ= "c10";//步骤4-新鲜值
    }

    //枚举类
    public static class Menu {
        public static final int  INT_ADD = 1;
        public static final int  INT_UPDATE = 2;
        public static final int  INT_DEL = 3;
        public static final int  INT_DETAIL = 4;
    }

    //消息类
    public static class Messeage {
        //bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论(点评)；6.沟通；10.日志-工作汇报；11.商城；12.公告）
        public static final int  M_SUM = -1;
        public static final int  M_XTTZ = 1;
        public static final int  M_SP = 2;
        public static final int  M_YBS = 3;
        public static final int  M_PL = 4;
        public static final int  M_GT = 6;
        public static final int  M_GZHB = 10;
        public static final int  M_SC = 11;
        public static final int  M_GG = 12;
    }

    //下单
    public static class Order {
        // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）7：车销单（添加）8：车销单（修改，详情）9.商城详情 10.历史订单
        public static final int  O_BF = 1;
        public static final int  O_DH = 2;
        public static final int  O_DHXD_LIST = 3;
        public static final int  O_TH = 4;
        public static final int  O_TH_LIST = 5;
        public static final int  O_CAR_ADD = 7;
        public static final int  O_CAR_UPDATE = 8;
        public static final int  O_SC = 9;
        public static final int  O_HISTORY = 10;
        public static final int  O_CXPH = 1;//车销配货
        public static final int  O_CXHK = 2;//车销回库
        public static final int  O_CXPH_LIST = 3;//车销配货单列表
    }

    public static class MapType {
        public static final int  MAP_VISIT = 1;//员工在线
        public static final int  MAP_record = 2;//拜访记录
        public static final int  MAP_track = 3;//拜访回放
        public static final int  MAP_call = 4;//拜访查询
    }

    //应用列表（包括应用代码）
    public static class Apply {
        public static final String TXL_OLD = "txl";//通讯录
        public static final String SHP_OLD = "shp";//审批
        public static final String GG_OLD = "gg";//公告
        public static final String KQ_OLD = "kq";//考勤
        public static final String XX_OLD = "xx";//消息
        public static final String KHGL_OLD = "khgl";//客户管理
        public static final String BFDT_OLD = "bfdt";//拜访地图
        public static final String BFCX_OLD = "bfcx";//拜访查询
        public static final String WAREINFO_OLD = "wareInfo";//商品信息
        public static final String GHSGL_OLD = "ghsgl";//供货商
        public static final String CXXDGL_OLD = "cxxdgl";//车销下单
        public static final String WLPSZXGL_OLD = "wlpszxgl";//物流配送中心
        public static final String SJDJXC_OLD = "sjdjxc";//移动进销存
        public static final String BBTJ_OLD = "bbtj";//报表统计
        public static final String BFJH_OLD = "bfjh";//拜访计划
        public static final String GZHB_OLD = "gzhb";//工作汇报
        public static final String BBFXTJ_OLD = "bbfxtj";//报表分析

//        public static final String  YBS_NEW = "ybs-new";//易办事
//        public static final String  TXL_NEW = "txl-new";//通讯录
//        public static final String  SHP_NEW = "shp-new";//审批
//        public static final String  GG_NEW = "gg-new";//公告
//        public static final String  KQ_NEW = "kq-new";//考勤
//        public static final String  XX_NEW = "xx-new";//消息
//        public static final String  KHGL_NEW = "khgl-new";//客户管理
//        public static final String  BFDT_NEW = "bfdt-new";//拜访地图
//        public static final String  DKDT_NEW = "dkdt-new";//拜访地图(打卡地图)
//        public static final String  BFCX_NEW = "bfcx-new";//拜访查询
//        public static final String  DHXD_NEW = "dhxd-new";//订货下单
//        public static final String  SPXX_NEW = "spxx-new";//商品信息
//        public static final String  WDSJ_NEW = "wdsj-new";//供货商(我的商家)
//        public static final String  CXXD_NEW = "cxxd-new";//车销下单
//        public static final String  WLPSZX_NEW = "wlpszx-new";//物流配送中心
//        public static final String  JXC_NEW = "jxc-new";//移动进销存
//        public static final String  SCGL_NEW = "scgl-new";//商城管理
//        public static final String  TJBB_NEW = "tjbb-new";//统计报表
//        public static final String  BFJH_NEW = "bfjh-new";//拜访计划
//        public static final String  YP_NEW = "yp-new";//云盘
//        public static final String  GZHB_NEW = "gzhb-new";//工作汇报
//        public static final String  BBFX_NEW = "bbfx-new";//报表分析
//        public static final String  PDKC_NEW = "pdkc-new";//盘点库存
//        public static final String  LDDK_NEW = "lddk-new";//流动打卡
//        public static final String  DKCX_NEW = "dkcx-new";//打卡查询
//        public static final String  HCWDKH_NEW = "hcwdkh-new";//缓存我的客户
//        public static final String  WDHC_NEW = "wdhc-new";//我的缓存
//        public static final String  XYPZ_NEW = "xypz-new";//协议凭证
//        public static final String  TJ_NEW = "tj-new";//编辑应用
//        public static final String  NORMAL = "";//默认

        public static final String  TJ_NEW = "tj_new";//编辑应用
        public static final String  NORMAL = "";//默认
        //企业办公
        public static final String  YBS_NEW = "ybs_new";//易办事
        public static final String  SP_NEW = "sp_new";//审批
        public static final String  KQ_NEW = "kq_new";//考勤
        public static final String  GG_NEW = "gg_new";//企微社区--公告
        public static final String  SPZQ_NEW = "spzq_new";//商品展区--商品信息
        public static final String  BFDT_NEW = "bfdt_new";//员工在线--拜访地图
        public static final String  WDHC_NEW = "wdhc_new";//我的缓存
        public static final String  TJBB_NEW = "tjbb_new";//拜访报表--统计报表
        public static final String  SPZQ_SHOW_SALE_PRICE = "spzq_show_sale_price";//商品展区--查看销售价
        public static final String  SPZQ_SHOW_IN_PRICE = "spzq_show_in_price";//商品展区--查看采购价

        //业务外勤管理
        public static final String  KHGL_NEW = "khgl_new";//我的客户--客户管理
        public static final String  KHGL_QUERY_NEW = "khgl_query_new";//我的客户--客户管理--查询
        public static final String  KHGL_CALL_NEW = "khgl_call_new";//我的客户--客户管理--拜访
        public static final String  KHGL_NEAR_NEW = "khgl_show_around_customer";//我的客户--客户管理--周边客户
        public static final String  BFCX_NEW = "bfcx_new";//我的拜访--拜访查询
        public static final String  DHXD_NEW = "dhxd_new";//订货下单
        public static final String  DHXD_QUERY_NEW = "dhxd_query_new";//订货下单-查询
        public static final String  DHXD_ORDER_NEW = "dhxd_order_new";//订货下单-下单
        public static final String  JHBF_NEW = "jhbf_new";//计划拜访
        public static final String  JHBF_QUERY_NEW = "jhbf_query_new";//计划拜访--查询
        public static final String  JHBF_CALL_NEW = "jhbf_call_new";//计划拜访--拜访
        public static final String  GZHB_NEW = "gzhb_new";//工作汇报
        public static final String  WLPSZX_NEW = "wlpszx_new";//物流配送中心
        public static final String  CXGL_NEW = "cxgl_new";//车销管理--车销下单
        public static final String  CXGL_QUERY_NEW = "cxgl_query_new";//车销管理--车销下单--查询
        public static final String  CXGL_ORDER_NEW = "cxgl_order_new";//车销管理--车销下单--下单
        public static final String  LDDK_NEW = "lddk_new";//流动打卡
        public static final String  DKCX_NEW = "dkcx_new";//打卡查询
        public static final String  DKDT_NEW = "dkdt_new";//打卡地图

        //库存管理
        public static final String  JSKC_NEW = "jskc_new";//即时库存
        public static final String  RKTJ_NEW = "rktj_new";//入库统计
        public static final String  CKTJ_NEW = "cktj_new";//出库统计
        public static final String  KCPD_NEW = "kcpd_new";//库存盘点
        public static final String  ZDKCYJ_NEW = "zdkcyj_new";//最低库存预警

        //销售台账
        public static final String  KHXSTJ_NEW = "khxstj_new";//客户销售统计
        public static final String  YWXSTJ_NEW = "ywxstj_new";//业务销售统计
        public static final String  SPXSTJ_NEW = "spxstj_new";//商品销售统计
        public static final String  KHTRFYB_NEW = "khtrfyb_new";//客户投入费用表

        //财务台账
        public static final String  HBZJ_NEW = "hbzj_new";//货币资金
        public static final String  YSZK_NEW = "yszk_new";//应收账款
        public static final String  YFHK_NEW = "yfhk_new";//应付货款
        public static final String  QTYSK_NEW = "qtysk_new";//其他应收款
        public static final String  QTYFK_NEW = "qtyfk_new";//其他应付款
        public static final String  YSZK_NEW2 = "yszk_new2";//预收账款
        public static final String  YFZK_NEW = "yfzk_new";//预付帐款
        public static final String  YHYYE_NEW = "yhyye_new";//云会员余额

        //会计报表
        public static final String  XJRBB_NEW = "xjrbb_new";//现金日报表
        public static final String  LRB_NEW = "lrb_new";//利润表
        public static final String  ZCFZB_NEW = "zcfzb_new";//资产负债表
        public static final String  FYTJ_NEW = "fytj_new";//费用统计

        //单据管理
        public static final String  XSDD_NEW = "xsdd_new";//销售订单
        public static final String  XSFP_NEW = "xsfp_new";//销售发票
        public static final String  FHD_NEW = "fhd_new";//发货单
        public static final String  CGFP_NEW = "cgfp_new";//采购发票
        public static final String  SKD_NEW = "skd_new";//收款单
        public static final String  FKD_NEW = "fkd_new";//付款单
        public static final String  FYD_NEW = "fyd_new";//费用单

        //商城管理
        public static final String  SCSPSJ_NEW = "scspsj_new";//商城商品上架
        public static final String  HYDJSZ_NEW = "hydjsz_new";//会员等级价格设置
        public static final String  SCDD_NEW = "scdd_new";//商城订单
        public static final String  YCDDCL_NEW = "ycddcl_new";//异常订单处理
        public static final String  SCYYBB_NEW = "scyybb_new";//商城营业报表
        public static final String  WDSJ_NEW = "wdsj_new";//供货商(我的商家)

        //门店管理
        public static final String  MDSPSJ_NEW = "mdspsj_new";//门店商品上架
        public static final String  MDHYDJJGSZ_NEW = "mdhydjjgsz_new";//门店会员等级价格设置
        public static final String  MDDD_NEW = "mddd_new";//门店订单
        public static final String  MDYYBB_NEW = "mdyybb_new";//门店营业报表

        //我的空间
        public static final String  TXL_NEW = "txl_new";//通讯录
        public static final String  DKKH_NEW = "dkkh_new";//客户积累
        public static final String  WDZJ_NEW = "wdzj_new";//我的足迹
        public static final String  LYHC_NEW = "lyhc_new";//旅游画册
        public static final String  WDWZ_NEW = "wdwz_new";//我的位置
        public static final String  WDYJ_NEW = "wdyj_new";//我的一家
        public static final String  WDDW_NEW = "wddw_new";//我的单位
        public static final String  GZSJ_NEW = "gzsj_new";//关注商家
        public static final String  YJFK_NEW = "yjfk_new";//意见反馈
        public static final String  CGFP_NEW_PHONE = "cgfp_new_phone";//采购发票（原生的）
        public static final String  CONTACT = "contact";//联系客服

        //管理员配置
        public static final String  JSPZ_NEW = "jspz_new";//角色配置
        public static final String  SPFLSZ_NEW = "spflsz_new";//商品分类设置
        public static final String  SPPPGL_NEW = "spppgl_new";//商品品牌管理
        public static final String  KHLXGL_NEW = "khlxgl_new";//客户类型管理
        public static final String  KHDJGL_NEW = "khdjgl_new";//客户等级管理
        public static final String  GSBMGL_NEW = "gsbmgl_new";//公司部门管理
        public static final String  CLGL_NEW = "clgl_new";//车辆管理
        public static final String  SJSZ_NEW = "sjsz_new";//司机设置
        public static final String  ZJZHGL_NEW = "zjzhgl_new";//资金账户管理
        public static final String  YWYGL_NEW = "ywygl_new";//业务员管理
        public static final String  SPJGGL_NEW = "spjggl_new";//商品价格管理
        public static final String  SPTRFYGL_NEW = "sptrfygl_new";//销售投入费用设置
        public static final String  QYXXSZ_NEW = "qyxxsz_new";//企业信息设置

        public static final String  SERVICE_MESSAGE = "service_message";//客服

        //库位管理
        public static final String  KWGL_NEW = "kwgl_new";//库位管理
        public static final String  KWGL_KWZL_NEW = "kwgl_kwzl_new";//库位整理
        public static final String  KWGL_RCD_NEW = "kwgl_rcd_new";//入仓单
        public static final String  KWGL_CCD_NEW = "kwgl_ccd_new";//出仓单
        public static final String  KWGL_WARE_NEW = "kwgl_ware_new";//库位商品

        //----------------------------子菜单--------------------------------
        public static final String  SPZQ_NEW_UPDATE = "spzq_new_update";//商品展区--修改
        public static final String  SPZQ_NEW_CKQB = "spzq_new_ckqb";//商品展区--查看全部


    }






}
