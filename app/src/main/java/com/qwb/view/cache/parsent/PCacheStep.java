package com.qwb.view.cache.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

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
import com.qwb.view.cache.ui.CacheStepActivity;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep2Bean;
import com.qwb.db.DStep3Bean;
import com.qwb.db.DStep4Bean;
import com.qwb.db.DStep5Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DStepAllBean;
import com.qwb.utils.MyFileUtil;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：缓存-拜访步骤
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCacheStep extends XPresent<CacheStepActivity>{
    private Activity activity;

    public void queryAllCache(Activity activity, int position, int pageNo, int pageSize) {
        this.activity = activity;
        try {
            if(null != getV()){
                switch (position){
                    case 1:
                        List<DStep1Bean> step1Beans = MyDataUtils.getInstance().queryStep1(pageNo, pageSize);
                        if(null != step1Beans){
                            getV().refreshAdapter1(step1Beans, false);
                        }
                        break;
                    case 2:
                        List<DStep2Bean> step2Beans = MyDataUtils.getInstance().queryStep2(pageNo, pageSize);
                        if(null != step2Beans){
                            getV().refreshAdapter2(step2Beans, false);
                        }
                        break;
                    case 3:
                        List<DStep3Bean> step3Beans = MyDataUtils.getInstance().queryStep3(pageNo, pageSize);
                        if(null != step3Beans){
                            getV().refreshAdapter3(step3Beans, false);
                        }
                        break;
                    case 4:
                        List<DStep4Bean> step4Beans = MyDataUtils.getInstance().queryAllStep4();
                        if(null != step4Beans){
                            getV().refreshAdapter4(step4Beans, false);
                        }
                        break;
                    case 5:
                        // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
                        // 这边只查询（拜访下单(添加或修改)）
                        List<DStep5Bean> step5Beans = MyDataUtils.getInstance().queryAllStep5("1", true);
                        if(null != step5Beans){
                            getV().refreshAdapter5(step5Beans, false);
                        }
                        break;
                    case 6:
                        List<DStep6Bean> step6Beans = MyDataUtils.getInstance().queryStep6(pageNo, pageSize);
                        if(null != step6Beans){
                            getV().refreshAdapter6(step6Beans, false);
                        }
                        break;
                }
            }
        }catch (Exception e){}
    }

    public void queryData(Activity activity, int pageNo, int pageSize){
        try {
            this.activity = activity;
            List<DStep1Bean> step1Beans = MyDataUtils.getInstance().queryStep1(pageNo, pageSize);
            List<DStepAllBean> list = new ArrayList<>();
            for (DStep1Bean bean: step1Beans) {
                DStep2Bean bean2 = MyDataUtils.getInstance().queryStep2(bean.getCid(), MyTimeUtils.getNyrByNyrsfm(bean.getTime()));
                DStep3Bean bean3 = MyDataUtils.getInstance().queryStep3(bean.getCid(), MyTimeUtils.getNyrByNyrsfm(bean.getTime()));
                DStep6Bean bean6 = MyDataUtils.getInstance().queryStep6(bean.getCid(), MyTimeUtils.getNyrByNyrsfm(bean.getTime()));
                DStepAllBean data = new DStepAllBean();
                if (bean != null){
                    data.setUserId(bean.getUserId());
                    data.setCompanyId(bean.getCompanyId());
                    data.setCid(bean.getCid());
                    data.setTime(bean.getTime());
                    data.setKhNm(bean.getKhNm());
                }
                //图片
                List<DStepAllBean.PicBean> picList = new ArrayList<>();
                if (bean != null) {
                    List<String> picList1 = bean.getPicList();
                    for (String s : picList1){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("1签到");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                }
                if (bean2 != null) {
                    List<String> picList2 = bean2.getPicList();
                    for (String s : picList2){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("2生动化检查");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                    List<String> pic1List22 = bean2.getPicList2();
                    for (String s : pic1List22){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("2生动化检查");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                }
                if (bean3 != null) {
                    List<String> picList3 = bean3.getPicList();
                    for (String s : picList3){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("3库存检查");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                    List<String> picList32 = bean3.getPicList2();
                    for (String s : picList32){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("3库存检查");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                    List<String> pic1List33 = bean3.getPicList3();
                    for (String s : pic1List33){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("3库存检查");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                }
                if (bean6 != null) {
                    List<String> pic1List6 = bean6.getPicList();
                    for (String s : pic1List6){
                        DStepAllBean.PicBean picBean = new DStepAllBean.PicBean();
                        picBean.setName("4签退");
                        picBean.setPic(s);
                        picList.add(picBean);
                    }
                }
                data.setPicList(picList);
                data.setdStep1Bean(bean);
                data.setdStep2Bean(bean2);
                data.setdStep3Bean(bean3);
                data.setdStep6Bean(bean6);
                list.add(data);
            }
            getV().refreshAdapter(list, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //处理拜访1
    public void doStep1(final DStep1Bean bean, final int type) {
        try {
            String address = bean.getAddress();
            if(MyStringUtil.isEmpty(address) && !MyStringUtil.isEmpty(bean.getLongitude())){
                //没地址信息
                LatLng latLng = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
                reverseGeoCode(latLng, bean, null, type);
            }else{
                doStep1(bean, address, type);
            }
        }catch (Exception e){
        }
    }

    public void doStep1(final DStep1Bean bean, String address, final int type) {
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
                .execute(new MyHttpCallback(activity) {
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
                                if(1 == type){
                                    getV().refreshAdapter(null, true);
                                }else {
                                    getV().refreshAdapter1(null, true);
                                }
                                ToastUtils.showCustomToast("上传成功");
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
    public void doStep2(final DStep2Bean bean, final int type) {
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
                .execute(new MyHttpCallback(activity) {
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
                                if(1 != type){
                                    getV().refreshAdapter2(null, true);
                                    ToastUtils.showCustomToast("上传成功");
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
    public void doStep3(final DStep3Bean bean, final int type) {
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
                .execute(new MyHttpCallback(activity) {
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
                                if(1 != type){
                                    getV().refreshAdapter3(null, true);
                                    ToastUtils.showCustomToast("上传成功");
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
    public void doStep4(final DStep4Bean bean) {
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
                .execute(new MyHttpCallback(activity) {
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
//                                getV().refreshAdapter4(null, true);
//                                ToastUtils.showCustomToast("上传成功");
                            }else{
                                MyDataUtils.getInstance().updateStep4(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep4(bean);
                        }
                    }
                });
    }

    //处理拜访5:下单
    public void doStep5(final DStep5Bean bean) {
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
        String type = bean.getType();
        // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
        switch (type){
            case "1":
                if("0".equals(bean.getCount())){
                    url = Constans.addBforderWeb;
                }else if("1".equals(bean.getCount())){
                    params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                    url = Constans.updateBforderWeb;
                }
                break;
            case "2":
                url = Constans.addDhorderWeb;
                break;
            case "3":
                params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                url = Constans.updateDhorderWeb;
                break;
            case "4":
                url = Constans.addThorderWeb;
                break;
            case "5":
                params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                url = Constans.updateThorderWeb;
                break;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
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
                                getV().refreshAdapter5(null, true);
                                ToastUtils.showCustomToast("上传成功");
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
    public void doStep6(final DStep6Bean bean, final int type) {
        String address = bean.getAddress();
        if(MyStringUtil.isEmpty(address) && !MyStringUtil.isEmpty(bean.getLongitude())){
            //没地址信息
            LatLng latLng = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
            reverseGeoCode(latLng, null, bean, type);
        }else{
            doStep6(bean, address, type);
        }
    }

    public void doStep6(final DStep6Bean bean, final String address, final int type) {
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
                .execute(new MyHttpCallback(activity) {
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
                                if(1 != type){
                                    getV().refreshAdapter6(null, true);
                                    ToastUtils.showCustomToast("上传成功");
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



    //TODO ------------------------接口回調----------------------
    //经纬度转地址
    public void reverseGeoCode(LatLng latLng, final DStep1Bean bean1, final DStep6Bean bean6, final int type) {
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
                    doStep1(bean1, address, type);
                }else if(null != bean6){
                    doStep6(bean6, address, type);
                }
            }
        });
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }


}
