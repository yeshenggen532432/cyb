package com.qwb.view.tab.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyAppUtil;
import com.qwb.view.base.model.VersionBean;
import com.qwb.view.base.model.VersionResult;
import com.qwb.view.tab.model.MsgBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DMessageBean;
import com.qwb.view.tab.ui.XTabActivity;
import com.qwb.utils.MyPublicSaveMessageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 首页
 */
public class PXTab extends XPresent<XTabActivity>{

    /**
     * 未读
     */
    public void loadDataUnRead(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils.post()
                .params(params)
                .url(Constans.unReadURL)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 获取版本更新
     */
    public void queryDataUpdateVersion() {
        // 0:测试版本 4：Android发布版本
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("verSion", "2");
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateVerSionURL)
                .id(2)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }



    //TODO ------------------------接口回調----------------------
    //解析数据-登录
    private void parseJson1(String response) {
        try {
            MsgBean msgBean = JSON.parseObject(response, MsgBean.class);
            if (msgBean !=null && msgBean.isState()) {
                //新旧数据库放同一处理
                MyPublicSaveMessageUtil.getInstance().doPublicSaveMessage(msgBean, null);
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }



    //解析数据-版本更新
    private void parseJson2(String response) {
        try {
            VersionResult result = JSON.parseObject(response, VersionResult.class);
            if (result != null && result.isState()){
                VersionBean bean = result.getVersion();
                String isQz = bean.getIsQz();// 是否强制更新
                String versionUrl = bean.getVersionUrl();// 下载地址
                String versionContent = bean.getVersionContent();// 更新内容
                Integer serviceVersionNo = bean.getAppNo();

                int appVersionNo = MyAppUtil.getAppVersionNo();// 获取app当前版本号
                if (serviceVersionNo != null && serviceVersionNo > appVersionNo){
                    if (MyAppUtil.isShowUpdateVersion(serviceVersionNo)){
                        getV().setVersionUpdate(isQz, versionUrl, versionContent);
                        MyAppUtil.saveVersionNo(serviceVersionNo);
                    }
                }
            }

//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject != null && jsonObject.getBoolean("state")) {
//
//                String appVersion = OtherUtils.getAppVersion();// 获取app当前版本号
//                String replace_appVersion = appVersion.replace(".", ",");
//                String[] split_appVersion = replace_appVersion.split(",");// String.split(".")不能转化为数组
//
//                double appVersion_1 = Double.valueOf(split_appVersion[0]);
//                double appVersion_2 = Double.valueOf(split_appVersion[1]);
//                double appVersion_3 = Double.valueOf(split_appVersion[2]);
//
//                JSONObject version = jsonObject.getJSONObject("version");
//
//                // 服务器的更新版本号
//                String serviceVersion = version.getString("versionName");
//                String replace_serviceVersion = serviceVersion.replace(".", ",");
//                String[] split_serviceVersion = replace_serviceVersion.split(",");
//                double serviceVersion_1=0;
//                double serviceVersion_2=0;
//                double serviceVersion_3=0;
//                try{
//                    serviceVersion_1 = Double.valueOf(split_serviceVersion[0]);
//                    serviceVersion_2 = Double.valueOf(split_serviceVersion[1]);
//                    serviceVersion_3 = Double.valueOf(split_serviceVersion[2]);
//                }catch (Exception e){
//                    ToastUtils.showCustomToast("版本号格式错误");
//                }
//
//                String isQz = version.getString("isQz");// 是否强制更新
//                String versionUrl = version.getString("versionUrl");// 下载地址
//                String versionContent = version.getString("versionContent");// 更新内容
//
//                // @注意：版本格式为例如：0.0.0（两个“.”）； “0.0.0”分解成：0，0，0；
//                // 先判断第一个后台版本大于当前版本弹出窗体（这边要强制更新，具体看参数isQz），相等或不于判断第二个
//                // 第二个后台版本大于当前版本弹出窗体（这边要强制更新，具体看参数isQz），相等或不于判断第三个（这边要不强制更新，具体看参数isQz）
//                // 第一层
//                if (serviceVersion_1 > appVersion_1) {// 强
//                    if(getV()!=null){
//                        getV().setVersionUpdate(isQz, versionUrl, versionContent);
//                    }
//                } else {// 一般相等
//                    // 第二层
//                    if (serviceVersion_2 > appVersion_2) {// 强
//                        if(getV()!=null){
//                            getV().setVersionUpdate(isQz, versionUrl, versionContent);
//                        }
//                    } else if (serviceVersion_2 == appVersion_2) {
//                        // 第三层
//                        if (serviceVersion_3 > appVersion_3) {// 弹出窗体
//                            if(getV()!=null){
//                                getV().setVersionUpdate(isQz, versionUrl, versionContent);
//                            }
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
//            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    /** 聊天记录根据聊天类型保存到不同的表 */
    private void toDB(MsgBean.MsgItemBean msgItemBean) {

        String tp = msgItemBean.getTp();
        switch (Integer.valueOf(tp)) {
            case 16:// 邀请同事加入部门
            case 18:// 邀请同事加入公司
            case 41:// 转让客户
            case 42:// 转让客户
            case 26:// 申请加入公司
            case 27:// 同意或不同意公司请求
            case 28:// 被移除公司的通知消息
            case 13:// 系统公告
            case 12:// 企业公告
            case 32:// 计划拜访
                // 以上类型都所属系统通知的未读消息
                toLastMsg(msgItemBean, 1, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 31:// 31审批
                toLastMsg(msgItemBean, 2, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 10:// 新建任务跟任务催办推送
            case 11:// 系统根据任务设置的提醒推送
                toLastMsg(msgItemBean, 3, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 43:// 拜访查询--评论
                toLastMsg(msgItemBean, 4, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 34:// 日志
            case 40:// 日志-日报，周报，月报评论
                toLastMsg(msgItemBean, 10, String.valueOf(msgItemBean.getMemberId()));
                break;
            case 100:// 商城订单
                toLastMsg(msgItemBean, 11, String.valueOf(msgItemBean.getMemberId()));
                break;
        }
    }


    /**
     * 系统通知的未读消息（表名 ur_sysnotify）
     * 保存未读消息列表只需保存最后一条消息
     *  mark--用来区分该条未读消息
     *  blbankuai(所属板块): 1系统通知2审批3易办事4评论赞6圈聊天7真心话
     */
    private void toLastMsg(MsgBean.MsgItemBean msgItemBean, int bangkuai, String mark) {
        // 保存消息的列表
        String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
        String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
        Integer isNeedClean = 0;
        Integer	isact = 0;
        String tp = msgItemBean.getTp();

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

        DMessageBean messageBean = new DMessageBean();
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

    }


}
