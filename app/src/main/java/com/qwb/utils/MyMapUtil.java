package com.qwb.utils;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.qwb.db.LocationBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 百度定位工具类
 */

public class MyMapUtil {
    private OnLocationListener mLocationListener;

    public MyMapUtil() {
    }

    private static MyMapUtil MANAGER = null;
    public static MyMapUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyMapUtil();
        }
        return MANAGER;
    }

    public int errorCount = 0;
    public MyMapUtil getLocationClient(Context context){
        try {
            final LocationClient client = new LocationClient(context); // 声明LocationClient类
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
            option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
            option.setOpenGps(true);// 可选，默认false,设置是否使用gps
            client.setLocOption(option);
            client.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (null == bdLocation) {
                        errorCount ++;
                        if(5 == errorCount){
                            if(null != mLocationListener){
                                mLocationListener.setErrorListener();
                            }
                        }
                        client.stop();
                    }else{
                        if(null != mLocationListener){
                            mLocationListener.setOnSuccessListener(bdLocation);
                        }
                        errorCount = 0;
                        client.stop();

                        //没有地址信息，通过经纬度转地址
                        if(MyStringUtil.isEmpty(bdLocation.getAddrStr())){
                            reverseGeoCode(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
                        }
                    }
                }
            });

            client.start();
        }catch (Exception e){
            ToastUtils.showError(e);
        }

        return this;
    }

    public void setOnLocationListener(OnLocationListener onLocationListener){
        mLocationListener = onLocationListener;
    }

    public interface OnLocationListener{
       void setOnSuccessListener(BDLocation bdLocation);
       void setErrorListener();
       void setAddressListener(String addressStr);
    }

    //经纬度转地址
    public MyMapUtil reverseGeoCode(LatLng latLng) {
        try {
            // 创建地理编码检索实例
            GeoCoder geoCoder = GeoCoder.newInstance();
            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                // 反地理编码查询结果回调函数
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        return;
                    }
                    String addressStr = result.getAddress();//这里的addressText就是我们要的地址
                    if(null != mLocationListener){
                        mLocationListener.setAddressListener(addressStr);
                    }
                }

                // 地理编码查询结果回调函数
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null|| result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                    }
                }
            };
            // 设置地理编码检索监听者
            geoCoder.setOnGetGeoCodeResultListener(listener);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        }catch (Exception e){
        }
        return this;
    }

    public void toNav(Context context,String latitude, String longitude,  String latitude2, String longitude2){
        if (MyStringUtil.isEmpty(latitude) || MyUtils.isEmptyString(longitude)) {
            ToastUtils.showCustomToast("起点没有经纬度，请检查位置是否正确");
            return;
        }
        if (MyStringUtil.isEmpty(latitude2) || MyUtils.isEmptyString(longitude2)) {
            ToastUtils.showCustomToast("终点没有经纬度，请检查位置是否正确");
            return;
        }

        LatLng pt1 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        LatLng pt2 = new LatLng(Double.valueOf(latitude2), Double.valueOf(longitude2));
        // 构建 导航参数
        if (isInstallByread("com.baidu.BaiduMap")) {
            NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("起始").endName("结束");
            BaiduMapNavigation.openBaiduMapNavi(para, context);
        } else {
            NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2);
            BaiduMapNavigation.openWebBaiduMapNavi(para, context);
        }
    }

    /**
     * 判断是否安装目标应用
     *  目标应用安装后的包名
     *  是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 上下班添加轨迹点
     * work_status1:上班，2：下班，3：拜访签到，4：拜访签退
     */
    public void sendLocation(int work_status, String latitude, String longitude, String address, String location_from) {
//        List<LocationBean> list = new ArrayList<>();
//        LocationBean mLocationBean = new LocationBean();
//        mLocationBean.setLatitude(Double.valueOf(latitude));
//        mLocationBean.setLongitude(Double.valueOf(longitude));
//        mLocationBean.setAddress(address);
//        mLocationBean.setLocation_time(Long.valueOf(System.currentTimeMillis() / 1000));
//        mLocationBean.setLocation_from(location_from);
//        mLocationBean.setOs(Build.MODEL + "   " + Build.VERSION.RELEASE);
//        mLocationBean.setWork_status(work_status);
//        list.add(mLocationBean);
//        // 作用：同一个时间连线，不同时间断开连线
//        if (1 == work_status) {
//            SPUtils.setValues("check_in_time", String.valueOf(System.currentTimeMillis() / 1000));
//        }
//        String location = JSON.toJSONString(list);
//
//        sendData(location);
    }

    /**
     * 发送请求
     */
    public void sendData(String locStr) {
//        OkHttpUtils
//                .post()
//                .url(Constans.postLocation)
//                .addParams("company_id", SPUtils.getCompanyId())
//                .addParams("user_id", SPUtils.getID())
//                .addParams("location", locStr)
//                .id(1)
//                .build()
//                .execute(new MyHttpCallback(null) {
//                    @Override
//                    public void myOnError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void myOnResponse(String response, int id) {
//                        try {
//                            if (MyStringUtil.isEmpty(response) && response.startsWith("[")) {
//                                JSONArray parseArray = JSON.parseArray(response);
//                                if (parseArray != null && parseArray.size() > 0) {
//                                    for (int i = 0; i < parseArray.size(); i++) {
//                                        JSONObject object = (JSONObject) parseArray.get(i);
//                                        if (object != null) {
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                });
    }






}
