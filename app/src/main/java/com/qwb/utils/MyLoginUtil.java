package com.qwb.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.qwb.view.mine.model.UserManagerBean;
import com.qwb.view.base.model.LoginBean;
import com.qwb.utils.push.MyPushUtil;
import com.qwb.db.PassWordBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.view.base.ui.XLoginActivity;
import com.qwb.view.base.model.ApplyBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *  登录相关的一些工具
 */

public class MyLoginUtil {

    /**
     * 退出登录把一些数据重置
     */
    public static void logout(Context context){
        try {
            if(null != context){
                ActivityManager.getInstance().jumpToLoginActivity(context, 0);
                ActivityManager.getInstance().finishAllActivityExceptOne(XLoginActivity.class);
            }

            SPUtils.setBoolean(ConstantUtils.Sp.IS_LOGIN, false);
            SPUtils.setValues(ConstantUtils.Sp.TOKEN, null);
            SPUtils.setValues(ConstantUtils.Sp.USER_ID, null);//用户id
            SPUtils.setValues(ConstantUtils.Sp.USER_HEAD, null);//用户头像
            SPUtils.setValues(ConstantUtils.Sp.USER_NAME, null);//用户名称
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_ID, null);// 公司id
            SPUtils.setValues(ConstantUtils.Sp.MSG_MODEL, null);
            // 成员类型:0.普通成员1.单位超级管理员 2.单位管理员 3.部门管理员
            SPUtils.setValues(ConstantUtils.Sp.IS_CREAT, null);
            SPUtils.setValues(ConstantUtils.Sp.IS_UNITMNG, null);
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_TYPE, null);// 公司类型
            SPUtils.setValues(ConstantUtils.Sp.DATA_SOURCE, null);//数据库
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_S, null);//所属公司s
            SPUtils.setValues(ConstantUtils.Sp.LOGIN_BASE_URL, null);//独立域名
            SPUtils.setValues(ConstantUtils.Sp.LOGIN_JWT, null);//独立域名
            //手机号和密码用于登录显示
//      SPUtils.setValues(ConstantUtils.Sp.USER_MOBILE, null);//手机号（账号）
//      SPUtils.setValues(ConstantUtils.Sp.PASSWORD, null);//密码
            //设置别名为""
            MyPushUtil.setAlias("");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


//                {
//                        "msg": "登录成功",
//                        "isUnitmng": "",
//                        "msgmodel": "2",
//                        "tpNm": "快消",
//                        "token": "6cd2e35aac15b907093a1652cc26ed83",
//                        "companyId": 285,
//                        "memberNm": "叶生根",
//                        "memberHead": "publicplat/member/1524747171026.jpg",
//                        "rzState": 0,
//                        "datasource": "xmqwbwlkjyxgs285",
//                        "companys": "[{\"companyId\":590,\"companyName\":\"小帅集团\"},{\"companyId\":139,\"companyName\":\"鹭百川\"},{\"companyId\":285,\"companyName\":\"厦门企微宝网络科技有限公司\"},{\"companyId\":137,\"companyName\":\"思迅网络科技\"}]",
//                        "state": true,
//                        "memberMobile": "13950104779",
//                        "memId": 195,
//                        "cid": 0
//                }

    public static void login(LoginBean loginBean,String pwd,boolean check){
        try {
            SPUtils.setBoolean(ConstantUtils.Sp.INIT_PASSWORD, true);
            SPUtils.setBoolean(ConstantUtils.Sp.RZ_STATE, true);
            //手机认证rzStat 0未认证；1已认证
            SPUtils.setBoolean(ConstantUtils.Sp.IS_LOGIN, true);
            Integer rzState = loginBean.getRzState();
            if(rzState != null && rzState == 0) {
                if(Constans.ISRZ){
                    SPUtils.setBoolean(ConstantUtils.Sp.IS_LOGIN, false);
                }
            }
            SPUtils.setValues(ConstantUtils.Sp.TOKEN, loginBean.getToken());
            SPUtils.setValues(ConstantUtils.Sp.USER_ID, String.valueOf(loginBean.getMemId()));//用户id
            SPUtils.setValues(ConstantUtils.Sp.USER_HEAD, loginBean.getMemberHead());//用户头像
            SPUtils.setValues(ConstantUtils.Sp.USER_NAME, loginBean.getMemberNm());//用户名称
            SPUtils.setValues(ConstantUtils.Sp.USER_MOBILE, loginBean.getMemberMobile());//手机号（账号）
            SPUtils.setValues(ConstantUtils.Sp.PASSWORD, pwd);//密码
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_ID, String.valueOf(loginBean.getCompanyId()));// 公司id
//            SPUtils.setValues(ConstantUtils.Sp.MSG_MODEL, loginBean.getMsgmodel());//msgmodel--1:没启用分类，2：启用分类
            // 成员类型:0.普通成员1.单位超级管理员 2.单位管理员 3.部门管理员
            SPUtils.setValues(ConstantUtils.Sp.IS_CREAT, loginBean.getIsUnitmng());
            SPUtils.setValues(ConstantUtils.Sp.IS_UNITMNG, loginBean.getIsUnitmng());
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_TYPE, loginBean.getTpNm());// 公司类型
            SPUtils.setValues(ConstantUtils.Sp.DATA_SOURCE, loginBean.getDatasource());//数据库
            SPUtils.setValues(ConstantUtils.Sp.COMPANY_S, loginBean.getCompanys());//所属公司s

            //极光设置别名
            MyPushUtil.setAlias(loginBean.getMemberMobile());
            //添加账户
            addUserManager(loginBean,pwd);
            //保存密码
            savePwd(loginBean, pwd, check);
            //切换公司时，把通过sp保存的数据，相应的key也改变
            doChangeKey();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void login(String jwt, String domain){
        try {
            SPUtils.setValues(ConstantUtils.Sp.LOGIN_BASE_URL, domain);//独立域名
            SPUtils.setValues(ConstantUtils.Sp.LOGIN_JWT, jwt);//独立域名
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加用户管理（添加账号）
     */
    public static void addUserManager(LoginBean parseObject,String pwd) {
        int  userId=parseObject.getMemId();
        String userName=parseObject.getMemberNm();
        String phone=parseObject.getMemberMobile();
        String userHead=parseObject.getMemberHead();

        UserManagerBean bean = new UserManagerBean();
        bean.setMid(String.valueOf(userId));
        bean.setName(userName);
        bean.setPhone(phone);
        bean.setPwd(pwd);
        bean.setMemberHead(userHead);
        ArrayList<UserManagerBean> zhglList = new ArrayList<>();
        zhglList.clear();

        String zhanghaoguanli = SPUtils.getSValues(ConstantUtils.Sp.USER_MANAGER);
        if (!MyUtils.isEmptyString(zhanghaoguanli)) {
            List<UserManagerBean> parseArray = JSON.parseArray(zhanghaoguanli, UserManagerBean.class);
            if (parseArray != null) {
                //先加原来有的，再判断新的是否已经包含--要修改，没有添加
                zhglList.addAll(parseArray);
                for (int i = 0; i < zhglList.size(); i++) {
                    UserManagerBean zhanghaoglBean = zhglList.get(i);
                    String mid2 = zhanghaoglBean.getMid();
                    if (String.valueOf(userId).equals(mid2)) {
                        zhglList.set(i, bean);
                        return;
                    }
                }
                zhglList.add(bean);
            }
        } else {
            zhglList.add(bean);
        }
        String jsonString = JSON.toJSONString(zhglList);
        SPUtils.setValues(ConstantUtils.Sp.USER_MANAGER, jsonString);
    }

    //记住密码：保存到数据库
    private static void savePwd(LoginBean loginBean,String pwd, boolean check){
        try {
            if(!TextUtils.isEmpty(pwd)){
                PassWordBean bean = new PassWordBean();
                bean.setCheck(check);
                bean.setPhone(loginBean.getMemberMobile());
                bean.setUserId(loginBean.getMemId());
                bean.setCompanyId(loginBean.getCompanyId());
                bean.setPwd(pwd);
                MyDataUtils.getInstance().savePassWord(bean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //修改密码和用户名
    public static void updateNameAndPwd(LoginBean loginBean,String pwd, boolean check){
        try {
            SPUtils.setValues(ConstantUtils.Sp.USER_NAME, loginBean.getMemberNm());//用户名称
            SPUtils.setValues(ConstantUtils.Sp.PASSWORD, pwd);//密码
            savePwd(loginBean,pwd,check);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理应用列表
     */
    public static void doApplyList(RoleBean bean) {
        try {
            SPUtils.setBoolean(ConstantUtils.Sp.IS_ROLE, false);

            List<ApplyBean> applyList = bean.getApplyList();
            List<ApplyBean> qwbList = new ArrayList<>();
            List<ApplyBean> qwfwList = new ArrayList<>();
            if(null != applyList && !applyList.isEmpty()){
                for (ApplyBean apply : applyList) {
                    String menuNm = apply.getMenuNm();
                    if ("qwb".equals(menuNm)) {
                        qwbList.add(apply);
                    } else if ("qwfw".equals(menuNm)) {
                        qwfwList.add(apply);
                    }
                }
                String oldApplyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST);
                if(TextUtils.isEmpty(oldApplyStr) || "[]".equals(oldApplyStr)){
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST, JSON.toJSONString(applyList));
                }else{
                    List<ApplyBean> oldApplyList = JSON.parseArray(oldApplyStr,ApplyBean.class);
//                    Collections.sort(applyList, new ApplyComparator());
//                    Collections.sort(oldApplyList, new ApplyComparator());

                    List<Integer> oldIds = new ArrayList<>();
                    for (ApplyBean oldApply:oldApplyList) {
                        oldIds.add(oldApply.getId());
                    }
                    List<ApplyBean> tempList = new ArrayList<>();
                    for (int i = 0; i < applyList.size(); i++){
                        ApplyBean apply = applyList.get(i);

                        if(oldIds.contains(apply.getId())){
                            for (int j = 0; j < oldApplyList.size(); j++){
                                ApplyBean oldApply = oldApplyList.get(j);
                                if(apply.getId() == oldApply.getId()){
                                    //新的对象修改属性值（sort，和isMe）后加入临时列表
                                    apply.setSort(oldApply.getSort());
                                    apply.setMe(oldApply.isMe());
                                    apply.setMeApplySort(oldApply.getMeApplySort());
                                    apply.setMeApply(oldApply.isMeApply());
                                    oldApplyList.set(j,apply);
                                    tempList.add(apply);
                                    break;
                                }
                            }
                        }else{
                            //没包含则添加到临时列表；排序顺序则最后一个数据+1；
                            apply.setSort((oldApplyList.get(oldApplyList.size()-1).getSort()) + 1);
                            apply.setMe(true);
                            apply.setMeApplySort((oldApplyList.get(oldApplyList.size()-1).getSort()) + 1);
                            apply.setMeApply(false);
                            tempList.add(apply);
                        }
                    }
                    //以临时列表更新数据（可以过滤多余的数据）
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST, JSON.toJSONString(tempList));
                }

                //加SPUtils.getID()：区分不同的账号
                SPUtils.setValues(ConstantUtils.Sp.APPLY_QWB_LIST, JSON.toJSONString(qwbList));
                SPUtils.setValues(ConstantUtils.Sp.APPLY_QWBFW_LIST, JSON.toJSONString(qwfwList));
            }else{
                SPUtils.setValues(ConstantUtils.Sp.APP_LIST, null);
                SPUtils.setValues(ConstantUtils.Sp.APPLY_QWB_LIST, null);
                SPUtils.setValues(ConstantUtils.Sp.APPLY_QWBFW_LIST, null);
            }
        }catch (Exception e){
        }

    }

    /**
     * 处理应用列表
     */
    public static void doApplyListNew(RoleBean bean) {
        try {
            SPUtils.setBoolean(ConstantUtils.Sp.IS_ROLE, false);

            List<ApplyBean> newApplyList = bean.getApplyList();
            List<ApplyBean> newChildren = new ArrayList<>();

            if(null != newApplyList && !newApplyList.isEmpty()){
                String oldApplyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_NEW);
                String oldChildStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);

                if(MyStringUtil.isEmpty(oldApplyStr) || "[]".equals(oldApplyStr)|| MyStringUtil.isEmpty(oldChildStr) || "[]".equals(oldChildStr)){
                    int count = 0;
                    for (ApplyBean parentBean : newApplyList){
                        List<ApplyBean> children = parentBean.getChildren();
                        for (ApplyBean childBean : children){
                            count++;
                            childBean.setMeApplySort(count);
                            newChildren.add(childBean);
                        }
                    }
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST_NEW, JSON.toJSONString(newApplyList));
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST_CHILDREN, JSON.toJSONString(newChildren));
                }else{
                    List<ApplyBean> oldChildren = JSON.parseArray(oldChildStr, ApplyBean.class);
//                    Collections.sort(newApplyList, new ApplyComparator());
//                    Collections.sort(oldApplyList, new ApplyComparator());
                    //1.先取出旧列表中第二层所有数据
                    //2.遍历新列表的第二层与第1步比较
                    for (ApplyBean newBean: newApplyList){
                        List<ApplyBean> children = newBean.getChildren();
                        for (ApplyBean childBean: children){
                            for (ApplyBean oldChildBean: oldChildren){
                                if(oldChildBean.getId() == childBean.getId()){
                                    //新的对象修改属性值（sort，和isMe）后加入临时列表
                                    childBean.setSort(oldChildBean.getSort());
                                    childBean.setMe(oldChildBean.isMe());
                                    childBean.setMeApplySort(oldChildBean.getMeApplySort());
                                    childBean.setMeApply(oldChildBean.isMeApply());
                                    break;
                                }
                            }
                            newChildren.add(childBean);
                        }
                    }
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST_NEW, JSON.toJSONString(newApplyList));
                    SPUtils.setValues(ConstantUtils.Sp.APP_LIST_CHILDREN, JSON.toJSONString(newChildren));
                }

                //加SPUtils.getID()：区分不同的账号
            }else{
                SPUtils.setValues(ConstantUtils.Sp.APP_LIST_NEW, null);
                SPUtils.setValues(ConstantUtils.Sp.APP_LIST_CHILDREN, null);
            }
        }catch (Exception e){
        }
    }

    /**
     * 切换公司时，把通过sp保存的数据，相应的key也改变
     */
    public static void doChangeKey(){
        //TODO 有使用BASE的要在MyLoginUtil中修改
        ConstantUtils.Sp.APP_LIST = "appList" + SPUtils.getID() + "_" + SPUtils.getCompanyId();
        ConstantUtils.Sp.APP_LIST_NEW = "appList_new" + SPUtils.getID() + "_" + SPUtils.getCompanyId();
        ConstantUtils.Sp.APP_LIST_CHILDREN = "appList_children" + SPUtils.getID() + "_" + SPUtils.getCompanyId();
        ConstantUtils.Sp.APP_LIST_CHILDREN_NORMAL = "appList_children_normal" + SPUtils.getID() + "_" + SPUtils.getCompanyId();//应用列表_子(快捷菜单默认)
        ConstantUtils.Sp.APPLY_QWB_LIST = "qwbList"+ SPUtils.getID() + "_" + SPUtils.getCompanyId();;//应用列表-企微宝-工作台
        ConstantUtils.Sp.APPLY_QWBFW_LIST = "qwfwList"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//应用列表-企微服务-微生活
        ConstantUtils.Sp.TRACK_UPDATE_TYPE = "track_update_type"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//轨迹上传类型
        ConstantUtils.Sp.FRAME_MEMBER_IDS_MAP = "memberIds_map"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//结构图（部门及员工）-拜访地图
        ConstantUtils.Sp.FRAME_BRANCH_IDS_MAP = "branchIds_map"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//结构图（部门及员工）-拜访地图
        ConstantUtils.Sp.WORK_STATE = "work_state"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//上班状态
        ConstantUtils.Sp.WORK_TIME = "work_time"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//上班状态
        ConstantUtils.Sp.CAR_DEFAULT_STORAGE = "car_default_storage"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//车销：默认仓库
        ConstantUtils.Sp.CAR_DEFAULT_STORAGE_NAME = "car_default_storage_name"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//车销：默认仓库
        ConstantUtils.Sp.STORAGE_ZERO = "storage_zero"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//车销：默认仓库
        ConstantUtils.Sp.TRACK_UPLOAD_STATUS = "track_upload_status"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//轨迹上传状态：0不上传；1上传
        ConstantUtils.Sp.TRACK_UPLOAD_MIN = "track_upload_min"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//轨迹上传间隔：默认1分钟
        ConstantUtils.Sp.OPEN_SMALL_PRICE = "open_small_price"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//开启开单小单位参考
        ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS = "tree_bfdt_member_branch_ids"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//开启开单小单位参考
        ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS_MAP = "tree_bfdt_member_branch_ids_map"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//开启开单小单位参考
        ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID = "normal_shop_company_id"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//首页tab "默认商城"
        ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME = "normal_shop_company_name"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//首页tab "默认商城"
        ConstantUtils.Sp.NORMAL_SHOP_COMPANY_URL = "normal_shop_company_url"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//首页tab "默认商城"
        ConstantUtils.Sp.CLEAR_NORMAL_SHOP_COMPANY = "clear_normal_shop_company"+SPUtils.getID() + "_" + SPUtils.getCompanyId();//清空默认商城
    }

    /**
     * 重新登录
     */
    public static void restartLogin(){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder params=new FormBody.Builder();
        String phone=SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE);
        final String pwd=SPUtils.getSValues(ConstantUtils.Sp.PASSWORD);
        params.add("mobile", phone);
        params.add("pwd", MD5.hex_md5(pwd));
        //创建一个请求对象
        Request request = new Request.Builder()
                .post(params.build())
                .url(Constans.loginURL)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if(!MyUtils.isEmptyString(json) && json.startsWith("{")){
                        LoginBean loginBean = JSON.parseObject(json, LoginBean.class);
                        if (loginBean != null && loginBean.isState()) {
                            SPUtils.setBoolean(ConstantUtils.Sp.INIT_PASSWORD, true);
                            //登录后保存数据
                            MyLoginUtil.login(loginBean, pwd, true);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call call, IOException response){
            }
        });
    }

    /**
     * 获取各个模块的应用dataTp
     */
    public static String getDataTp(boolean isApplyNew, String applyCodeNew, String applyCodeOld){
        String dataTp = "";
        if(isApplyNew){
            String applyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
            if(!MyStringUtil.isEmpty(applyStr)){
                List<ApplyBean> applyList = JSON.parseArray(applyStr, ApplyBean.class);
                for (ApplyBean bean :applyList){
                    if(applyCodeNew.equals(bean.getApplyCode())){
                        return bean.getDataTp();
                    }
                }
            }
        }else{
            String applyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST);
            if(!MyStringUtil.isEmpty(applyStr)){
                List<ApplyBean> applyList = JSON.parseArray(applyStr, ApplyBean.class);
                for (ApplyBean bean :applyList){
                    if(applyCodeOld.equals(bean.getApplyCode())){
                        return bean.getDataTp();
                    }
                }
            }
        }

        return dataTp;
    }

    /**
     * 获取各个模块的应用mids
     */
    public static String getDataTpMids(boolean isApplyNew, String applyCodeNew, String applyCodeOld){
        String mids = "";
        if(isApplyNew){
            String applyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
            if(!MyStringUtil.isEmpty(applyStr)){
                List<ApplyBean> applyList = JSON.parseArray(applyStr, ApplyBean.class);
                for (ApplyBean bean :applyList){
                    if(applyCodeNew.equals(bean.getApplyCode())){
                        return bean.getMids();
                    }
                }
            }
        }else{
            String applyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST);
            if(!MyStringUtil.isEmpty(applyStr)){
                List<ApplyBean> applyList = JSON.parseArray(applyStr, ApplyBean.class);
                for (ApplyBean bean :applyList){
                    if(applyCodeOld.equals(bean.getApplyCode())){
                        return bean.getMids();
                    }
                }
            }
        }

        return mids;
    }


    /**
     * 根据"应用代码"查询是否有此"菜单"（针对新的）
     */
    public static boolean getMenuByApplyCode(String applyCode){
        boolean flag = false;
        try{
            String applyStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_NEW);
            if(!MyStringUtil.isEmpty(applyStr)){
                List<ApplyBean> applyList = JSON.parseArray(applyStr, ApplyBean.class);
                //一
                if(applyList != null && applyList.size() > 0){
                    for (ApplyBean bean : applyList){
                        if(applyCode.equals(bean.getApplyCode())){
                            return true;
                        }else{
                            //二
                            List<ApplyBean> applyList2 = bean.getChildren();
                            if(applyList2 != null && applyList2.size() > 0){
                                for (ApplyBean bean2 : applyList2){
                                    if(applyCode.equals(bean2.getApplyCode())){
                                        return true;
                                    }else{
                                        //三
                                        List<ApplyBean> applyList3 = bean2.getChildren();
                                        if(applyList3 != null && applyList3.size() > 0){
                                            for (ApplyBean bean3 : applyList3){
                                                if(applyCode.equals(bean3.getApplyCode())){
                                                    return true;
                                                }else{
                                                    //四
                                                    List<ApplyBean> applyList4 = bean3.getChildren();
                                                    if(applyList4 != null && applyList4.size() > 0){
                                                        for (ApplyBean bean4 : applyList4){
                                                            if(applyCode.equals(bean4.getApplyCode())){
                                                                return true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        return flag;
    }

    /**
     * 获取密码
     */
    public static String getPwd(){
        String pwd = SPUtils.getSValues(ConstantUtils.Sp.PASSWORD);
        if (!MyStringUtil.isEmpty(pwd)){
            return pwd;
        }
      return "";
    }


}
