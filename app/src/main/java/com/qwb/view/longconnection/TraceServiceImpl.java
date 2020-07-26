package com.qwb.view.longconnection;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.db.CacheLocationBean;
import com.qwb.db.LocationBean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroidmvp.log.XLog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * 省电模式：
 */
public class TraceServiceImpl extends AbsWorkService {

    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;

    public static void stopService() {
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
        //关闭音乐
        stopPlayMusic();
        //关闭定位，释放资源
        stopLocation();
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消对任务的订阅
        if (sDisposable != null) sDisposable.dispose();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        int upload = SPUtils.getIValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS);
        XLog.e("upload", "" + upload);
        if (1 == upload) {
            //初始化定位
            initLocation();
            //初始化音乐
            initMediaPlayer();
            //间隔线程
            sDisposable = Observable.interval(1, TimeUnit.SECONDS)//"每1秒采集一次数据"
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            //取消任务时取消定时唤醒
                            stopService();
                        }
                    })
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long count) throws Exception {
                            int traceTime = 60;// 默认一分钟
                            int min = SPUtils.getIValues(ConstantUtils.Sp.TRACK_UPLOAD_MIN);
                            if (min > 0) {
                                traceTime = min * 60;
                            }
//                            XLog.e("gps --count", "" + count);
                            if (null != count && count % traceTime == 0) {
//                                mLocClient.start();//开启定位
                                String workTime = SPUtils.getSValues(ConstantUtils.Sp.WORK_TIME);
                                String nowTime = MyTimeUtils.getNowTime();
                                if (!MyStringUtil.isEmpty(workTime)){
                                    if (!MyTimeUtils.judgmentDate(workTime, nowTime)){
                                        doSubmitData(mCurrentLocation);
                                        saveCacheData();
                                    }else {
                                        TraceServiceImpl.stopService();//关闭上传
                                        SPUtils.setValues(ConstantUtils.Sp.WORK_STATE,"2");
                                        //可以调下班接口
                                    }
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
    }


    /**
     * TODO -----------------------------音乐的一些设置-------------------------
     */
    //初始化音乐
    public void initMediaPlayer() {
        if (null == mMediaPlayer) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        }
        mMediaPlayer.setLooping(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
    }

    private static MediaPlayer mMediaPlayer;

    //播放音乐
    private static void startPlayMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    //关闭音乐，释放资源
    private static void stopPlayMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    //TODO ----------------定位的一些设置--------------------
    /**
     * 初始化定位
     */
    private static LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    public BDLocation mCurrentLocation;
    private long locationTime;//定位的时间戳（用来修改数据库）
    private String os = Build.MODEL + " " + Build.VERSION.RELEASE;// 手机型号,版本号;
    private String mAddressStr;//地址

    private void initLocation() {
        mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1 * 1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            // 去掉坐标【0，0】点
            if (4.9E-324 == location.getLatitude() || 4.9E-324 == location.getLongitude()) {
                return;
            }

                mCurrentLocation = location;
                XLog.e("gps=====定位", "" + location.getAddrStr());
//                doSubmitData(location);
//                mLocClient.stop();//关闭定位

                //保存上传数据（间隔1分钟）
//                saveCacheData();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    //关闭定位，释放资源
    public static void stopLocation() {
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
    }

    /**
     * 获取定位类型：gps;wifi;xl
     */
    private String getLocationFrom(BDLocation location) {
        String locationFrom = null;
        try {
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                locationFrom = "gps " + MyUtils.getAppVersion();
            }
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                locationFrom = "wifi " + MyUtils.getAppVersion();
            }
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                locationFrom = "lx " + MyUtils.getAppVersion();
            }
        }catch (Exception e){}
        return locationFrom;
    }

    /**
     * 发送请求
     */
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
                                        int code;
                                        JSONObject object = (JSONObject) parseArray.get(i);
                                        if (object != null) {
                                            code = object.getIntValue("code");
                                            if (0 == code) {
                                                //上传成功，删除缓存数据
                                                MyDataUtils.getInstance().delCacheLocationData(String.valueOf(locationTime));
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

    /**
     * 保存缓存数据
     */
    public void saveCacheData(){
        //保存上传数据（间隔1分钟）
        try{
            if(mCurrentLocation != null){
                CacheLocationBean bean = new CacheLocationBean();
                bean.setLatitude(Double.valueOf(mCurrentLocation.getLatitude()));
                bean.setLongitude(Double.valueOf(mCurrentLocation.getLongitude()));
                bean.setAddress(mAddressStr);
                bean.setLocation_time(locationTime);
                bean.setLocation_from(getLocationFrom(mCurrentLocation));
                bean.setOs(os);
                bean.setCompany_id(SPUtils.getCompanyId());
                bean.setUser_id(SPUtils.getID());
                MyDataUtils.getInstance().saveCacheLocationData(bean);
            }
        }catch (Exception e){
        }
    }

    /**
     * 处理提交数据
     */
    private void doSubmitData(BDLocation location) {
        try {
            if(location != null){
                if (!MyStringUtil.isEmpty(location.getAddrStr())) {
                    mAddressStr = location.getAddrStr();
                }
                locationTime = System.currentTimeMillis() / 1000;//当前的时间戳（上传成功修改数据库状态）
                LocationBean locationBean = new LocationBean();
                locationBean.setLatitude(Double.valueOf(location.getLatitude()));
                locationBean.setLongitude(Double.valueOf(location.getLongitude()));
                locationBean.setAddress(mAddressStr);
                locationBean.setLocation_time(locationTime);
                locationBean.setLocation_from(getLocationFrom(location));//定位类型+app的版本号
                locationBean.setOs(os);//手机型号，手机版本号
                List<LocationBean> list = new ArrayList<>();
                list.clear();
                list.add(locationBean);
                String locStr = JSON.toJSONString(list);
                String workState = SPUtils.getSValues(ConstantUtils.Sp.WORK_STATE);//1:上班；2：下班
                int upload = SPUtils.getIValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS);
                if (1 == upload && "1".equals(workState)) {
                    sendData(locStr);
                }
            }
        }catch (Exception e){
        }
    }

}
