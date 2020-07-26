package com.qwb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.qwb.application.MyApp;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * 网络工具类
 */

public class MyNetWorkUtil {
    static String TAG = "MyNetWorkUtil";

    /**
     * 判定网络是否流通
     */
//    public static boolean isAvailable() {
//        boolean isMobile = NetWorkUtils.getMobileDataEnabled();
//        boolean isWifi = NetWorkUtils.getWifiEnabled();
//        boolean isConnect = NetWorkUtils.isConnect();
//        boolean isAvailable = NetWorkUtils.isAvailable();
//        boolean isAvailableByPing = NetWorkUtils.isAvailableByPing();
//        String baiduIp = NetWorkUtils.getDomainAddress("mp.qweib.com");
//
//        XLog.e("b","isMobile:" + isMobile);
//        XLog.e("b","isWifi:" + isWifi);
//        XLog.e("b","isConnect:" + isConnect);
//        XLog.e("b","isAvailable:" + isAvailable);
//        XLog.e("b","isAvailableByPing:" + isAvailableByPing);
//        XLog.e("b","baiduIp:" + baiduIp);


//        try {
//            boolean isMobile = NetWorkUtils.getMobileDataEnabled();
//            boolean isWifi = NetWorkUtils.getWifiEnabled();
//            //wifi和4G都没有打开
//            if(!isMobile && !isWifi){
//                return false;
//            }
//            boolean isAvailable = NetWorkUtils.isAvailable();
//            boolean isAvailableByPing = NetWorkUtils.isAvailableByPing();
//            boolean isAvailableByBaiduPing = NetWorkUtils.isAvailableByPing(NetWorkUtils.getDomainAddress("baidu.com"));
//            if(isAvailable || isAvailableByPing || isAvailableByBaiduPing){
//                return true;
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }

    /**
     * 判断移动网络是否开启
     */
    public static boolean isNetEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getI().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
//                        XLog.e(TAG, "移动网络已经开启");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否开启
     */
    public static boolean isWifiEnabled() {
        WifiManager wm = (WifiManager) MyApp.getI().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wm != null && wm.isWifiEnabled()) {
//            XLog.e(TAG, "Wifi网络已经开启");
            return true;
        }
//        XLog.e(TAG, "Wifi网络还未开启");
        return false;
    }

    /**
     * 判断移动网络是否连接成功
     */
    public static boolean isNetContected() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getI().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm != null && info != null && info.isConnected()) {
//            XLog.e(TAG, "移动网络连接成功");
            return true;
        }
//        XLog.e(TAG, "移动网络连接失败");
        return false;
    }

    /**
     * 判断WIFI是否连接成功
     */
    public static boolean isWifiContected() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getI().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected()) {
//            XLog.e(TAG, "Wifi网络连接成功");
            return true;
        }
//        XLog.e(TAG, "Wifi网络连接失败");
        return false;
    }

    /**
     * 判断移动网络和WIFI是否开启
     */
    public static boolean isNetWorkEnabled() {
        if (isNetEnabled() || isWifiEnabled()) {
//            XLog.e(TAG, "网络已经开启" + isNetEnabled() + "    ,    " + isWifiEnabled());
            return true;
        }
//        XLog.e(TAG, "网络还未开启" + isNetEnabled() + "    ,    " + isWifiEnabled());
        return false;
    }

    /**
     * 判断移动网络和WIFI是否连接成功
     */
    public static boolean isNetworkConnected() {
        if (isWifiContected() || isNetContected()) {
//            XLog.e(TAG, "网络连接成功" + isWifiContected() + "    ,    " + isNetContected());
            return true;
        }
//         XLog.e(TAG, "网络连接失败" + isWifiContected() + "    ,    " + isNetContected());
        return false;
    }


}
