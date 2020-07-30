package com.qwb.utils;

import com.xdandroid.hellodaemon.DaemonEnv;
import com.qwb.view.longconnection.TraceServiceImpl;


/**
 * 上传缓存位置的工具类
 */

public class MyTraceServiceUtil {


    private static MyTraceServiceUtil instance = null;

    public static MyTraceServiceUtil getInstance() {
        if (instance == null) {
            instance = new MyTraceServiceUtil();
        }
        return instance;
    }

    public void startOrStopService(){
//        String workState = SPUtils.getSValues(ConstantUtils.Sp.WORK_STATE);//1:上班；2：下班
//        int upload = SPUtils.getIValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS);//0:不上传；1：上传
//        if(1 == upload && "1".equals(workState)){
//            TraceServiceImpl.sShouldStopService = false;
//            DaemonEnv.startServiceMayBind(TraceServiceImpl.class);
//            MyDataUtils.getInstance().loadCacheData();
//        }else{
//            TraceServiceImpl.stopService();//关闭上传
//        }
    }


}
