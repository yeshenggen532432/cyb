package com.qwb.view.flow.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.flow.ui.FlowCameraVoiceActivity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
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
 * 创建描述：流动打卡
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PFlowCameraVoice extends XPresent<FlowCameraVoiceActivity>{

    private Activity mContext;

    public void addFlow(Activity context, String longitude, String latitude, String address, String zb, File voiceFile, long voiceTime, List<String> picList, String khNm) {
        mContext = context;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("mid", SPUtils.getID());
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("address", address);
        params.put("remarks", zb);
        if(!MyStringUtil.isEmpty(khNm)){
            params.put("khNm", khNm);
        }

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);}
        });
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                File oldFile=new File(picList.get(i));
                File newFile = new CompressHelper.Builder(context)
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
                files.put("file" + j, newFile);
            }
        }

        // 语音
        if (null != voiceFile && voiceTime > 0) {
            files.put("voice", voiceFile);// 语音文件
            params.put("voiceTime", String.valueOf((voiceTime)));// 语音时长
        }

        OkHttpUtils.post()
                .files(files)
                .params(params)
                .url(Constans.addFlow)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });




    }


    //解析数据
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if(jsonObject.getBoolean("state")){
                ToastUtils.showCustomToast("添加成功");
                getV().sendLocation(3);
                if(null != mContext){
                    ActivityManager.getInstance().closeActivity(mContext);
                }
            } else {
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


}

