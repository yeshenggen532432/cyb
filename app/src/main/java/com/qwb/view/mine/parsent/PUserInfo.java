package com.qwb.view.mine.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.mine.ui.UserInfoActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.mine.model.UserInfoResult;
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
 * 用户信息
 */
public class PUserInfo extends XPresent<UserInfoActivity>{

    private Activity activity;

    /**
     * 获取用户信息
     */
    public void getUserInfo(Activity activity) {
        this.activity = activity;

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.userinfoURL)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson(response, id);
                    }
                });
    }


    /**
     * 修改性别
     */
    public void updateUserSex(Activity activity, String sex) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("sex",sex);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateinfoURL)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            BaseBean bean = JSON.parseObject(response, BaseBean.class);
                            if (bean.isState()) {

                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    /**
     * 修改头像
     */
    public void updateUserHead(Activity activity, File oldFile) {
        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        File newFile = new CompressHelper.Builder(activity)
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(1280) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                .setFileName(String.valueOf(System.currentTimeMillis())) // 设置你的文件名
                .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                .build()
                .compressToFile(oldFile);
        files.put("file", newFile);
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("memberId", SPUtils.getSValues("memId"));
        OkHttpUtils
                .post()
                .files(files)
                .params(params)
                .url(Constans.updateHeadURL)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson(response, id);
                    }
                });

    }


    //1 个人信息 2 修改头像
    public void parseJson(String json, int tag) {
        try {
            switch (tag) {
                case 1:
                    UserInfoResult bean = JSON.parseObject(json, UserInfoResult.class);
                    if (bean != null && bean.isState()) {
                        getV().doUserInfo(bean);
                    } else {
                        ToastUtils.showLongCustomToast(bean.getMsg());
                    }
                    break;
                case 2 :
                    try {
                        BaseBean baseBean = JSON.parseObject(json, BaseBean.class);
                        if (baseBean.isState()) {
                            getUserInfo(activity);
                        }else{
                            ToastUtils.showCustomToast(baseBean.getMsg());
                        }
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                    break ;
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }






    //TODO ------------------------接口回調----------------------




}
