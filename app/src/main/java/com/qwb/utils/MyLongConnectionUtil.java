package com.qwb.utils;



import com.alibaba.fastjson.JSON;
import com.qwb.db.LocationBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * 上传定位
 */

public class MyLongConnectionUtil {

    public MyLongConnectionUtil() {
    }

    private static MyLongConnectionUtil MANAGER = null;
    public static MyLongConnectionUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyLongConnectionUtil();
        }
        return MANAGER;
    }

    // 状态--1:上班，2：下班，3：拜访签到，4：拜访签退
    public void addLocation(String latitude, String longitude, String addressStr, String locationFrom, int workStatus){
        try {
            List<LocationBean> list = new ArrayList<>();
            LocationBean mLocationBean = new LocationBean();
            mLocationBean.setLatitude(Double.valueOf(latitude));
            mLocationBean.setLongitude(Double.valueOf(longitude));
            mLocationBean.setAddress(addressStr);
            mLocationBean.setLocation_time(Long.valueOf(System.currentTimeMillis() / 1000));
            mLocationBean.setLocation_from(locationFrom);
            mLocationBean.setOs(android.os.Build.MODEL + "   " + android.os.Build.VERSION.RELEASE);
            mLocationBean.setWork_status(workStatus);// 状态--1:上班，2：下班，3：拜访签到，4：拜访签退
            mLocationBean.setVisit_check_in_time(Long.valueOf(System.currentTimeMillis() / 1000));
            list.add(mLocationBean);
            final String location = JSON.toJSONString(list);

            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    Map<String, String> params = new HashMap<>();
                    params.put("company_id", SPUtils.getCompanyId());
                    params.put("user_id", SPUtils.getID());
                    params.put("location", location);
                    OkHttpUtils
                            .post()
                            .params(params)
                            .url(Constans.postLocation)
                            .id(1)
                            .build()
                            .execute(new MyHttpCallback(null) {
                                @Override
                                public void myOnError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void myOnResponse(String response, int id) {
//                                    XLog.e("上传定位","response: " + response);
                                }
                            });
                }
            }).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(Object o) {
                }
                @Override
                public void onError(Throwable e) {
                }
                @Override
                public void onComplete() {
                }
            });
        }catch (Exception e){

        }
    }






}
