package com.qwb.view.step.parsent;

import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.step.ui.Step6Activity;
import com.qwb.view.step.model.QueryBfgzxcBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：拜访6：拜访签退
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PStep6 extends XPresent<Step6Activity>{
    /**
     * 获取上次提交信息
     */
    public void loadDataInfo(Activity activity, String clientId, String pdateStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if (!MyUtils.isEmptyString(pdateStr)) {
            params.put("date", pdateStr);
        }
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryBfgzxcWeb)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 添加或修改（）
     */
    public void addData(Activity activity,String count6, String xcbfId,String clientId,String longitude,String latitude, String addressStr,String pdateStr,
                        File file_voice,int recodeTime,
                        String bfzjStr,String dbsxStr,String dataStr,String xsjdNm,String bfflNm, List<String> picList, String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
//        params.put("address", addressStr);
        // 补拜访
        if (!MyUtils.isEmptyString(addressStr)) {
            params.put("address", addressStr);
        }
        // 补拜访
        if (!MyUtils.isEmptyString(pdateStr)) {
            params.put("date", pdateStr);
        }
        if (!MyUtils.isEmptyString(bfzjStr)) {
            params.put("bcbfzj", bfzjStr);
        }
        if (!MyUtils.isEmptyString(dbsxStr)) {
            params.put("dbsx", dbsxStr);
        }
        params.put("xcdate", dataStr);
        params.put("xsjdNm", xsjdNm);
        params.put("bfflNm", bfflNm);

        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        // 语音
        if (file_voice != null && recodeTime > 0) {
            files.put("voice", file_voice);// 语音文件
            params.put("voiceTime", String.valueOf((int) recodeTime));// 语音时长
        }
        // 图片数组
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                File oldFile = new File(picList.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(720)  // 默认最大宽度为720
                        .setMaxHeight(1280) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                        .setFileName(String.valueOf(System.currentTimeMillis()+"_"+i)) // 设置你的文件名
                        .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                        .build()
                        .compressToFile(oldFile);
                //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                String absolutePath=newFile.getAbsolutePath();
                String j=absolutePath.substring(absolutePath.length()-6,absolutePath.length()-5);
                files.put("file" + j + 1, newFile);
            }
        }

        String url = null;
        if ("0".equals(count6)) { // 0:（添加） 1:（修改）
            url = Constans.addBfgzxcWeb;
        } else if ("1".equals(count6)) {
            url = Constans.updateBfgzxcWeb;
            params.put("id", String.valueOf(xcbfId));// 拜访拍照签到id
        }
        OkHttpUtils.post()
                .files(files)
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        if(null != getV()){
//                            getV().saveCacheData();
                            getV().submitDataError();
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBfgzxcBean parseObject = JSON.parseObject(response, QueryBfgzxcBean.class);
            if (parseObject != null && parseObject.isState()) {
                if(getV()!=null){
                    getV().showInfo(parseObject);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (null != parseObject && parseObject.getBoolean("state") && null != getV() ) {
                getV().submitDataSuccess();//提交数据成功
            }else{
                ToastUtils.showCustomToast(parseObject.getString("msg"));
                if(null != getV()){
//                    getV().saveCacheData();
                    getV().submitDataError();
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取token
     */
    public void queryToken(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryToken)
                .id(28)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson28(response);
                    }
                });
    }


    //解析数据--获取token
    private void parseJson28(String response) {
        try {
            TokenBean bean = JSON.parseObject(response, TokenBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().doToken(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }



}
