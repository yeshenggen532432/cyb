package com.qwb.view.longconnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.db.CacheLocationBean;
import com.qwb.db.LocationBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * 上传缓存数据
 *
 * @author yeshenggen
 * @date 2017.4.24
 */
public class CacheService extends Service {

    private long mLocationTime;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Observable.interval(10, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        queryData();//查询数据
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 查询数据
     */
    private void queryData() {
        String workState = SPUtils.getSValues(ConstantUtils.Sp.WORK_STATE);
        if ("1".equals(workState)) {
            List<LocationBean> list = new ArrayList<>();
            list.clear();
            List<CacheLocationBean> beans = LitePal.limit(100).order("location_time").find(CacheLocationBean.class);
            if (beans != null && beans.size() > 0) {
                for (int i = 0; i < beans.size(); i++) {
                    CacheLocationBean bean = beans.get(i);
                    LocationBean locationBean = new LocationBean();
                    locationBean.setLatitude(Double.valueOf(bean.getLatitude()));
                    locationBean.setLongitude(Double.valueOf(bean.getLongitude()));
                    locationBean.setAddress(bean.getAddress());
                    locationBean.setLocation_time(bean.getLocation_time());
                    locationBean.setLocation_from(bean.getLocation_from());
                    locationBean.setOs(bean.getOs());
                    list.add(locationBean);
                    String locStr = JSON.toJSONString(list);
//                    sendData(locStr);
                    mLocationTime = bean.getLocation_time();//最后一条的时间
                }
            } else {
                stopSelf();//服务本身关闭
            }
        }
    }

    public void sendData(String locStr) {
        OkHttpUtils
                .post()
                .url(Constans.postLocation)
                .addParams("company_id", SPUtils.getCompanyId())
                .addParams("user_id", SPUtils.getID())
                .addParams("location", locStr)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            if (MyStringUtil.isEmpty(response) && response.startsWith("[")) {
                                JSONArray parseArray = JSON.parseArray(response);
                                if (parseArray != null && parseArray.size() > 0) {
                                    for (int i = 0; i < parseArray.size(); i++) {
                                        int code = -1;
                                        JSONObject object = (JSONObject) parseArray.get(i);
                                        if (object != null) {
                                            code = object.getIntValue("code");
                                            if (0 == code) {
                                                //删除缓存数据
                                                LitePal.deleteAll(CacheLocationBean.class, "location_time<=?", String.valueOf(mLocationTime));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

}
