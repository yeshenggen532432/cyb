package com.qwb.view.step.parsent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.step.ui.Step3Activity;
import com.qwb.view.step.model.QueryBfcljccjBean;
import com.qwb.view.step.model.QueryCljccjMdlsBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：拜访3：库存检查
 */
public class PStep3 extends XPresent<Step3Activity> {
    /**
     * 获取模板
     */
    public void loadDataModel(Context context, String pdateStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        if (!MyUtils.isEmptyString(pdateStr)) {
            params.put("date", pdateStr);
        }
        OkHttpUtils.post().params(params).url(Constans.queryCljccjMdls).id(1).build().execute(new MyStringCallback(), context);
    }

    /**
     * 获取上次提交信息
     */
    public void loadDataInfo(Context context, String clientId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        OkHttpUtils.post().params(params).url(Constans.queryBfcljccj).id(2).build().execute(new MyStringCallback(), context);
    }

    /**
     * 添加或修改（）
     */
    public void addData(Activity activity, String count3, String clientId, String jsonStr, String pdateStr, List<String> picList, List<String> picList2, List<String> picList3, String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        params.put("xxjh", jsonStr);
        if (!MyUtils.isEmptyString(pdateStr)) {
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
        if (null != picList3 && !picList3.isEmpty()) {
            for (int i = 0; i < picList3.size(); i++) {
                File oldFile = new File(picList3.get(i));
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
                files.put("file3" + j + 1, newFile);
            }
        }

        String url = null;
        if ("0".equals(count3)) { // 0:（添加） 1:（修改）
            url = Constans.addBfcljccj;
        } else if ("1".equals(count3)) {
            url = Constans.updateBfcljccj;
        }
        OkHttpUtils.post()
                .params(params)
                .files(files)
                .url(url)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        if (null != getV()) {
//                            getV().saveCacheData();
                            getV().submitDataError();
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://模版
                    parseJson1(response);
                case 2://上次提交的信息
                    parseJson2(response);
                    break;
                case 3:// 添加--修改
                    parseJson3(response);
                    break;
            }
        }
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            QueryCljccjMdlsBean parseObject = JSON.parseObject(response, QueryCljccjMdlsBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QueryCljccjMdlsBean.CljccjMdls> list = parseObject.getList();
                if (list != null && !list.isEmpty()) {
                    if (getV() != null) {
                        getV().showModelInfo(list);
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            QueryBfcljccjBean parseObject3 = JSON.parseObject(response, QueryBfcljccjBean.class);
            if (parseObject3 != null) {
                if (parseObject3.isState()) {
                    List<QueryBfcljccjBean.QueryBfcljccj> list = parseObject3.getList();
                    if (list != null && !list.isEmpty()) {
                        if (getV() != null) {
                            getV().showInfo(list);
                        }
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject3.getMsg());
                }
            } else {
                ToastUtils.showCustomToast("陈列检查采集失败！");
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (null != parseObject && parseObject.getBoolean("state") && null != getV()) {
                getV().submitDataSuccess();//提交数据成功
            } else {
                ToastUtils.showCustomToast(parseObject.getString("msg"));
                if (null != getV()) {
//                    getV().saveCacheData();
                    getV().submitDataError();
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
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
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
