package com.qwb.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Map;


/**
 * Service:工具类
 * 1  isServiceRunning：判断服务是否运行
 * 2  startService：启动服务
 * 3  stopService：停止服务
 */

public class MyServiceUtil {

    /**
     * 判断服务是否运行
     * @param className 完整包名的服务类名
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> infos = activityManager.getRunningServices(0x7FFFFFFF);//0x7FFFFFFF最大的整型数 int
        if (infos == null || infos.size() == 0) return false;
        for (RunningServiceInfo info : infos) {
            if (className.equals(info.service.getClassName())) return true;
        }
        return false;
    }

    /**
     * 启动服务
     */
    public static void startService(Context context, Class c) {
        try {
            Intent intent = new Intent(context, c);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务
     */
    public static void startService(Context context, Class c, Map<String, Object> map) {
        try {
            Intent intent = new Intent(context, c);
            //遍历map
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if(value instanceof Integer){
                    intent.putExtra(key, (int)value);
                }else if(value instanceof String){
                    intent.putExtra(key, (String) value);
                }
                //..............如果还有...........instanceof
            }
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 停止服务
     */
    public static boolean stopService(Context context, Class c) {
        try {
            Intent intent = new Intent(context, c);
            return context.stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

