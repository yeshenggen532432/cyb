package com.qwb.receive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.qwb.utils.MyLoginUtil;

public class MySQLite extends SQLiteOpenHelper {
	private Context mContext;

	public MySQLite(Context context, int version) {
		super(context, "clould.db", null, version);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String other1 = "CREATE TABLE IF NOT EXISTS other_1(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "time varchar(50),count varchar(50))";
		db.execSQL(other1);
		String other2 = "CREATE TABLE IF NOT EXISTS other_2(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "time varchar(50),count varchar(50))";
		db.execSQL(other2);
		String other3 = "CREATE TABLE IF NOT EXISTS other_3(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "time varchar(50),count varchar(50))";
		db.execSQL(other3);
		String other4 = "CREATE TABLE IF NOT EXISTS other_4(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "time varchar(50),count varchar(50))";
		db.execSQL(other4);

		/**
		 * 用来记录其他七七八八的状态 有未读消息 悬浮按钮变色 未读消息条数 menucolor 是否有未读消息（按钮变色 val = 1 有未读
		 * val = 0 没有未读） rememberpsw 值为用户手机号码memberMobile 记住密码 （涉及到换账号
		 * 所以用数据库存储状态 默认为val = 0记住密码 val = 1为不记住） 审批模块保存审批人 mark 区分每个模板 val 用户头像
		 * num 用户id rememberpsw 用户名字 未读消息分模块显示最新消息
		 */
		String other = "CREATE TABLE IF NOT EXISTS other(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "menucolor INTEGER,rememberpsw varchar(1000),mark varchar(1000),num INTEGER,userid varchar(1000),companyId varchar(50),val varchar(1000))";
		db.execSQL(other);

		// (只保存最新的那条显示在未读界面)未读消息的表（unread_sysnotify）
		/**
		 * blbankuai (belongbankuai)所属板块 1系统通知2公告3易办事4通讯录板块（部门聊天与私信） isact
		 * 是否可操作（0 该条未读没操作过1 已经同意 2已经拒绝 ）（被邀请等操作过后就不可再次操作了） isneedclean 是不是需要清空（
		 * 默认是0可以清空1不可以清空）
		 */
		String xitong = "CREATE TABLE IF NOT EXISTS ur_sysnotify(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"companyId varchar(50),"
				+ "memberid INTEGER,belongId INTEGER,belongname varchar(1000),isread varchar(1000),blbankuai INTEGER,belongMsg varchar(1000),"
				+ "userid INTEGER,lastmsg varchar(1000),addtime varchar(50),msgtp varchar(50),mark varchar(50),type varchar(50),"
				+ "headurl varchar(1000),membername varchar(1000),isneedclean INTEGER,isact INTEGER)";
		db.execSQL(xitong);

		// //未读消息的列表数据(根据memberid该条消息的发送者， userid 区分哪个用户de记录
		// //lastmsg该id的最后一条消息addtime时间type区分是系统/私信/群聊(用在listview行点击事件是区分该条未读消息的类型))
		// //mark 在存储数据时用来区分这条未读消息是否是同个类型数据
		// //msg消息内容addtime时间msgtype区分该条消息是图片还是文字还是语音
		// //type 区分圈，公司，部门聊天
		// String message =
		// "CREATE TABLE IF NOT EXISTS msgunread(_id INTEGER PRIMARY KEY
		// AUTOINCREMENT,"
		// +
		// "memberid INTEGER,belongId INTEGER,belongname varchar(1000),belongMsg
		// varchar(1000),userid INTEGER,lastmsg varchar(1000),addtime
		// varchar(50),msgtp varchar(50),mark varchar(50),type varchar(50),"
		// + "headurl varchar(1000),membername varchar(1000))";
		//
		// db.execSQL(message);
		// 存储私信消息membername该条消息的发送者名字，memberid该条消息的发送者id
		// userid登陆者的ID,recieveid该条消息接受者的id，
		// msg消息内容addtime时间msgtype区分该条消息是图片还是文字还是语音
		// belongid 该条消息的聊天对象是谁
		// mark 用对方的id跟自己的id凭借组成区别
		// 私信聊天记录
		String allmsg = "CREATE TABLE IF NOT EXISTS mymsg(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "memberid INTEGER,userid INTEGER,belongid varchar(1000),belongname varchar(1000),belongMsg varchar(1000),msgId INTEGER,msg varchar(1000),addtime varchar(50),msgtype varchar(50),type varchar(50),"
				+ "latitude varchar(1000),longitude varchar(1000),"
				+ "headurl varchar(1000),mark varchar(1000),voicetime varchar(1000),membername varchar(1000))";

		db.execSQL(allmsg);
		// 保存圈聊天记录的表
		String msg_quan = "CREATE TABLE IF NOT EXISTS quanmsg(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "memberid INTEGER,userid INTEGER,belongid varchar(1000),belongname varchar(1000),belongMsg varchar(1000),msgId INTEGER,msg varchar(1000),addtime varchar(50),msgtype varchar(50),type varchar(50),"
				+ "latitude varchar(1000),longitude varchar(1000),"
				+ "headurl varchar(1000),voicetime varchar(1000),membername varchar(1000))";

		db.execSQL(msg_quan);
		// 保存部门聊天记录的表
		String msg_bumen = "CREATE TABLE IF NOT EXISTS bumenmsg(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "memberid INTEGER,userid INTEGER,belongid varchar(1000),belongname varchar(1000),belongMsg varchar(1000),msgId INTEGER,msg varchar(1000),addtime varchar(50),msgtype varchar(50),type varchar(50),"
				+ "latitude varchar(1000),longitude varchar(1000),"
				+ "headurl varchar(1000),voicetime varchar(1000),membername varchar(1000))";
		db.execSQL(msg_bumen);
		// 保存公司聊天记录的表
		String msg_gognsi = "CREATE TABLE IF NOT EXISTS gognsimsg(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "memberid INTEGER,userid INTEGER,belongid varchar(1000),belongname varchar(1000),belongMsg varchar(1000),msgId INTEGER,msg varchar(1000),addtime varchar(50),msgtype varchar(50),type varchar(50),"
				+ "latitude varchar(1000),longitude varchar(1000),"
				+ "headurl varchar(1000),voicetime varchar(1000),membername varchar(1000))";
		db.execSQL(msg_gognsi);

		// TODO 拜访流程--数据缓存
		// 0保存拜访流程--图片--字段cid与前面的表相关联
		String bfphoto = "CREATE TABLE IF NOT EXISTS biao_bfphoto(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "cid varchar(100),picId varchar(100),type varchar(10),ssId varchar(100),picMini varchar(1000),pic varchar(1000))";
		db.execSQL(bfphoto);

		// 1：保存拜访流程1-拜访签到拍照的表
		String bfqdpz = "CREATE TABLE IF NOT EXISTS biao_bfqdpz(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "pzid varchar(100),cid varchar(100),longitude varchar(50),latitude varchar(50),address varchar(100),hbzt varchar(50),ggyy varchar(50),isXy varchar(10),remo varchar(1000))";
		db.execSQL(bfqdpz);

		// 2：保存拜访流程2-生动化检查的表
		String bfsdhjc = "CREATE TABLE IF NOT EXISTS biao_bfsdhjc(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "sdhId varchar(100),cid varchar(100),isXy varchar(50),remo1 varchar(1000),remo2 varchar(1000),pophb varchar(100),cq varchar(100),wq varchar(100))";
		db.execSQL(bfsdhjc);

		// 4：保存拜访流程4-销售小结
		String bfxsxj = "CREATE TABLE IF NOT EXISTS biao_bfxsxj(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "xsxjId varchar(100),cid varchar(100),wid varchar(100),wareNm varchar(100),dhNum varchar(100),sxNum varchar(100),kcNum varchar(100),"
				+ "ddNum varchar(100),xstp varchar(100),xxd varchar(100),wareGg varchar(100),remo varchar(1000))";
		db.execSQL(bfxsxj);

		// 6：保存拜访流程6-道谢并告知下次拜访的表
		String bfxcbf = "CREATE TABLE IF NOT EXISTS biao_bfxcbf(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "xcbfId varchar(100),cid varchar(100),xsjdNm varchar(50),bfflNm varchar(50),bcbfzj varchar(1000),dbsx varchar(1000),xcdate varchar(100)"
				+ ",latitude varchar(100),longitude varchar(100),address varchar(100))";
		db.execSQL(bfxcbf);

		upDbTo3(db);// 上传轨迹
		upDbTo4(db);// 周边客户，我的客户
		upDbTo5(db);
		upDbTo7(db);//订货下单
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// for循环执行数据库版本跟新
		for (int i = oldVersion+1; i <= newVersion; i++) {
			switch (i) {
				case 3:
					upDbTo4(db);
					break;
				case 4:
					upDbTo5(db);
					break;
				case 6:
					upDbTo6(db);
					break;
				case 7:
					upDbTo7(db);
					break;
				case 8:
					upDbTo8(db);
					break;
				case 9:
					upDbTo9(db);
				break;
			}
		}
		//退到登录页
		MyLoginUtil.logout(mContext);
	}

	

	// 数据升级为3时添加了 --上传轨迹
	private void upDbTo3(SQLiteDatabase db) {
		/**
		 * “上传轨迹”表： company_id：公司id user_id：用户id longitude： latitude： address：地址
		 * location_time：时间戳 location_from：gps,wifi os：Android，ios state：是否上传成功
		 * ---0成功，1失败
		 */
		String track = "CREATE TABLE IF NOT EXISTS biao_track(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "company_id varchar(20),user_id varchar(20),longitude varchar(50),latitude varchar(50),"
				+ "location_time INTEGER,address varchar(100),location_from varchar(50),"
				+ "os varchar(10),state INTEGER)";
		db.execSQL(track);
	}

	// 数据升级为3时添加了 --上传轨迹
	private void upDbTo5(SQLiteDatabase db) {
		// "address": "福建省厦门市思明区西林西里16号",---------------地址
		// "location": "[118.14831956967,24.47794125686]",---经纬度
		// "memberJob": "",----------------------------------职位
		// "zt": "在线",--------------------------------------状态
		// "times": "2017-06-02 10:29:11",-------------------时间
		// "userNm": "王亦强",---------------------------------名称
		// "userTel": "13806090563",-------------------------电话
		// "userHead": "publicplat/member/1462374247938.jpg"--头像
		// "userId": 196-------------------------------------业务员id
		// "mid": -------------------------------------------用户id--区分不同账号
		// "remo_1": ----------------------------------------备用
		// "remo_2": ----------------------------------------备用
		// "remo_3": ----------------------------------------备用

		String shishichagang = "CREATE TABLE IF NOT EXISTS shishichagang(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "address varchar(100),location varchar(50),memberJob varchar(50),zt varchar(50),"
				+ "times varchar(50),userNm varchar(50),userTel varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "userHead varchar(50),userId INTEGER, mid varchar(20))";
		db.execSQL(shishichagang);
	}

	// 我的客户 --添加一列“距离字段”--数据类型为double
	private void upDbTo6(SQLiteDatabase db) {
		db.execSQL("ALTER TABLE mine_client ADD distance DOUBLE");
	}

	// 数据升级为3时添加了 --上传轨迹
	private void upDbTo4(SQLiteDatabase db) {
		// "address": ----------地址
		// "id": ---------------cid客户id
		// "longitude": --------经纬度
		// "latitude":
		// "khTp": 2,-----------客户类型
		// "khNm": "路突然",------客户名称
		// "linkman": ----------联系人
		// "xxzt": -------------新鲜度--正常，临期
		// "jlkm":
		// "qdtpNm": "零售",-----
		// "xsjdNm": "流失",
		// "scbfDate": ---------上次拜访时间
		// "mobile":
		// "tel": "3555555",
		// "city": "厦门市",
		// "province": "福建省",--省份
		// "area": "思明区"-------地区

		// 备用--remo_1,remo_2,remo_3

		String near_client = "CREATE TABLE IF NOT EXISTS near_client(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "address varchar(100),cid INTEGER,longitude varchar(50),latitude varchar(50),"
				+ "khTp INTEGER,khNm varchar(50),linkman varchar(50),scbfDate varchar(50),"
				+ "jlkm varchar(50),qdtpNm varchar(50),xsjdNm varchar(50),"
				+ "mobile varchar(50),tel varchar(50),xxzt varchar(50),"
				+ "province varchar(50),city varchar(50),area varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50)," + "mid varchar(50))";
		db.execSQL(near_client);

		// "address": "",
		// "id": 139,
		// "area": "思明区",
		// "province": "福建省",
		// "city": "厦门市",
		// "khNm": "旅途",
		// "khTp": 2,
		// "linkman": "吃火锅",
		// "tel": "577888",
		// "mobile": "5888777",
		// "longitude": "118.192705",
		// "latitude": "24.495365",
		// "scbfDate": "",

		// "jlkm": "0.0"
		// "qdtpNm": "连锁",
		// "xsjdNm": "意向签约",
		// "memberNm": "",
		// "branchName": "技术部",

		// mid--用来区分

		String mine_client = "CREATE TABLE IF NOT EXISTS mine_client(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "address varchar(100),cid INTEGER,longitude varchar(50),latitude varchar(50),"
				+ "khTp INTEGER,khNm varchar(50),linkman varchar(50),"
				+ "mobile varchar(50),tel varchar(50),scbfDate varchar(50),"
				+ "province varchar(50),city varchar(50),area varchar(50),"
				+ "jlkm varchar(50),distance DOUBLE,qdtpNm varchar(50),xsjdNm varchar(50),"
				+ "memberNm varchar(50),branchName varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50)," + "mid varchar(50))";
		db.execSQL(mine_client);
	}

	// 订货下单
	private void upDbTo7(SQLiteDatabase db) {
//		private int id; // 订单id
//		private String memberNm;// 业务员名称
//		private String khNm; // 客户名称
//		private String orderNo; // 订单号
//		private String orderZt; // 订单状态（审核，未审核）
//		private String oddate; // 日期
//		private int isMe; // 1：我  2：别人
//		//快消--字段
//		private String odtime; // 时间
//		private String tel; // 电话
//		private String shr; // 收货人
//		private String cjje; // 金额
//		private String ddNum; // 数量
//		//卖场--字段
//		private int mid; // 业代id
//		private int cid; // 客户id

		String dhxd = "CREATE TABLE IF NOT EXISTS dhxd(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "memberNm varchar(50),khNm varchar(50),orderNo varchar(50),orderZt varchar(50),"
				+ "oddate varchar(50),odtime varchar(50),tel varchar(50),"
				+ "shr varchar(50),cjje varchar(50),ddNum varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "isMe INTEGER,ddid INTEGER,mid INTEGER, cid INTEGER,userId INTEGER)";
		db.execSQL(dhxd);
		
//		private int id;//拜访id
//		private int cid;
//		private int mid;
//		private int jlm;
//		private String bcbfzj;
//		private String voiceUrl;// 语音-路径
//		private String voiceTime;// 语音-时长
//		private String memberNm;
//		private String khdjNm;
//		private String qddate;// 时间段
//		private String qdtime;// 时长
//		private String khNm;
//		private String memberHead;
//		private String branchName;
//		private String address;
//		private int zfcount;//客户重复条数
//		private String longitude;//签到
//		private String latitude;
//		private String longitude2;//签退
//		private String latitude2;
//		private String longitude3;//客户地址
//		private String latitude3;
		
		//备注：userId区分不同账号，bfid关联评论，图片
		String bfcx = "CREATE TABLE IF NOT EXISTS bfcx(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "bcbfzj varchar(50),voiceUrl varchar(50),voiceTime varchar(50),memberNm varchar(50),"
				+ "khdjNm varchar(50),qddate varchar(50),qdtime varchar(50),"
				+ "khNm varchar(50),memberHead varchar(50),branchName varchar(50),"
				+ "address varchar(50),longitude varchar(50),latitude varchar(50),"
				+ "longitude2 varchar(50),latitude2 varchar(50),longitude3 varchar(50),latitude3 varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "zfcount INTEGER,jlm INTEGER,mid INTEGER, cid INTEGER,bfid INTEGER,userId INTEGER)";
		db.execSQL(bfcx);
		//评论
		String comment = "CREATE TABLE IF NOT EXISTS comment(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "content varchar(50),memberNm varchar(50),rcNm varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "voiceTime INTEGER,commentId INTEGER,memberId INTEGER,belongId INTEGER, position INTEGER,bfid INTEGER,userId INTEGER)";
		db.execSQL(comment);
		//回复
		String rc = "CREATE TABLE IF NOT EXISTS rc(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "content varchar(50),rcNm varchar(50),memberNm varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "voiceTime INTEGER,commentId INTEGER,memberId INTEGER,belongId INTEGER, rcPosition INTEGER,plId INTEGER,userId INTEGER)";
		db.execSQL(rc);
		//图片
		String pic = "CREATE TABLE IF NOT EXISTS pic(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "pic varchar(50),picMin varchar(50),nm varchar(50),"
				+ "remo_1 varchar(50),remo_2 varchar(50),remo_3 varchar(50),"
				+ "bfid INTEGER,userId INTEGER)";
		db.execSQL(pic);
	}

	// 系统板块--添加字段companyId：INTEGER
	private void upDbTo8(SQLiteDatabase db) {
		db.execSQL("ALTER TABLE ur_sysnotify ADD companyId varchar(50)");
	}

	// 系统板块--添加字段companyId：INTEGER
	private void upDbTo9(SQLiteDatabase db) {
		db.execSQL("ALTER TABLE other ADD companyId varchar(50)");
	}

}
