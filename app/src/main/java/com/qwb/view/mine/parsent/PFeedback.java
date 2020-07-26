package com.qwb.view.mine.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.mine.ui.FeedbackActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 意见反馈
 */
public class PFeedback extends XPresent<FeedbackActivity>{

    private Activity activity;

    /**
     * 提交
     * feedType 反馈类型 1 异常问题 2 意见改进
     * */
    public void addData(Activity activity, String feedType, String input) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("feedType", feedType);
        params.put("plat", "android");
        params.put("feedContent", input);

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        int size = Constans.publish_pics.size();
        if (size > 0) {
            params.put("feedType", "2");
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
                files.put("file" + j + 1, newFile);
            }
        }else{
            params.put("feedType", "1");
        }
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(Constans.addFeedbackURL)
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



    public void parseJson1(String json) {
        try {
            BaseBean bean = JSON.parseObject(json, BaseBean.class);
            if (bean.isState()) {
                ToastUtils.showLongCustomToast( "谢谢参与！");
                getV().doAddSuccess();
            }else{
                ToastUtils.showCustomToast( bean.getMsg());
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }





}
