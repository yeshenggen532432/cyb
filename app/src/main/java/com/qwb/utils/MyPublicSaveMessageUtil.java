package com.qwb.utils;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qwb.application.MyApp;
import com.qwb.view.tab.model.MsgBean;
import com.qwb.receive.UnReadNumManager;
import com.qwb.db.DCategoryMessageBean;
import com.qwb.db.DMessageBean;

import java.util.List;

/**
 * Created by yeshenggen on 2019/5/24.
 */

public class MyPublicSaveMessageUtil {

    private static MyPublicSaveMessageUtil MANAGER = null;

    public static MyPublicSaveMessageUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyPublicSaveMessageUtil();
        }
        return MANAGER;
    }

    public void doPublicSaveMessage(MsgBean msgBean, Context context){
        List<MsgBean.MsgItemBean> msgList = msgBean.getMsgList();
        if(null == msgList || msgList.isEmpty()){
            return;
        }
        //处理保存到新的数据库
        doNewMessage(msgList);
//        //处理保存到旧的数据库
//        doOldMessage(msgList);

        // 发广播到未读消息界面
        Intent intent2 = new Intent();
        intent2.setAction(Constans.UnRreadMsg);
        MyApp.getI().sendBroadcast(intent2);
        for (MsgBean.MsgItemBean msgItemBean :msgList) {
            if ("35".equals(msgItemBean.getTp())) {
                // 角色应用菜单变更，请重新登录
                MyLoginUtil.logout(context);
                return;
            }
        }
    }

    //--------------------------------------------------新的：start---------------------------------------------------------------
    private void doNewMessage(List<MsgBean.MsgItemBean> msgList){
        try {
            for (MsgBean.MsgItemBean msgItemBean :msgList) {
                if (!"35".equals(msgItemBean.getTp())) {
                    toNewDB(msgItemBean);
                }
            }
        }catch (Exception e){}
    }
    /** 聊天记录根据聊天类型保存到不同的表 */
    private void toNewDB(MsgBean.MsgItemBean msgItemBean) {
        try {
            //bankuai:(1.系统通知；2.审批;3.易办事；4.拜访查询-评论；10.日志-工作汇报；11.商城）
            String tp = msgItemBean.getTp();
            switch (Integer.valueOf(tp)) {
                case 16:// 邀请同事加入部门
                case 18:// 邀请同事加入公司
                case 41:// 转让客户
                case 42:// 转让客户
                case 26:// 申请加入公司
                case 27:// 同意或不同意公司请求
                case 28:// 被移除公司的通知消息
                case 32:// 计划拜访
                    // 以上类型都所属系统通知的未读消息
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_XTTZ, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 31:// 31审批
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_SP, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 10:// 新建任务跟任务催办推送
                case 11:// 系统根据任务设置的提醒推送
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_YBS, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 43:// 拜访查询--评论
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_PL, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 34:// 日志(工作汇报)
                case 40:// 日志-日报，周报，月报评论
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_GZHB, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 100:// 商城订单
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_SC, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 12:// 企业公告
                case 13:// 系统公告
                    toNewMsg(msgItemBean, ConstantUtils.Messeage.M_GG, String.valueOf(msgItemBean.getMemberId()));
                    break;
                case 30:// 版本升级
                    Intent intent1 = new Intent();
                    intent1.setAction(Constans.Action_versonup);
                    MyApp.getI().sendBroadcast(intent1);
                    break;
            }
        }catch (Exception e){
        }
    }

    /**
     */
    private void toNewMsg(MsgBean.MsgItemBean msgItemBean, int bangkuai, String mark) {
        try {
            //bankuai:(1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城）
            Intent intent = new Intent();
            intent.setAction(Constans.whitchbankuai);
            intent.putExtra("bankuai", bangkuai);
            MyApp.getI().sendBroadcast(intent);

            // 保存消息的列表
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            Integer isNeedClean = 0;
            Integer	isact = 0;
            String tp = msgItemBean.getTp();
            Integer mainId = msgItemBean.getId();

            Integer memberId = msgItemBean.getMemberId();
            String memberName = msgItemBean.getMemberNm();
            Integer belongId = msgItemBean.getBelongId();
            String belongNm = msgItemBean.getBelongNm();
            String belongMsg = msgItemBean.getBelongMsg();

            String addTime = msgItemBean.getAddtime();
            String msgTp = msgItemBean.getMsgTp();
            String msg = msgItemBean.getMsg();
            String isRead = "0";
            String memberHead = msgItemBean.getMemberHead();
            //bangkuai,mark:在参数
            switch (Integer.valueOf(msgItemBean.getTp())) {
                case 8:
                case 16:
                case 18:
                case 19:// 19好友申请
                case 41:// 转让客户
                case 42:// 转让客户
                case 26:
                    isNeedClean = 1;
                    break;
            }

            //根据消息id查询是否存在
            DMessageBean messageBean = MyDataUtils.getInstance().queryMessageById(""+bangkuai,tp,mark,""+belongId,""+mainId);
            if(null == messageBean){
                messageBean = new DMessageBean();
            }
//            DMessagBean messageBean = new DMessagBean();
            messageBean.setCompanyId(companyId);
            messageBean.setUserId(userId);
            messageBean.setBankuai(bangkuai);
            messageBean.setIsNeedClean(isNeedClean);
            messageBean.setIsact(isact);
            messageBean.setIsRead(isRead);
            messageBean.setType(tp);
            messageBean.setMemberId(memberId);
            messageBean.setMemberName(memberName);
            messageBean.setBelongId(belongId);
            messageBean.setBelongName(belongNm);
            messageBean.setBelongMsg(belongMsg);
            messageBean.setAddTime(addTime);
            messageBean.setMsgTp(msgTp);
            messageBean.setAddTime(addTime);
            messageBean.setMsg(msg);
            messageBean.setMainId(msgItemBean.getId());//id
            messageBean.setMsgId(msgItemBean.getMsgId());//消息id
            messageBean.setReceiveId(Integer.valueOf(userId));
            messageBean.setMemberHead(memberHead);
            messageBean.setMark(mark);
            MyDataUtils.getInstance().saveMessage(messageBean);

            //保存分类的消息
            DCategoryMessageBean categoryMessagBean = new DCategoryMessageBean();
            categoryMessagBean.setCompanyId(companyId);
            categoryMessagBean.setUserId(userId);
            categoryMessagBean.setBankuai(bangkuai);
            MyDataUtils.getInstance().saveCategroyMessage(categoryMessagBean);
        }catch (Exception e){
        }

    }

    //--------------------------------------------------新的：end---------------------------------------------------------------


    //--------------------------------------------------旧的：start---------------------------------------------------------------

    private void doOldMessage(List<MsgBean.MsgItemBean> msgList){
        try {
            for (MsgBean.MsgItemBean msgItemBean :msgList) {
                if (!"35".equals(msgItemBean.getTp())) {
                    toOldDB(msgItemBean);
                }
            }
        }catch (Exception e){}
    }

    /** 聊天记录根据聊天类型保存到不同的表 */
    private void toOldDB(MsgBean.MsgItemBean msgItemBean) {
        try{

        }catch (Exception e){

        }
        // 保存记录到数据库
        Intent intent = new Intent();
        intent.putExtra("chatBean", msgItemBean);
        String tp = msgItemBean.getTp();
        if ("27".equals(tp)) {
            // 该条未读消息是公司申请经过审批（已同意或拒绝）
            String belongNm = msgItemBean.getBelongNm();
            if (!MyUtils.isEmptyString(belongNm)) {
                SPUtils.setValues("datasource", belongNm);
                Intent intent1 = new Intent();
                intent1.setAction(Constans.Action_getcompanyInfo);
                MyApp.getI().sendBroadcast(intent1);
            } else {
                SPUtils.setValues("datasource", "");
            }
        }

        if ("30".equals(tp)) {
            // 版本升级
            Intent intent1 = new Intent();
            intent1.setAction(Constans.Action_versonup);
            MyApp.getI().sendBroadcast(intent1);
        }
        if ("28".equals(tp)) {
            // 该条未读消息是被移除公司
            SPUtils.setValues("datasource", "");
            return;
        }

        ContentValues values = new ContentValues();
        String tabelName = null;
        switch (Integer.valueOf(tp)) {
            // 根据tp类型把未读消息设置到位置
            // 消息类型：1 添加圈主题 2 圈评论 3 圈点赞 4 添加照片墙
            // 5 照片墙评论 6 照片墙点赞 7 @人 8 加入圈请求 9 员工圈聊天
            // 10催办 11进度 12 企业公告 13 系统公告 14 部门聊天 15 私信聊
            // 16 邀请同事加入公司17真心话（全公司聊天）18邀请到部门19好友申请
            // 20 同意或不同意好友请求 21 同意或不同意组织(公司)邀请 22 群主同意或不同意成员加入圈
            // 23 成员退出圈通知群主 24 群主解散圈通知圈成员 25 群主或管理员剔除圈成员26 申请加入公司 27 同意或不同意公司请求
            // 28被移除公司的通知消息29购开心30版本跟新推送31审批32计划拜访
            case 8:// 加入圈请求
            case 16:// 邀请同事加入部门
            case 18:// 16 邀请同事加入公司
            case 23:// 23 成员退出圈通知群主
            case 19:// 19好友申请
            case 41:// 转让客户
            case 42:// 转让客户
            case 20:// 同意或不同意好友请求
            case 21:// 同意或不同意组织申请
            case 22:// 群主同意或不同意成员加入圈
            case 24:// 24 群主解散圈通知圈成员
            case 25:// 25 群主或管理员剔除圈成员
            case 26:// 26 申请加入公司
            case 27:// 同意或不同意公司请求
            case 28:// 被移除公司的通知消息
            case 13:// 系统公告
            case 12:// 企业公告
            case 29:// 购开心
            case 32:// 计划拜访
            case 34:// 日志
            case 40:// 日志-日报，周报，月报评论
            case 100:// 商城订单
                // 以上类型都所属系统通知的未读消息
                toOldMsg(msgItemBean, 1, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 31:// 31审批
                toOldMsg(msgItemBean, 2, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 10:// 新建任务跟任务催办推送
            case 11:// 系统根据任务设置的提醒推送
                toOldMsg(msgItemBean, 3, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 1:// 添加圈主题
            case 2:// 圈评论
            case 3:// 圈点赞
            case 43:// 拜访查询--评论
                toOldMsg(msgItemBean, 4, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 14:// 部门聊天 用部门id作为mark
                toOldMsg(msgItemBean, 6, String.valueOf(msgItemBean.getBelongId()));
                intent.putExtra("type", String.valueOf(tp));
                intent.putExtra("receiveid", String.valueOf(msgItemBean.getBelongId()));
                tabelName = "bumenmsg";
                break;
            case 15:// 私信 用对方id+自己id 作为mark
                toOldMsg(msgItemBean, 6, String.valueOf(msgItemBean.getMemberId() + SPUtils.getSValues("memId")));
                intent.putExtra("type", String.valueOf(tp));
                intent.putExtra("receiveid", String.valueOf(msgItemBean.getMemberId()));
                // 用mark去标记与不同用户的聊天
                values.put("mark", String.valueOf(msgItemBean.getMemberId()) + SPUtils.getSValues("memId"));
                tabelName = "mymsg";
                break;
            case 9:// 圈聊天 用belongid 作为区分
                toOldMsg(msgItemBean, 6, String.valueOf(msgItemBean.getBelongId()));
                intent.putExtra("type", String.valueOf(tp));
                intent.putExtra("receiveid", String.valueOf(msgItemBean.getBelongId()));
                tabelName = "quanmsg";
                break;
            case 17:// 真心话（全公司聊天）
                intent.putExtra("type", String.valueOf(tp));
                intent.putExtra("receiveid", SPUtils.getSValues("datasource"));
                values.put("belongname", SPUtils.getSValues("datasource"));
                tabelName = "gognsimsg";
                toOldMsg(msgItemBean, 7, String.valueOf(msgItemBean.getBelongId()));

                Intent intent2 = new Intent();
                intent2.setAction(Constans.whitchbankuai);
                intent2.putExtra("bankuai", 5);
               MyApp.getI(). sendBroadcast(intent2);
                break;
        }
        intent.setAction(Constans.chatMsg);
        MyApp.getI(). sendBroadcast(intent);
        // 保存到数据库
        if (!MyUtils.isEmptyString(tabelName)) {
            values.put("membername", msgItemBean.getMemberNm());
            values.put("headurl", msgItemBean.getMemberHead());
            values.put("userid", SPUtils.getSValues(ConstantUtils.Sp.USER_ID));
            values.put("longitude", msgItemBean.getLongitude());
            values.put("latitude", msgItemBean.getLatitude());
            values.put("memberid", msgItemBean.getMemberId());
            values.put("belongid", msgItemBean.getBelongId());
            values.put("belongMsg", msgItemBean.getBelongMsg());
            values.put("msgtype", msgItemBean.getMsgTp());
            values.put("type", tp);
            values.put("msg", msgItemBean.getMsg());
            values.put("msgId", msgItemBean.getMsgId());
            values.put("voicetime", msgItemBean.getVoiceTime());
            values.put("addtime", msgItemBean.getAddtime());// 时间 排序用
            MyUtils.getDB().insert(tabelName, null, values);
        }
    }

    /**
     * 系统通知的未读消息（表名 ur_sysnotify）
     * 保存未读消息列表只需保存最后一条消息
     *  mark--用来区分该条未读消息
     *  blbankuai(所属板块): 1系统通知2审批3易办事4评论赞6圈聊天7真心话
     */
    private void toOldMsg(MsgBean.MsgItemBean msgItemBean, int bangkuai, String mark) {
        try{
           // 未读消息按时间排序红点计算
            UnReadNumManager.getI().addNumberToTime(msgItemBean.getTp() + mark, msgItemBean.getMsg());
            // 未读消息界面的分板块的红点计算
            UnReadNumManager.getI().addNumberToBK("bangkuai" + bangkuai, msgItemBean.getMsg(), msgItemBean.getAddtime());

            Intent intent = new Intent();
            intent.setAction(Constans.whitchbankuai);
            intent.putExtra("bankuai", bangkuai);
            MyApp.getI().sendBroadcast(intent);

            // 保存消息的列表
            String tp = msgItemBean.getTp();
            String msg = msgItemBean.getMsg();
            String msgtp = msgItemBean.getMsgTp();
            String addtime = msgItemBean.getAddtime();
            String belongMsg = msgItemBean.getBelongMsg();
            String belongNm = msgItemBean.getBelongNm();
            int belongid = msgItemBean.getBelongId();
            String membername = msgItemBean.getMemberNm();
            int memberId = msgItemBean.getMemberId();
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);

            SQLiteDatabase mDB = MyApp.getDB();

            //类型："41"和"42"为转让客户，有多条数据，其他为单条数据
            if("41".equals(tp) || "42".equals(tp)){
                Cursor query = mDB.query("ur_sysnotify", null, "mark=? and userid=? and type=? and blbankuai=? and memberid=? and belongId=? and companyId=?",
                        new String[] { mark, userId, tp, String.valueOf(bangkuai), String.valueOf(memberId), String.valueOf(belongid),SPUtils.getCompanyId() }, null, null, null);
                boolean moveToFirst = query.moveToFirst();
                if(moveToFirst){
                    String sql="UPDATE ur_sysnotify SET isneedclean=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
                    mDB.execSQL(sql,new String[] { "0", tp, String.valueOf(mark), userId, String.valueOf(bangkuai),SPUtils.getCompanyId() });
                }else{
                    insert_sql(msgItemBean, bangkuai, mark, tp, msg, msgtp, addtime, mDB, userId);
                }
            }else{
                Cursor query = mDB.query("ur_sysnotify", null, "mark=? and userid=? and type=? and blbankuai=? and companyId=?",
                        new String[] { mark, userId, tp, String.valueOf(bangkuai) ,SPUtils.getCompanyId()}, null, null, null);
                boolean moveToFirst = query.moveToFirst();
                if (moveToFirst) {
                    // 修改
                    update_sql(msgItemBean, bangkuai, mark, tp, msg, msgtp, addtime, belongMsg, belongNm, belongid,membername, memberId, mDB, userId);
                } else {
                    // 插入
                    insert_sql(msgItemBean, bangkuai, mark, tp, msg, msgtp, addtime, mDB, userId);
                }
                query.close();
            }
        }catch (Exception e){

        }

    }

    //修改数据
    private void update_sql(MsgBean.MsgItemBean msgItemBean, int bangkuai, String mark, String tp, String msg, String msgtp,
                            String addtime, String belongMsg, String belongNm, int belongid, String membername, int memberId,
                            SQLiteDatabase mDB, String sValues) {
        try{
            String sql_1="UPDATE ur_sysnotify SET lastmsg=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_2="UPDATE ur_sysnotify SET msgtp=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_3="UPDATE ur_sysnotify SET addtime=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_4="UPDATE ur_sysnotify SET belongMsg=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_5="UPDATE ur_sysnotify SET belongname=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_6="UPDATE ur_sysnotify SET belongId=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_7="UPDATE ur_sysnotify SET memberid=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_8="UPDATE ur_sysnotify SET membername=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_9="UPDATE ur_sysnotify SET headurl=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_10="UPDATE ur_sysnotify SET isread=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            String sql_11="UPDATE ur_sysnotify SET isact=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
            mDB.execSQL(sql_1, new String[] { msg, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_2,new String[] { msgtp, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai) ,SPUtils.getCompanyId()});
            mDB.execSQL(sql_3,new String[] { addtime, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_4,new String[] { belongMsg, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_5,new String[] { belongNm, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai) ,SPUtils.getCompanyId()});
            mDB.execSQL(sql_6,new String[] { String.valueOf(belongid), tp, String.valueOf(mark), sValues,String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_7,new String[] { String.valueOf(memberId), tp, String.valueOf(mark), sValues,String.valueOf(bangkuai) ,SPUtils.getCompanyId()});
            mDB.execSQL(sql_8,new String[] { membername, tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_9,new String[] { msgItemBean.getMemberHead(), tp, String.valueOf(mark), sValues,String.valueOf(bangkuai),SPUtils.getCompanyId() });
            mDB.execSQL(sql_10,new String[] { String.valueOf(0), tp, String.valueOf(mark), sValues,String.valueOf(bangkuai) ,SPUtils.getCompanyId()});
            mDB.execSQL(sql_11,new String[] { "0", tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
            switch (Integer.valueOf(msgItemBean.getTp())) {
                case 8:
                case 16:
                case 18:
                case 26:
                    String sql_12="UPDATE ur_sysnotify SET isneedclean=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
                    mDB.execSQL(sql_12,new String[] { "1", tp, String.valueOf(mark), sValues, String.valueOf(bangkuai),SPUtils.getCompanyId() });
                    break;
                default:
                    String sql_13="UPDATE ur_sysnotify SET isneedclean=?  WHERE type=? and mark=? and userid=? and blbankuai=? and companyId=?";
                    mDB.execSQL(sql_13,new String[] { "0", tp, String.valueOf(mark), sValues, String.valueOf(bangkuai) ,SPUtils.getCompanyId()});
                    break;
            }
        }catch (Exception e){

        }

    }

    // 插入数据
    private void insert_sql(MsgBean.MsgItemBean msgItemBean, int bangkuai, String mark, String tp, String msg, String msgtp,
                            String addtime, SQLiteDatabase mDB, String sValues) {
        try{
            ContentValues values = new ContentValues();
            switch (Integer.valueOf(msgItemBean.getTp())) {
                case 8:
                case 16:
                case 18:
                case 19:// 19好友申请
                case 41:// 转让客户
                case 42:// 转让客户
                case 26:
                    values.put("isneedclean", "1");
                    break;
                default:
                    values.put("isneedclean", "0");
                    break;
            }
            values.put("lastmsg", msg);
            values.put("mark", mark);
            values.put("membername", msgItemBean.getMemberNm());
            values.put("msgtp", msgtp);
            values.put("blbankuai", bangkuai);
            values.put("isread", 0);
            values.put("belongId", msgItemBean.getBelongId());
            values.put("belongname", msgItemBean.getBelongNm());
            values.put("belongMsg", msgItemBean.getBelongMsg());
            values.put("headurl", msgItemBean.getMemberHead());
            values.put("memberid", msgItemBean.getMemberId());
            values.put("userid", sValues);
            values.put("companyId", SPUtils.getCompanyId());
            values.put("type", tp);
            values.put("isact", "0");
            values.put("msgtp", msgtp);
            values.put("addtime", addtime);// 时间 排序用
            mDB.insert("ur_sysnotify", null, values);
        }catch (Exception e){

        }

    }

    //--------------------------------------------------旧的：end---------------------------------------------------------------

}
