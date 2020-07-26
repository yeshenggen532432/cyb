package com.qwb.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.application.MyApp;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DRecordUpdateBean;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep2Bean;
import com.qwb.db.DStep3Bean;
import com.qwb.db.DStep4Bean;
import com.qwb.db.DStep5Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.utils.MyFileUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.ware.model.QueryStkWareType;
import com.qwb.view.step.model.ShopInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class XStepService extends Service {
    public XStepService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doCache();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void doCache(){
        if(MyNetWorkUtil.isNetworkConnected() && !MyStringUtil.isEmpty(SPUtils.getCompanyId())){
            addStep1();
            addStep2();
            addStep3();
            addStep4();
            addStep5();
            addStep6();
            //记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)
//            queryCustomer();
//            queryWareType();
//            queryWare();
        }
    }

    //步骤1
    public Disposable disposable = null;
    public void addStep1(){
        try {
            if(null == disposable){
                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep1(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //步骤2
    public Disposable disposable2 = null;
    public void addStep2(){
        try {
            if(null == disposable2){

                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable2 = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep2(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        }catch (Exception e){
        }
    }

    //步骤3
    public Disposable disposable3 = null;
    public void addStep3(){
        try {
            if(null == disposable3){
                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable3 = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep3(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){

        }
    }

    //步骤4
    public Disposable disposable4 = null;
    public void addStep4(){
        try {
            if(null == disposable4){
                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable4 = d;
                            }

                            @Override
                            public void onNext(Long count) {

                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep4(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //步骤5
    public Disposable disposable5 = null;
    public void addStep5(){
        try {
            if(null == disposable5){
                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable5 = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep5(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //步骤6
    public Disposable disposable6 = null;
    public void addStep6(){
        try {
            if(null == disposable6){
                Observable.interval(0,20, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable6 = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                boolean b = SPUtils.getBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE);
                                if(!b){
                                    doStep6(count);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //---------------------------------------------------------------------------------------------

    //处理拜访1
    private void doStep1(Long count) {
        try {
            //避免内存
            if(count > 30){
                if(null != disposable){
                    disposable.dispose();
                    disposable = null;
                }
                return;
            }
            final DStep1Bean bean = MyDataUtils.getInstance().queryStep1();
            if(null == bean){
                if(null != disposable){
                    disposable.dispose();
                    disposable = null;
                }
                return;
            }
            String address = bean.getAddress();
            if(MyStringUtil.isEmpty(address) && !MyStringUtil.isEmpty(bean.getLongitude())){
                //没地址信息
                LatLng latLng = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
                reverseGeoCode(latLng, bean, null);
            }else{
                doStep1(bean, address);
            }
        }catch (Exception e){
        }
    }

    /**
     * 有地址信息
     */
    private void doStep1(final DStep1Bean bean, String address) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        params.put("longitude", bean.getLongitude());
        params.put("latitude", bean.getLatitude());
        params.put("address", address);
        params.put("isXy", bean.getIsXy());
        if (!MyStringUtil.isEmpty(bean.getIsXy())) {
            params.put("isXy", bean.getIsXy());
        }
        if (!MyStringUtil.isEmpty(bean.getHbzt())) {
            params.put("hbzt", bean.getHbzt());// 及时更换外观破损，肮脏的海报招贴
        }
        if (!MyStringUtil.isEmpty(bean.getGgyy())) {
            params.put("ggyy", bean.getGgyy());// 拆除过时的附有旧广告用语的宣传品
        }
        if (!MyStringUtil.isEmpty(bean.getRemo())) {
            params.put("remo", bean.getRemo());// 摘要
        }

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        //记录要删除的图片文件
        final List<File> delFiles = new ArrayList<>();

        List<String> picList = bean.getPicList();
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                if(MyFileUtil.isFileExist(picList.get(i))){
                    File oldFile = new File(picList.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        String count1 = bean.getCount();
        String pId = bean.getpId();
        String url = null;
        if ("0".equals(count1)) { // 0:拜访拍照签到（添加） 1:拜访拍照签到（修改）
            url = Constans.addBfqdpz;
        } else if ("1".equals(count1)) {
            url = Constans.updateBfqdpz;
            params.put("id", pId);// 拜访拍照签到id
        }
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep1(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep1(bean);
                                //删除图片
                                if(null != delFiles && !delFiles.isEmpty()){
                                    for (File file: delFiles) {
                                        MyFileUtil.deleteFile(file);
                                    }
                                }
                            }else{
                                MyDataUtils.getInstance().updateStep1(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep1(bean);
                        }

                    }
                });
    }

    //处理拜访2
    private void doStep2(Long count) {
        //避免内存
        if(count > 30){
            if(null != disposable2){
                disposable2.dispose();
                disposable2 = null;
            }
            return;
        }
        final DStep2Bean bean = MyDataUtils.getInstance().queryStep2();
        if(null == bean){
            if(null != disposable2){
                disposable2.dispose();
                disposable2 = null;
            }
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        // 可选
        if (!MyStringUtil.isEmpty(bean.getPophb())) {
            params.put("pophb", bean.getPophb());
        }
        if (!MyStringUtil.isEmpty(bean.getCq())) {
            params.put("cq", bean.getCq());
        }
        if (!MyStringUtil.isEmpty(bean.getWq())) {
            params.put("wq", bean.getWq());
        }
        if (!MyStringUtil.isEmpty(bean.getRemo1())) {
            params.put("remo1", bean.getRemo1());
        }
        if (!MyStringUtil.isEmpty(bean.getRemo2())) {
            params.put("remo2", bean.getRemo2());
        }
        if (!MyStringUtil.isEmpty(bean.getIsXy())) {
            params.put("isXy", bean.getIsXy());
        }

        //记录要删除的图片文件
        final List<File> delFiles = new ArrayList<>();

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        List<String> picList = bean.getPicList();
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                if(MyFileUtil.isFileExist(picList.get(i))){
                    File oldFile = new File(picList.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file1" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        List<String> picList2 = bean.getPicList2();
        if (null != picList2 && !picList2.isEmpty()) {
            for (int i = 0; i < picList2.size(); i++) {
                if(MyFileUtil.isFileExist(picList2.get(i))){
                    File oldFile = new File(picList2.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file2" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        String url = null;
        if ("0".equals(bean.getCount())) { // 0:生动化检查（添加） 1:生动化检查（修改）
            url = Constans.addBfsdhjc;
        } else if ("1".equals((bean.getCount()))) {
            url = Constans.updateBfsdhjc;
            params.put("id", String.valueOf(bean.getBfId()));// 拜访拍照签到id
        }
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep2(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep2(bean);
                                //删除图片
                                if(null != delFiles && !delFiles.isEmpty()){
                                    for (File file: delFiles) {
                                        MyFileUtil.deleteFile(file);
                                    }
                                }
                            }else{
                                MyDataUtils.getInstance().updateStep2(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep2(bean);
                        }

                    }
                });
    }

    //处理拜访3
    private void doStep3(Long count) {
        //避免内存
        if(count > 30){
            if(null != disposable3){
                disposable3.dispose();
                disposable3 = null;
            }
            return;
        }
        final DStep3Bean bean = MyDataUtils.getInstance().queryStep3();
        if(null == bean){
            if(null != disposable3){
                disposable3.dispose();
                disposable3 = null;
            }
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        if (!MyStringUtil.isEmpty(bean.getXxjh())) {
            params.put("xxjh", bean.getXxjh());
        }

        //记录要删除的图片文件
        final List<File> delFiles = new ArrayList<>();

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        List<String> picList = bean.getPicList();
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                if(MyFileUtil.isFileExist(picList.get(i))){
                    File oldFile = new File(picList.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file1" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        List<String> picList2 = bean.getPicList2();
        if (null != picList2 && !picList2.isEmpty()) {
            for (int i = 0; i < picList2.size(); i++) {
                if(MyFileUtil.isFileExist(picList2.get(i))){
                    File oldFile = new File(picList2.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file2" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }
        List<String> picList3 = bean.getPicList3();
        if (null != picList3 && !picList3.isEmpty()) {
            for (int i = 0; i < picList3.size(); i++) {
                if(MyFileUtil.isFileExist(picList3.get(i))){
                    File oldFile = new File(picList3.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file3" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        String url = null;
        if ("0".equals(bean.getCount())) { // 0:生动化检查（添加） 1:生动化检查（修改）
            url = Constans.addBfcljccj;
        } else if ("1".equals((bean.getCount()))) {
            url = Constans.updateBfcljccj;
        }
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep3(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep3(bean);
                                //删除图片
                                if(null != delFiles && !delFiles.isEmpty()){
                                    for (File file: delFiles) {
                                        MyFileUtil.deleteFile(file);
                                    }
                                }
                            }else{
                                MyDataUtils.getInstance().updateStep3(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep3(bean);
                        }

                    }
                });
    }

    //处理拜访4
    private void doStep4(Long count) {
        //避免内存
        if(count > 30){
            if(null != disposable4){
                disposable4.dispose();
                disposable4 = null;
            }
            return;
        }
        final DStep4Bean bean = MyDataUtils.getInstance().queryStep4();
        if(null == bean){
            if(null != disposable4){
                disposable4.dispose();
                disposable4 = null;
            }
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        if(!MyUtils.isEmptyString(bean.getXsxj())){
            params.put("xsxj", bean.getXsxj());
        }

        String url = null;
        if("0".equals(bean.getCount())){// 0:销售小结（添加） 1:销售小结（修改）
            url = Constans.addBfxsxjWeb;
        }else if("1".equals(bean.getCount())){
            url = Constans.updateBfxsxjWeb;
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep4(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep4(bean);
                            }else{
                                MyDataUtils.getInstance().updateStep4(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep4(bean);
                        }
                    }
                });
    }

    //处理拜访5:下单  1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
    private void doStep5(Long count) {
        //避免内存
        if(count > 30){
            if(null != disposable5){
                disposable5.dispose();
                disposable5 = null;
            }
            return;
        }
        final DStep5Bean bean = MyDataUtils.getInstance().queryStep5();
        if(null == bean){
            if(null != disposable5){
                disposable5.dispose();
                disposable5 = null;
            }
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        if (!MyStringUtil.isEmpty(bean.getShr())) {
            params.put("shr", bean.getShr());
        }
        if (!MyStringUtil.isEmpty(bean.getTel())) {
            params.put("tel", bean.getTel());
        }
        if (!MyStringUtil.isEmpty(bean.getAddress())) {
            params.put("address", bean.getAddress());
        }
        if (!MyStringUtil.isEmpty(bean.getRemo())) {
            params.put("remo", bean.getRemo());
        }
        if (!MyStringUtil.isEmpty(bean.getZje())) {
            params.put("zje", bean.getZje());
        }
        if (!MyStringUtil.isEmpty(bean.getZdzk())) {
            params.put("zdzk", bean.getZdzk());
        }
        if (!MyStringUtil.isEmpty(bean.getCjje())) {
            params.put("cjje", bean.getCjje());
        }
        if (!MyStringUtil.isEmpty(bean.getOrderId())) {
            params.put("orderxx", bean.getOrderxx());
        }
        if (!MyStringUtil.isEmpty(bean.getShTime())) {
            params.put("shTime", bean.getShTime());
        }
        if (!MyStringUtil.isEmpty(bean.getPszd())) {
            params.put("pszd", bean.getPszd());
        }
        if (!MyStringUtil.isEmpty(bean.getStkId())) {
            params.put("stkId", bean.getStkId());
        }

        String url = null;
        // type 1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货 5：退货下单（列表）
        String type = bean.getType();
        if("1".equals(type)){
            if("0".equals(bean.getCount())){
                url = Constans.addBforderWeb;
            }else if("1".equals(bean.getCount())){
                params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                url = Constans.updateBforderWeb;
            }
        }else if("2".equals(type)){
            url = Constans.addDhorderWeb;
        }else if("3".equals(type)){
            params.put("id", String.valueOf(bean.getOrderId()));// 订单id
            url = Constans.updateDhorderWeb;
        }else if("4".equals(type)){
            url = Constans.addThorderWeb;
        }else if("5".equals(type)){
            params.put("id", String.valueOf(bean.getOrderId()));// 订单id
            url = Constans.updateThorderWeb;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep5(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep5(bean);
                            }else{
                                MyDataUtils.getInstance().updateStep5(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep5(bean);
                        }
                    }
                });
    }

    //处理拜访6
    private void doStep6(Long count) {
        //避免内存
        if(count > 30){
            if(null != disposable6){
                disposable6.dispose();
                disposable6 = null;
            }
            return;
        }
        final DStep6Bean bean = MyDataUtils.getInstance().queryStep6();
        if(null == bean){
            if(null != disposable6){
                disposable6.dispose();
                disposable6 = null;
            }
            return;
        }

        String address = bean.getAddress();
        if(MyStringUtil.isEmpty(address) && !MyStringUtil.isEmpty(bean.getLongitude())){
            //没地址信息
            LatLng latLng = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
            reverseGeoCode(latLng, null, bean);
        }else{
            doStep6(bean, address);
        }
    }

    private void doStep6(final DStep6Bean bean, final String address) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        params.put("longitude", bean.getLongitude());
        params.put("latitude", bean.getLatitude());
        params.put("address", address);
        if (!MyStringUtil.isEmpty(bean.getBcbfzj())) {
            params.put("bcbfzj", bean.getBcbfzj());
        }
        if (!MyStringUtil.isEmpty(bean.getDbsx())) {
            params.put("dbsx", bean.getDbsx());
        }
        if (!MyStringUtil.isEmpty(bean.getXcdate())) {
            params.put("xcdate", bean.getXcdate());
        }
        if (!MyStringUtil.isEmpty(bean.getXsjdNm())) {
            params.put("xsjdNm", bean.getXsjdNm());
        }
        if (!MyStringUtil.isEmpty(bean.getBfflNm())) {
            params.put("bfflNm", bean.getBfflNm());
        }

        //记录要删除的图片文件
        final List<File> delFiles = new ArrayList<>();

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        // 语音
        if (!MyStringUtil.isEmpty(bean.getVoice())) {
            File voiceFile = new File(bean.getVoice());
            if(null != voiceFile){
                files.put("voice", voiceFile);// 语音文件
                params.put("voiceTime", String.valueOf(bean.getVoiceTime()));// 语音时长
            }
        }

        List<String> picList = bean.getPicList();
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                if(MyFileUtil.isFileExist(picList.get(i))){
                    File oldFile = new File(picList.get(i));
                    File newFile = new CompressHelper.Builder(MyApp.getI().getApplicationContext())
                            .setMaxWidth(720)  // 默认最大宽度为720
                            .setMaxHeight(1280) // 默认最大高度为960
                            .setQuality(80)    // 默认压缩质量为80
                            .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                            .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                            .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                            .build()
                            .compressToFile(oldFile);
                    //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                    String absolutePath = newFile.getAbsolutePath();
                    String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                    files.put("file" + j + 1, newFile);

                    delFiles.add(oldFile);
                }
            }
        }

        String url = null;
        if ("0".equals(bean.getCount())) {
            url = Constans.addBfgzxcWeb;
        } else if ("1".equals(bean.getCount())) {
            url = Constans.updateBfgzxcWeb;
            params.put("id", bean.getBfId());// 拜访拍照签到id
        }
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep6(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep6(bean);
                                //删除图片
                                if(null != delFiles && !delFiles.isEmpty()){
                                    for (File file: delFiles) {
                                        MyFileUtil.deleteFile(file);
                                    }
                                }
                            }else{
                                MyDataUtils.getInstance().updateStep6(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep6(bean);
                        }

                    }
                });
    }

    //经纬度转地址
    public void reverseGeoCode(LatLng latLng, final DStep1Bean bean1, final DStep6Bean bean6) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                String address = result.getAddress();//这里的addressText就是我们要的地址
                if(null != bean1){
                    doStep1(bean1, address);
                }else if(null != bean6){
                    doStep6(bean6, address);
                }
            }
        });
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    //--------------------------------//记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)-------------------------------------------------------------

    //记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)
    public Disposable disposableCustomer = null;
    public void queryCustomer(){
        try {
            if(null == disposableCustomer){
                Observable.interval(0,120, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableCustomer = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                doCustomer(count);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //处理：我的客户
    DRecordUpdateBean mDRecordUpdateCustomer;
    private void doCustomer(Long count) {
        try {
            //避免内存
            if(count > 1){
                if(null != disposableCustomer){
                    disposableCustomer.dispose();
                    disposableCustomer = null;
                }
                return;
            }
            mDRecordUpdateCustomer = MyDataUtils.getInstance().queryRecodeUpdate("1");
            if(null != mDRecordUpdateCustomer && 1 == mDRecordUpdateCustomer.getUpdate() && MyTimeUtils.getTodayStr().equals(mDRecordUpdateCustomer.getTime())){
                //更新过了
                if(null != disposableCustomer){
                    disposableCustomer.dispose();
                    disposableCustomer = null;
                }
                return;
            }
            if(null == mDRecordUpdateCustomer){
                mDRecordUpdateCustomer = new DRecordUpdateBean();
                mDRecordUpdateCustomer.setUserId(SPUtils.getID());
                mDRecordUpdateCustomer.setCompanyId(SPUtils.getCompanyId());
                mDRecordUpdateCustomer.setTime(MyTimeUtils.getTodayStr());
                mDRecordUpdateCustomer.setUpdate(0);
                mDRecordUpdateCustomer.setModel("1");
            }

            Map<String, String> params = new HashMap<>();
            params.put("token", SPUtils.getTK());
            params.put("pageNo", "1");
            params.put("pageSize", "100000");
            params.put("dataTp", "1");

            OkHttpUtils.post()
                    .params(params)
                    .url(Constans.mineClient)
                    .id(3)
                    .build()
                    .execute(new MyHttpCallback(null) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            try {
                                MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
                                if (parseObject != null && parseObject.isState()) {
                                    List<MineClientInfo> dataList = parseObject.getRows();
                                    if(dataList!=null && !dataList.isEmpty()){
                                        MyDataUtils.getInstance().saveMineClient(dataList);
                                        MyDataUtils.getInstance().updateRecodeUpdate(mDRecordUpdateCustomer);
                                    }
                                } else {
//                                    ToastUtils.showCustomToast(parseObject.getMsg());
                                }
                            }catch (Exception e){
                                ToastUtils.showError(e);
                            }
                        }
                    });
        }catch (Exception e){
        }
    }

    //记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)
    public Disposable disposableWareType = null;
    public void queryWareType(){
        try {
            if(null == disposableWareType){
                Observable.interval(0,120, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableWareType = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                doWareType(count);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //处理：商品分类
    DRecordUpdateBean mDRecordUpdateWareType;
    private void doWareType(Long count) {
        try {
            //避免内存
            if(count > 1){
                if(null != disposableWareType){
                    disposableWareType.dispose();
                    disposableWareType = null;
                }
                return;
            }
            mDRecordUpdateWareType = MyDataUtils.getInstance().queryRecodeUpdate("2");
            if(null != mDRecordUpdateWareType && 1 == mDRecordUpdateWareType.getUpdate() && MyTimeUtils.getTodayStr().equals(mDRecordUpdateWareType.getTime())){
                //更新过了
                if(null != disposableWareType){
                    disposableWareType.dispose();
                    disposableWareType = null;
                }
                return;
            }
            if(null == mDRecordUpdateWareType){
                mDRecordUpdateWareType = new DRecordUpdateBean();
                mDRecordUpdateWareType.setUserId(SPUtils.getID());
                mDRecordUpdateWareType.setCompanyId(SPUtils.getCompanyId());
                mDRecordUpdateWareType.setTime(MyTimeUtils.getTodayStr());
                mDRecordUpdateWareType.setUpdate(0);
                mDRecordUpdateWareType.setModel("2");
            }

            Map<String, String> params = new HashMap<>();
            params.put("token", SPUtils.getTK());
            OkHttpUtils.post()
                    .params(params)
                    .url(Constans.queryStkWareType)
                    .build()
                    .execute(new MyHttpCallback(null) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            try {
                                QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
                                if (bean != null) {
                                    if (bean.isState()) {
                                        List<DWareTypeBean> mData = new ArrayList<>();
                                        mData.clear();

                                        List<QueryStkWareType.List1> list = bean.getList();
                                        if (null != list && !list.isEmpty()) {
                                            // 第一层
                                            for (int i = 0; i < list.size(); i++) {
                                                QueryStkWareType.List1 data1 = list.get(i);

                                                DWareTypeBean wareTypeBean = new DWareTypeBean();
                                                wareTypeBean.setUserId(SPUtils.getID());
                                                wareTypeBean.setCompanyId(SPUtils.getCompanyId());
                                                wareTypeBean.setPid(0);
                                                wareTypeBean.setWareTypeId(data1.getWaretypeId());
                                                wareTypeBean.setWareTypeLeaf(data1.getWaretypeLeaf());
                                                wareTypeBean.setWareTypeNm(data1.getWaretypeNm());
                                                mData.add(wareTypeBean);

                                                // 第二层
                                                List<QueryStkWareType.List2> list2 = data1.getList2();
                                                if (list2 != null && list2.size() > 0) {
                                                    for (int z = 0; z < list2.size(); z++) {
                                                        QueryStkWareType.List2 list2_2 = list2.get(z);

                                                        DWareTypeBean wareTypeBean2 = new DWareTypeBean();
                                                        wareTypeBean2.setUserId(SPUtils.getID());
                                                        wareTypeBean2.setCompanyId(SPUtils.getCompanyId());
                                                        wareTypeBean2.setPid(data1.getWaretypeId());
                                                        wareTypeBean2.setWareTypeId(list2_2.getWaretypeId());
                                                        wareTypeBean2.setWareTypeNm(list2_2.getWaretypeNm());
                                                        wareTypeBean2.setWareTypeLeaf(list2_2.getWaretypeLeaf());
                                                        mData.add(wareTypeBean2);
                                                    }
                                                }
                                            }
                                            MyDataUtils.getInstance().saveWareType(mData);
                                            MyDataUtils.getInstance().updateRecodeUpdate(mDRecordUpdateWareType);
                                        }

                                    }
                                }
                            }catch (Exception e){
                            }
                        }
                    });
        }catch (Exception e){
        }
    }

    //记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)
    public Disposable disposableWare = null;
    public void queryWare(){
        try {
            if(null == disposableWare){
                Observable.interval(0,120, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableWare = d;
                            }

                            @Override
                            public void onNext(Long count) {
                                doWare(count);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception e){
        }
    }

    //处理：商品分类
    DRecordUpdateBean mDRecordUpdateWare;
    private void doWare(Long count) {
        try {
            //避免内存
            if(count > 1){
                if(null != disposableWare){
                    disposableWare.dispose();
                    disposableWare = null;
                }
                return;
            }
            mDRecordUpdateWare = MyDataUtils.getInstance().queryRecodeUpdate("3");
            if(null != mDRecordUpdateWare && 1 == mDRecordUpdateWare.getUpdate() && MyTimeUtils.getTodayStr().equals(mDRecordUpdateWare.getTime())){
                //更新过了
                if(null != disposableWare){
                    disposableWare.dispose();
                    disposableWare = null;
                }
                return;
            }
            if(null == mDRecordUpdateWare){
                mDRecordUpdateWare = new DRecordUpdateBean();
                mDRecordUpdateWare.setUserId(SPUtils.getID());
                mDRecordUpdateWare.setCompanyId(SPUtils.getCompanyId());
                mDRecordUpdateWare.setTime(MyTimeUtils.getTodayStr());
                mDRecordUpdateWare.setUpdate(0);
                mDRecordUpdateWare.setModel("3");
            }

            Map<String, String> params = new HashMap<>();
            params.put("token", SPUtils.getTK());
            OkHttpUtils.post()
                    .params(params)
                    .url(Constans.queryAllCacheWare)
                    .build()
                    .execute(new MyHttpCallback(null) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            try {
                                ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
                                if (null != parseObject && parseObject.isState()){
                                    List<ShopInfoBean.Data> dataList = parseObject.getList();
                                    if(null != dataList){
                                        List<DWareBean> wareBeans = new ArrayList<>();
                                        for (ShopInfoBean.Data data: dataList) {
                                            DWareBean bean = new DWareBean();
                                            bean.setUserId(SPUtils.getID());
                                            bean.setCompanyId(SPUtils.getCompanyId());
                                            bean.setWareId(data.getWareId());
                                            bean.setWareNm(data.getWareNm());
                                            bean.setHsNum(data.getHsNum());
                                            bean.setWareGg(data.getWareGg());
                                            bean.setWareDw(data.getWareDw());
                                            bean.setMinUnit(data.getMinUnit());
                                            bean.setWareDj(data.getWareDj());
                                            bean.setMaxUnitCode(data.getMaxUnitCode());
                                            bean.setMinUnitCode(data.getMinUnitCode());
                                            bean.setSunitFront(data.getSunitFront());
                                            bean.setWareType(data.getWareType());
                                            bean.setPy(data.getPy());
                                            wareBeans.add(bean);
                                        }
                                        MyDataUtils.getInstance().saveWare(wareBeans);
                                        MyDataUtils.getInstance().updateRecodeUpdate(mDRecordUpdateWare);
                                    }
                                }
                            }catch (Exception e){
                            }
                        }
                    });
        }catch (Exception e){
        }
    }







}
