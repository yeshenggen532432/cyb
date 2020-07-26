package com.qwb.view.log.parsent;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.log.ui.LogSubmitActivity;
import com.qwb.view.log.model.LogStrLengthBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.model.FileBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：提交日报；周报，月报
 */
public class PLogSubmit extends XPresent<LogSubmitActivity>{

    /**
     * 限制文字输入的字数
     */
    public void loadDataStrLength(Context context,int type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(type));// 报类型(1日报；2周报；3月报)
        OkHttpUtils.post().params(params).url(Constans.queryreportcdWeb).id(2).build().execute(new MyHttpCallback(null) {
            @Override
            public void myOnError(Call call, Exception e, int id) {

            }

            @Override
            public void myOnResponse(String response, int id) {
                parseJson2(response);
            }
        });
    }
    /**
     * 提交数据
     */
    public void addData(Activity activity, int type, String gzNr, String gzZj, String gzJh, String gzBz, String remo, String ids, ArrayList<FileBean> fileList
            , String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("tp", String.valueOf(type));// 报类型(1日报；2周报；3月报)
        params.put("gzNr", gzNr);
        params.put("gzZj", gzZj);
        params.put("gzJh", gzJh);
        params.put("gzBz", gzBz);
        params.put("remo", remo);
        params.put("usrIds", ids);// 用户id集合（如：15,2,6,23）

        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        // 附件
        if (fileList != null && fileList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            if (fileList.size() == 1) {
                sb.append(fileList.get(0).getFileNm());
            } else {
                for (int i = 0; i < fileList.size(); i++) {
                    FileBean fileBean = fileList.get(i);
                    String fileNm = fileBean.getFileNm();
                    if (i == fileList.size() - 1) {
                        sb.append(fileNm);
                    } else {
                        sb.append(fileNm + ",");
                    }
                }
            }
            params.put("fileNms", sb.toString().trim());
        }
        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        int size = Constans.publish_pics.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                File oldFile=new File(Constans.publish_pics.get(i));
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
                files.put("file1" + j, newFile);
            }
        }
        OkHttpUtils.post().files(files).params(params).url(Constans.addReport).id(1).build().execute(new MyHttpCallback(activity) {
            @Override
            public void myOnError(Call call, Exception e, int id) {

            }

            @Override
            public void myOnResponse(String response, int id) {
                parseJson1(response);
            }
        });
    }


    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
//            e.printStackTrace();
            ToastUtils.showCustomToast(e.getMessage());
            //提交数据失败-保存草稿
            if(id==1){
                if(getV()!=null){
                   getV().saveDataDraft();
                }
            }
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://提交数据
                    parseJson1(response);
                    break;
                case 2:
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据-提交数据
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null) {
                boolean state = jsonObject.getBoolean("state");
                if (state) {
                    if(getV()!=null){
                        getV().addDataSuccess();
                    }
                }else{
                    //提交数据失败-保存草稿
                    if(getV()!=null){
                        getV().saveDataDraft();
                    }
                }
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
            //提交数据失败-保存草稿
            if(getV()!=null){
                getV().saveDataDraft();
            }
        }
    }



    //解析数据-限制文字输入的字数
    private void parseJson2(String response) {
        try {
            LogStrLengthBean parseObject = JSON.parseObject(response, LogStrLengthBean.class);
            if (parseObject != null && parseObject.isState()) {
                if(getV()!=null){
                    getV().setDataStrLength(parseObject);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
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
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }






}
