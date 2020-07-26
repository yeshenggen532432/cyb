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
import com.qwb.view.step.ui.Step2Activity;
import com.qwb.view.step.model.QueryBfsdhjcBean;
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
 * 创建描述：拜访2：生动化检查
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PStep2 extends XPresent<Step2Activity>{
    /**
     * 获取上次提交信息
     */
    public void loadDataInfo(Activity activity,String clientId,String pdateStr) {
        Map<String, String> params=new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if (!MyUtils.isEmptyString(pdateStr)) {
            params.put("date", pdateStr);
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBfsdhjc)
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
    public void addData(Activity activity, String count2, String clientId, String sdhId, String pdateStr,
                        String pophbStr, String cqStr, String wqStr, String remo1Str, String remo2Str, String isXy, List<String> picList, List<String> picList2, String queryToken) {

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        // 可选
        if (!MyUtils.isEmptyString(pophbStr)) {
            params.put("pophb", String.valueOf(pophbStr));
        }
        if (!MyUtils.isEmptyString(cqStr)) {
            params.put("cq", String.valueOf(cqStr));
        }
        if (!MyUtils.isEmptyString(wqStr)) {
            params.put("wq", String.valueOf(wqStr));
        }
        if (!MyUtils.isEmptyString(remo1Str)) {
            params.put("remo1", String.valueOf(remo1Str));
        }
        if (!MyUtils.isEmptyString(remo2Str)) {
            params.put("remo2", String.valueOf(remo2Str));
        }
        params.put("isXy", isXy);//底座（1有，2无）

        if (!MyUtils.isEmptyString(pdateStr)) {//拜访计划
            params.put("date", String.valueOf(pdateStr));
        }
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                File oldFile = new File(picList.get(i));
                File newFile = new CompressHelper.Builder(activity)
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
            }
        }

        if (null != picList2 && !picList2.isEmpty()) {
            for (int i = 0; i < picList2.size(); i++) {
                File oldFile = new File(picList2.get(i));
                File newFile = new CompressHelper.Builder(activity)
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
            }
        }

        String url = null;
        if ("0".equals(count2)) { // 0:生动化检查（添加） 1:生动化检查（修改）
            url = Constans.addBfsdhjc;
        } else if ("1".equals(count2)) {
            url = Constans.updateBfsdhjc;
            params.put("id", String.valueOf(sdhId));// 拜访拍照签到id
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


    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBfsdhjcBean parseObject = JSON.parseObject(response, QueryBfsdhjcBean.class);
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
            ToastUtils.showCustomToast(e.getMessage());
        }
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
